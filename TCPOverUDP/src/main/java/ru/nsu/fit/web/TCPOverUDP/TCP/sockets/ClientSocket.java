package ru.nsu.fit.web.TCPOverUDP.TCP.sockets;

import ru.nsu.fit.web.TCPOverUDP.TCP.packet.TCPPacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

public class ClientSocket extends TCPSocket {
    public ClientSocket(int port) throws SocketException {
        super(port);
    }

    @Override
    public void connect(InetAddress address, int port) throws IOException {
        getSocket().connect(address, port);
        sendHandshake();
        receiveHandshakeAck();
        makeBuffers();
        System.err.println("Buffers initialized");
        sendHandshakeAck();
        setSeq(0);
        System.err.println("Thread starts work");
        getTcpThread().start();
    }

    private void sendHandshake() throws IOException {
        TCPPacket packet = new TCPPacket();
        packet.ackNumber = 1;
        packet.seqNumber = getSeq();
        packet.isSyn = true;
        getSocket().send(packet.makePacket());
        setSeq(getSeq() + 1);
    }

    private void receiveHandshakeAck() throws IOException {
        TCPPacket packet = new TCPPacket();
        int receivedLength = packet.getHeaderLength();
        DatagramPacket receivedPacket = new DatagramPacket(new byte[receivedLength], receivedLength);
        getSocket().receive(receivedPacket);
        packet.extractPacket(receivedPacket);
    }

    private void sendHandshakeAck() throws IOException {
        TCPPacket packet = new TCPPacket();
        packet.ackNumber = 2;
        packet.seqNumber = getSeq();
        packet.isAck = true;
        getSocket().send(packet.makePacket());
        setSeq(getSeq() + 1);
    }

    @Override
    public void close() throws IOException, InterruptedException {
        if (!getTcpThread().isInterrupted() && getTcpThread().isAlive()) {
            try {
                sendFin();
            } catch (IOException | InterruptedException e) {
                getTcpThread().interrupt();
                throw e;
            }
        } else {
            getSocket().close();
            getTimer().cancel();
        }
    }
}

