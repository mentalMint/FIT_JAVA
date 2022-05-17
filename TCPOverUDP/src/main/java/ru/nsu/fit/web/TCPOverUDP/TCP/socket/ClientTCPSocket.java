package ru.nsu.fit.web.TCPOverUDP.TCP.socket;

import ru.nsu.fit.web.TCPOverUDP.TCP.socket.packet.TCPPacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

public class ClientTCPSocket extends TCPSocket {
    public ClientTCPSocket(int port) throws SocketException {
        super(port);
    }

    private void sendHandshake() throws IOException {
        TCPPacket packet = new TCPPacket();
        packet.ack = getAck();
        packet.seq = getSeq();
        packet.isSyn = true;
        getSocket().send(packet.makePacket());
        setSeq(getSeq() + 1);
    }

    private void receiveHandshakeAck() throws IOException {
        int receivedLength = 2 * Integer.BYTES;
        DatagramPacket receivedPacket = new DatagramPacket(new byte[receivedLength], receivedLength);
        getSocket().receive(receivedPacket);
        TCPPacket packet = new TCPPacket();
        packet.extractPacket(receivedPacket);

        System.err.println("Seq: " + packet.seq + " Ack: " + packet.ack);
        setAck(getAck() + 1);
    }

    private void sendHandshakeAck() throws IOException {
        TCPPacket packet = new TCPPacket();
        packet.ack = getAck();
        packet.seq = getSeq();
        packet.isAck = true;
        getSocket().send(packet.makePacket());
        setSeq(getSeq() + 1);
    }

    @Override
    public void connect(InetAddress address, int port) {
        getSocket().connect(address, port);
        try {
            sendHandshake();
            receiveHandshakeAck();
            sendHandshakeAck();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
