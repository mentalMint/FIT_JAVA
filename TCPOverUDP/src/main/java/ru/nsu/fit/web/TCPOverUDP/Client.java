package ru.nsu.fit.web.TCPOverUDP;

import ru.nsu.fit.web.TCPOverUDP.TCP.sockets.ClientSocket;
import ru.nsu.fit.web.TCPOverUDP.TCP.sockets.TCPSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class Client {
    public static void main(String[] args) {
        TCPSocket socket;
        try {
            socket = new ClientSocket(7000);
            try {
                InetAddress address = InetAddress.getByName("127.0.0.1");
                socket.connect(address, 8000);
                byte[] buf;
                for (int i = 0; i < 10; i++) {
                    buf = socket.receive();
                    System.err.println("Received message " + i + ": " + new String(buf));
                }
                String str = "You picked the wrong house, fool!";
                socket.send(str.getBytes());

                for (int i = 0; i < 10; i++) {
                    str = "Message: " + i;
                    socket.send(str.getBytes(StandardCharsets.UTF_8));
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("Execute Server first");
                e.printStackTrace();
            } finally {
                socket.close();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
