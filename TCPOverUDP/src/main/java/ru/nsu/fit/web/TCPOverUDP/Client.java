package ru.nsu.fit.web.TCPOverUDP;

import ru.nsu.fit.web.TCPOverUDP.TCP.socket.TCPSocket;
import ru.nsu.fit.web.TCPOverUDP.TCP.socket.ClientTCPSocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class Client {
    public static void main(String[] args) {
        try (TCPSocket socket = new ClientTCPSocket(7000)) {
            InetAddress address = InetAddress.getByName("127.0.0.1");
//            socket.connect(address, 8000);
            String str = "You picked the wrong house, fool!";
            DatagramPacket packet = new DatagramPacket(str.getBytes(), str.length());
            socket.sendNow(packet);
            str = "Roger, roger, Roger, roger, Roger, roger, Roger, roger, Roger, roger";
            packet = new DatagramPacket(str.getBytes(), str.length());
            socket.sendNow(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
