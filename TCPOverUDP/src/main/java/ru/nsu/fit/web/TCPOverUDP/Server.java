package ru.nsu.fit.web.TCPOverUDP;

import ru.nsu.fit.web.TCPOverUDP.TCP.socket.TCPSocket;
import ru.nsu.fit.web.TCPOverUDP.TCP.socket.ServerTCPSocket;

import java.io.IOException;
import java.net.InetAddress;

public class Server {
    public static void main(String[] args) {
        try (TCPSocket socket = new ServerTCPSocket(8000)) {
            InetAddress address = InetAddress.getByName("127.0.0.1");
            socket.connect(address, 7000);
            byte[] buf;
            buf = socket.receive(1024);
            System.out.println(new String(buf));
            buf = socket.receive(1024);
            System.out.println(new String(buf));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
