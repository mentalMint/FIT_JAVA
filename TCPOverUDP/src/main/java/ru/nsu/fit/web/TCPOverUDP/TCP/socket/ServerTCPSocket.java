package ru.nsu.fit.web.TCPOverUDP.TCP.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

public class ServerTCPSocket extends TCPSocket{
    public ServerTCPSocket(int port) throws SocketException {
        super(port);
    }

    @Override
    public void connect(InetAddress address, int port) {
        socket.connect(address, port);
        byte[] buf = new byte[1];
        buf[0] = 'C';
        DatagramPacket packet = new DatagramPacket(buf, 1);
        try {
            receive(packet);
            String str = new String(packet.getData(), 0, packet.getLength());
            System.err.println(str);
            sendNow(new DatagramPacket(buf, 0));
            receive(packet);
            str = new String(packet.getData(), 0, packet.getLength());
            System.err.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
