package ru.nsu.fit.web.TCPOverUDP;

import ru.nsu.fit.web.TCPOverUDP.TCP.sockets.ServerSocket;
import ru.nsu.fit.web.TCPOverUDP.TCP.sockets.TCPSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String[] args) {
        try {
            TCPSocket socket = new ServerSocket(8000);
            InetAddress address = InetAddress.getByName("127.0.0.1");
            socket.connect(address, 7000);
            for (int i = 0; i < 10; i++) {
                String str = "Message: " + i;
                socket.send(str.getBytes(StandardCharsets.UTF_8));
            }
            byte[] buf;
            buf = socket.receive();
            System.err.println("Received message : " + new String(buf));
            for (int i = 0; i < 10; i++) {
                buf = socket.receive();
                System.err.println("Received message " + i + ": " + new String(buf));
            }
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
