package ru.nsu.fit.web.TCPOverUDP.SR;

import ru.nsu.fit.web.TCPOverUDP.TCP.socket.packet.TCPPacket;

import java.util.ArrayList;

public class SendBuffer {
    private final TCPPacket[] buffer;
    private final Boolean[] acknowledged;
    private final Boolean[] waitForSend;

    private final int windowSize = 8;
    private int base = 0;
    private int length = 16;

    public SendBuffer(int length) {
        this.length = length;
        buffer = new TCPPacket[length];
        acknowledged = new Boolean[length];
        waitForSend =  new Boolean[length];
        for (int i = 0; i < length; i++) {
            acknowledged[i] = false;
            waitForSend[i] = false;
        }
    }

    public Boolean[] getAcknowledged() {
        return acknowledged;
    }

    public Boolean[] getWaitForSend() {
        return waitForSend;
    }

    public SendBuffer() {
        buffer = new TCPPacket[length];
        acknowledged = new Boolean[length];
        waitForSend =  new Boolean[length];
        for (int i = 0; i < length; i++) {
            acknowledged[i] = false;
            waitForSend[i] = false;
        }
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

    public void put(TCPPacket packet) {
        buffer[packet.seq] = packet;
    }

    public void acknowledge(int index) {
        if (index >= base && index < base + windowSize) {
            acknowledged[index] = true;
            if (index == base) {
                int newBase = base;
                for (int i = base; i < windowSize; i++) {
                    if (acknowledged[i]) {
                        newBase =  i;
                    } else {
                        break;
                    }
                }
                base = newBase;
            }
        }
    }
}
