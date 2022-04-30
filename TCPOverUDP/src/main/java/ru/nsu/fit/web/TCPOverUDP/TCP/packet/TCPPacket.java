package ru.nsu.fit.web.TCPOverUDP.TCP.packet;

import java.net.DatagramPacket;

public class TCPPacket {
    DatagramPacket packet;

    public TCPPacket(byte[] buf, int length) {
        this.packet = new DatagramPacket(buf, length);
    }
}
