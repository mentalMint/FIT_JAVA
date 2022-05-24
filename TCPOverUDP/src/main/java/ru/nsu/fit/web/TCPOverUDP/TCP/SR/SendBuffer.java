package ru.nsu.fit.web.TCPOverUDP.TCP.SR;

import ru.nsu.fit.web.TCPOverUDP.TCP.packet.TCPPacket;

public class SendBuffer {
    private TCPPacket[] buffer = null;
    private Boolean[] acknowledged = null;
    private Boolean[] waitForSend = null;
    private Integer[] duplicateAcksNumbers = null;
    private final int windowSize = 8;
    private int base = 0;
    private int length = 16;
    private int packetsToAckNumber = 0;
    private int packetsWaitToSendNumber = 0;

    public void init() {
        buffer = new TCPPacket[length];
        acknowledged = new Boolean[length];
        waitForSend =  new Boolean[length];
        duplicateAcksNumbers =  new Integer[length];

        for (int i = 0; i < length; i++) {
            acknowledged[i] = false;
            waitForSend[i] = false;
            duplicateAcksNumbers[i] = 0;
        }
    }

    public SendBuffer() {
    }

    public SendBuffer(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Negative length");
        }
        this.length = length;
    }

    public int getPacketsWaitToSendNumber() {
        return packetsWaitToSendNumber;
    }

    synchronized public void incrementPacketsWaitToSendNumber() {
        packetsWaitToSendNumber++;
    }


    public int getDuplicateAckNumber(int index) {
        return duplicateAcksNumbers[index];
    }

    public void incrementDuplicateAckNumber(int index) {
        duplicateAcksNumbers[index] = (duplicateAcksNumbers[index] + 1) % 4;
    }

    synchronized public int getPacketsToAckNumber() {
        return packetsToAckNumber;
    }

    public TCPPacket getPacket(int index) {
        return buffer[index % length];
    }

    synchronized public void incrementPacketsToAckNumber() {
        packetsToAckNumber++;
    }

    synchronized public void decrementPacketsToAckNumber() {
        packetsToAckNumber--;
    }

    synchronized public Boolean[] getAcknowledged() {
        return acknowledged;
    }

    synchronized public Boolean[] getWaitForSend() {
        return waitForSend;
    }

    synchronized public int getWindowSize() {
        return windowSize;
    }

    synchronized public int getBase() {
        return base;
    }

    synchronized public void setBase(int base) {
        this.base = base % length;
    }

    synchronized public TCPPacket[] getBuffer() {
        return buffer;
    }

    public int getLength() {
        return length;
    }

    synchronized public void put(TCPPacket packet) {
        buffer[packet.seqNumber] = packet;
    }

    synchronized public void acknowledge(int index) {
            acknowledged[index % length] = true;
    }
}
