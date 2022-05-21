package ru.nsu.fit.web.TCPOverUDP.SR;

import ru.nsu.fit.web.TCPOverUDP.TCP.socket.packet.TCPPacket;

import java.util.ArrayList;

public class ReceiveBuffer {
    private final TCPPacket[] buffer;
    private final Boolean[] received;
    private final int windowSize = 8;
    private int base = 0;
    private int length = 16;

    public ReceiveBuffer(int length) {
        this.length = length;
        buffer = new TCPPacket[length];
        received = new Boolean[length];
        for (int i = 0; i < length; i++) {
            received[i] = false;
        }
    }

    public ReceiveBuffer() {
        buffer = new TCPPacket[length];
        received = new Boolean[length];
        for (int i = 0; i < length; i++) {
            received[i] = false;
        }
    }

    public int getWindowSize() {
        return windowSize;
    }

    public int getBase() {
        return base;
    }

    public TCPPacket[] getBuffer() {
        return buffer;
    }

    public int getLength() {
        return length;
    }

    public TCPPacket get(int index) {
        if (index >= base - windowSize && index < base) {
            return buffer[index];
        } else {
            return null;
        }
    }

    public void put(TCPPacket packet) {
        if (packet.seq >= base && packet.seq < base + windowSize) {
            buffer[packet.seq] = packet;
            if (packet.seq == base) {
                int newBase = base;
                for (int i = base; i < windowSize; i++) {
                    if (received[i]) {
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
