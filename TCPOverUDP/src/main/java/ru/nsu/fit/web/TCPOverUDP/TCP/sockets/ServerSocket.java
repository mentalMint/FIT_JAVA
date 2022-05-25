package ru.nsu.fit.web.TCPOverUDP.TCP.sockets;

import ru.nsu.fit.web.TCPOverUDP.TCP.packet.TCPPacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

public class ServerSocket extends TCPSocket {
    public ServerSocket(int port) throws SocketException {
        super(port);
    }

    @Override
    public void connect(InetAddress address, int port) {
        getSocket().connect(address, port);
        try {
            receiveHandshake();
            makeBuffers();
            System.err.println("Buffers initialized");
            sendHandshakeAck();
            receiveHandshakeAck();
            setSeq(0);
            getReceiver().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveHandshake() throws IOException {
        TCPPacket packet = new TCPPacket();
        int receivedLength = packet.getHeaderLength();
        DatagramPacket receivedPacket = new DatagramPacket(new byte[receivedLength], receivedLength);
        getSocket().receive(receivedPacket);
        packet.extractPacket(receivedPacket);
    }

    private void sendHandshakeAck() throws IOException {
        TCPPacket packet = new TCPPacket();
        packet.ackNumber = 1;
        packet.seqNumber = getSeq();
        packet.isSyn = true;
        packet.isAck = true;
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

    @Override
    public void close() throws IOException {
        try {
            synchronized (getMutex()) {
                if (!getIsFinReceived()) {
                    getMutex().wait();
                }
            }
            sendFinAck();
            synchronized (getMutex2()) {
                if (getSendBuffer().getPacketsToAckNumber() > 0 || getSendBuffer().getPacketsWaitToSendNumber() > 0) {
                    getMutex2().wait();
                    System.err.println("End");

                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            getTimer().cancel();
            getReceiver().interrupt();
            getSocket().close();
        }
    }
}
