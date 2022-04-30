package ru.nsu.fit.web.TCPOverUDP.TCP.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

public class ClientTCPSocket extends TCPSocket {
    public ClientTCPSocket(int port) throws SocketException {
        super(port);
    }

    @Override
    public void connect(InetAddress address, int port) {
        socket.connect(address, port);
        byte[] buf = new byte[1];
        DatagramPacket packet = new DatagramPacket(buf, 1);
        try {
            sendNow(new DatagramPacket(buf, 1));
            receive(packet);
            String str = new String(packet.getData(), 0, packet.getLength());
            System.err.println(str);
            sendNow(new DatagramPacket(buf, 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
