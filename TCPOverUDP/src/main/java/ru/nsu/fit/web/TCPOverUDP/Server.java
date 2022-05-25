package ru.nsu.fit.web.TCPOverUDP;

import ru.nsu.fit.web.TCPOverUDP.TCP.sockets.ServerSocket;
import ru.nsu.fit.web.TCPOverUDP.TCP.sockets.TCPSocket;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String[] args) {
//        try {
//            DatagramSocket datagramSocket = new DatagramSocket(8000);
//            InetAddress address = InetAddress.getByName("127.0.0.1");
//            datagramSocket.connect(address, 7000);
////            byte[] buf = new byte[1];
////            datagramSocket.receive(new DatagramPacket(buf, 0, 1));
//            datagramSocket.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try (TCPSocket socket = new ServerSocket(8000)) {
            InetAddress address = InetAddress.getByName("127.0.0.1");
            socket.connect(address, 7000);
//            String str = "You picked the wrong house, fool!";
//            socket.send(str.getBytes());
//            str = "Roger, roger, Roger, roger, Roger, roger, Roger, roger, Roger, roger";
//            socket.send(str.getBytes(StandardCharsets.UTF_8));
            for (int i = 0; i < 10; i++) {
                String str = "Message: " + i;
                socket.send(str.getBytes(StandardCharsets.UTF_8));
            }
            byte[] buf;
            buf = socket.receive();
            System.err.println("Received message 1: " + new String(buf));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
