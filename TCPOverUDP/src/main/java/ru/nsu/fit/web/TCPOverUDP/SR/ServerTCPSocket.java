package ru.nsu.fit.web.TCPOverUDP.SR;

import ru.nsu.fit.web.TCPOverUDP.TCP.socket.packet.TCPPacket;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ServerTCPSocket implements Closeable {
    private final DatagramSocket socket;
    private Integer seq = 0;
    private Integer ack = 0;
    private final long timeoutInterval = 500;
    private SendBuffer sendBuffer = null;
    private ReceiveBuffer receiveBuffer = null;

    public ServerTCPSocket(int port) throws SocketException {
        this.socket = new DatagramSocket(port);
    }

    private void makeBuffers() {
        sendBuffer = new SendBuffer();
        receiveBuffer = new ReceiveBuffer();
    }

    private void receiveHandshake() throws IOException {
        int receivedLength = 2 * Integer.BYTES;
        DatagramPacket receivedPacket = new DatagramPacket(new byte[receivedLength], receivedLength);
        socket.receive(receivedPacket);
        TCPPacket packet = new TCPPacket();
        packet.extractPacket(receivedPacket);
        ack++;
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
        packet.seq = seq;
        packet.isSyn = true;
        packet.isAck = true;
        socket.send(packet.makePacket());
        seq++;
    }

    private void sendNow(TCPPacket packetToSend) throws IOException {
        socket.send(packetToSend.makePacket());
    }

    public void send(byte[] buf) throws IOException {
        TCPPacket packetToSend = new TCPPacket();
        packetToSend.ack = ack;
        packetToSend.seq = seq;
        packetToSend.length = buf.length;
        packetToSend.buf = buf;
        sendBuffer.put(packetToSend);
        if (packetToSend.seq < sendBuffer.getBase() + sendBuffer.getWindowSize()) {
            sendNow(packetToSend);
            sendBuffer.getWaitForSend()[packetToSend.seq] = false;
        } else {
            sendBuffer.getWaitForSend()[packetToSend.seq] = true;
        }
        seq++;
    }

    private void moveWindow(int index) throws IOException {
        if (index == sendBuffer.getBase()) {
            int newBase = sendBuffer.getBase();
            for (int i = sendBuffer.getBase(); i < sendBuffer.getWindowSize(); i++) {
                if (sendBuffer.getAcknowledged()[i]) {
                    newBase = i;
                    if (sendBuffer.getWaitForSend()[i]) {
                        sendNow(sendBuffer.getBuffer()[i]);
                    }
                } else {
                    break;
                }
            }
            sendBuffer.setBase(newBase);
        }
    }

    private void acknowledge(int index) throws IOException {
        if (index >= sendBuffer.getBase() && index < sendBuffer.getBase() + sendBuffer.getWindowSize()) {
            sendBuffer.getAcknowledged()[index] = true;
        }
    }

    private void receiveAck() throws IOException {
        int receivedLength = 2 * Integer.BYTES;
        DatagramPacket receivedPacket = new DatagramPacket(new byte[receivedLength], receivedLength);
        socket.receive(receivedPacket);
        TCPPacket packet = new TCPPacket();
        packet.extractPacket(receivedPacket);
        ack++;
        acknowledge(packet.seq);
        moveWindow(packet.seq);
    }

    public class Task implements Runnable {
        public Task() {
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted() && !socket.isClosed()) {
                try {
                    receiveAck();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Thread sender;


    public void connect(InetAddress address, int port) {
        makeBuffers();
        socket.connect(address, port);
        try {
            receiveHandshake();
            sendHandshakeAck();
            receiveHandshakeAck();
            ack = 0;
            seq = 0;
            sender = new Thread(new Task());
            sender.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        sender.interrupt();
        socket.close();
    }
}
