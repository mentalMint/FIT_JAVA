package ru.nsu.fit.web.TCPOverUDP;

import ru.nsu.fit.web.TCPOverUDP.TCP.sockets.ClientSocket;
import ru.nsu.fit.web.TCPOverUDP.TCP.sockets.TCPSocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
    public static void main(String[] args) {
//        try {
//            DatagramSocket datagramSocket = new DatagramSocket(7000);
//            InetAddress address = InetAddress.getByName("127.0.0.1");
//            datagramSocket.connect(address, 8000);
//            byte[] buf = new byte[1];
//            datagramSocket.receive(new DatagramPacket(buf, 0, 1));
//            datagramSocket.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try (TCPSocket socket = new ClientSocket(7000)) {
            InetAddress address = InetAddress.getByName("127.0.0.1");
            socket.connect(address, 8000);
            byte[] buf;
            for (int i = 0; i < 10; i++) {
                buf = socket.receive();
                System.err.println("Received message " + i + ": " + new String(buf));
            }
//            buf = socket.receive();
//            System.err.println("Received message 1: " + new String(buf));
//            buf = socket.receive();
//            System.err.println("Received message 2: " + new String(buf));
            String str = "You picked the wrong house, fool!";
            socket.send(str.getBytes());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
