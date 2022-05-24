package ru.nsu.fit.web.TCPOverUDP.TCP.SR;

import ru.nsu.fit.web.TCPOverUDP.TCP.packet.TCPPacket;

import java.util.ArrayList;

public class TCPBuffer {
    private ArrayList<TCPPacket> buffer;
    private int windowSize = 4;
    private int base = 0;
    private int length = 8;

    public TCPBuffer(int length) {
        this.length = length;
        this.buffer = new ArrayList<>(length);
    }

    public TCPBuffer() {
        this.buffer = new ArrayList<>(length);
    }

    public int getWindowSize() {
        return windowSize;
    }

    public int getBase() {
        return base;
    }

    public ArrayList<TCPPacket> getBuffer() {
        return buffer;
    }

    public int getLength() {
        return length;
    }

    public void put(TCPPacket packet) {
        buffer.add(packet);
    }
}
