package ru.nsu.fit.web.TCPOverUDP.SR;

import ru.nsu.fit.web.TCPOverUDP.TCP.socket.packet.TCPPacket;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ClientTCPSocket implements Closeable {
    private final DatagramSocket socket;
    private Integer expectedSeqNum = 0;
    private Integer seq = 0;
    private Integer ack = 0;
    private final long timeoutInterval = 500;
    private SendBuffer sendBuffer = null;
    private ReceiveBuffer receiveBuffer = null;

    public ClientTCPSocket(int port) throws SocketException {
        this.socket = new DatagramSocket(port);
    }

    private void makeBuffers() {
        sendBuffer = new SendBuffer();
        receiveBuffer = new ReceiveBuffer();
    }


    private void sendHandshake() throws IOException {
        TCPPacket packet = new TCPPacket();
        packet.ack = ack;
        packet.seq = expectedSeqNum;
        packet.isSyn = true;
        socket.send(packet.makePacket());
        expectedSeqNum++;
    }

    private void receiveHandshakeAck() throws IOException {
        int receivedLength = 2 * Integer.BYTES;
        DatagramPacket receivedPacket = new DatagramPacket(new byte[receivedLength], receivedLength);
        socket.receive(receivedPacket);
        TCPPacket packet = new TCPPacket();
        packet.extractPacket(receivedPacket);
        ack++;
    }

    private void sendHandshakeAck() throws IOException {
        TCPPacket packet = new TCPPacket();
        packet.ack = ack;
        packet.seq = expectedSeqNum;
        packet.isAck = true;
        socket.send(packet.makePacket());
        expectedSeqNum++;
    }

    public byte[] receive() throws IOException, InterruptedException {
        synchronized (receiveBuffer) {
            if (seq >= receiveBuffer.getBase() - receiveBuffer.getWindowSize() && seq < receiveBuffer.getBase()) {
                return receiveBuffer.getBuffer()[seq].buf;
            }
            receiveBuffer.wait();
            return receiveBuffer.getBuffer()[seq].buf;
        }
    }

    private void receivePacket() throws IOException {
        int receivedLength = 1024 + 2 * Integer.BYTES;
        DatagramPacket receivedPacket = new DatagramPacket(new byte[receivedLength], receivedLength);
        socket.receive(receivedPacket);
        TCPPacket packet = new TCPPacket();
        packet.extractPacket(receivedPacket);
//        System.err.println("Seq: " + packet.seq + " Ack: " + packet.ack);
        ack++;

        synchronized (receiveBuffer) {
            receiveBuffer.put(packet);
            receiveBuffer.notify();
        }
    }

    private void sendAck() throws IOException {
        TCPPacket packetToSend = new TCPPacket();
        packetToSend.ack = ack;
        packetToSend.seq = expectedSeqNum;
        packetToSend.isAck = true;
        socket.send(packetToSend.makePacket());
        expectedSeqNum++;
    }

    public class ReceiveTask implements Runnable {
        public ReceiveTask() {
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    receivePacket();
                    sendAck();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //    private Thread sender;
    private Thread receiver;

    public void connect(InetAddress address, int port) {
        makeBuffers();
        socket.connect(address, port);
        try {
            sendHandshake();
            receiveHandshakeAck();
            sendHandshakeAck();
            ack = 0;
            seq = 0;
//             sender = new Thread(new SendTask());
            receiver = new Thread(new ReceiveTask());
            receiver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        receiver.interrupt();
        socket.close();
    }
}
