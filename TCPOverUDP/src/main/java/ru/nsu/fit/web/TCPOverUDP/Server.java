package ru.nsu.fit.web.TCPOverUDP;

import ru.nsu.fit.web.TCPOverUDP.TCP.socket.TCPSocket;
import ru.nsu.fit.web.TCPOverUDP.TCP.socket.ServerTCPSocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class Server {
    public static void main(String[] args) {
        try (TCPSocket socket = new ServerTCPSocket(8000)) {
            InetAddress address = InetAddress.getByName("127.0.0.1");
            socket.connect(address, 7000);
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, 1024);
            socket.receive(packet);
            String str = new String(packet.getData(), 0, packet.getLength());
            System.out.println(str);
            packet.setData(buf);
            packet.setLength(1024);
            socket.receive(packet);
            str = new String(packet.getData(), 0, packet.getLength());
            System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
