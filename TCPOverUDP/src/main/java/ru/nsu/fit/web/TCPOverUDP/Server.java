package ru.nsu.fit.web.TCPOverUDP;

//import ru.nsu.fit.web.TCPOverUDP.TCP.socket.ServerTCPSocket;
import ru.nsu.fit.web.TCPOverUDP.TCP.SR.ServerSocket;
import ru.nsu.fit.web.TCPOverUDP.TCP.SR.ServerTCPSocket;
import ru.nsu.fit.web.TCPOverUDP.TCP.SR.TCPSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String[] args) {
//        try (TCPSocket socket = new ServerTCPSocket(8000)) {
//            InetAddress address = InetAddress.getByName("127.0.0.1");
//            socket.connect(address, 7000);
//            byte[] buf;
//            buf = socket.receive(1024);
//            System.out.println(new String(buf));
//            buf = socket.receive(1024);
//            System.out.println(new String(buf));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try (TCPSocket socket = new ServerSocket(8000)) {
            InetAddress address = InetAddress.getByName("127.0.0.1");
            socket.connect(address, 7000);
            String str = "You picked the wrong house, fool!";
            socket.send(str.getBytes());
            str = "Roger, roger, Roger, roger, Roger, roger, Roger, roger, Roger, roger";
            socket.send(str.getBytes(StandardCharsets.UTF_8));
            byte[] buf;
            buf = socket.receive();
            System.err.println("Received message 1: " + new String(buf));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
