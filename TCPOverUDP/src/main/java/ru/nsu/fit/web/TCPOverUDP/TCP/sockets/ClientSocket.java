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
    public void connect(InetAddress address, int port) {
        getSocket().connect(address, port);
        try {
            sendHandshake();
            receiveHandshakeAck();
            makeBuffers();
            System.err.println("Buffers initialized");
            sendHandshakeAck();
            setSeq(0);
            getReceiver().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void close() throws IOException {
        try {
            sendFin();
            synchronized (getMutex3()) {
                if (!getIsFinReceived()) {
                    getMutex3().wait();
                    System.err.println("End");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            getTimer().cancel();
//            getReceiver().interrupt();
            getSocket().close();
        }
    }
}

