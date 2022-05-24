package ru.nsu.fit.web.TCPOverUDP.TCP.SR;

import ru.nsu.fit.web.TCPOverUDP.TCP.packet.TCPPacket;

public class ReceiveBuffer {
    private TCPPacket[] buffer = null;
    private Boolean[] received = null;
    private final int windowSize = 2;
    private int base = 0;
    private int length = 4;

    public void init() {
        buffer = new TCPPacket[length];
        received = new Boolean[length];
        for (int i = 0; i < length; i++) {
            received[i] = false;
        }
    }

    public ReceiveBuffer(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Negative length");
        }
        this.length = length;
    }

    public ReceiveBuffer() {
    }

    public boolean isPacketReceived(int index) {
        return received[index % length];
    }

    public void setIsPacketReceived(int index, boolean state) {
        received[index % length] = state;
    }

    public TCPPacket getPacket(int index) {
        return buffer[index % length];
    }

    public int getWindowSize() {
        return windowSize;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public TCPPacket[] getBuffer() {
        return buffer;
    }

    public int getLength() {
        return length;
    }

    public TCPPacket get(int index) {
        return buffer[index % length];
    }

    public void put(TCPPacket packet) {
        buffer[packet.seqNumber % length] = packet;
    }
}
