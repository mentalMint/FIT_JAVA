package ru.nsu.fit.web.TCPOverUDP;

import ru.nsu.fit.web.TCPOverUDP.TCP.SR.ClientSocket;
import ru.nsu.fit.web.TCPOverUDP.TCP.SR.ClientTCPSocket;
import ru.nsu.fit.web.TCPOverUDP.TCP.SR.TCPSocket;

import java.io.IOException;
import java.net.InetAddress;

public class Client {
    public static void main(String[] args) {
//        try (TCPSocket socket = new ClientTCPSocket(7000)) {
//            InetAddress address = InetAddress.getByName("127.0.0.1");
//            socket.connect(address, 8000);
//            String str = "You picked the wrong house, fool!";
//            socket.send(str.getBytes());
//            str = "Roger, roger, Roger, roger, Roger, roger, Roger, roger, Roger, roger";
//            socket.send(str.getBytes(StandardCharsets.UTF_8));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try (TCPSocket socket = new ClientSocket(7000)) {
            InetAddress address = InetAddress.getByName("127.0.0.1");

            socket.connect(address, 8000);
            byte[] buf;

            buf = socket.receive();
            System.err.println("Received message 1: " + new String(buf));
            buf = socket.receive();
            System.err.println("Received message 2: " + new String(buf));
            String str = "You picked the wrong house, fool!";
            socket.send(str.getBytes());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
