package ru.nsu.fit.web.TCPOverUDP.TCP.SR;

import ru.nsu.fit.web.TCPOverUDP.TCP.packet.TCPPacket;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ServerTCPSocket implements Closeable {
    private final DatagramSocket socket;
    private Integer seqNumber = 0;
    private Integer ackNumber = 0;
    private final long timeoutInterval = 500;
    private SendBuffer sendBuffer = null;
    private ReceiveBuffer receiveBuffer = null;
    private Integer expectedSeqNum = 0;
    private Integer receiveNumber = 0;

    public ServerTCPSocket(int port) throws SocketException {
        this.socket = new DatagramSocket(port);
    }

    private void makeBuffers() {
        sendBuffer = new SendBuffer();
        receiveBuffer = new ReceiveBuffer();
    }

    private void receiveHandshake() throws IOException {
        int receivedLength = 2 * Integer.BYTES + 1;
        DatagramPacket receivedPacket = new DatagramPacket(new byte[receivedLength], receivedLength);
        socket.receive(receivedPacket);
        TCPPacket packet = new TCPPacket();
        packet.extractPacket(receivedPacket);
        ackNumber++;
    }

    private void receiveHandshakeAck() throws IOException {
        int receivedLength = 2 * Integer.BYTES + 1;
        DatagramPacket receivedPacket = new DatagramPacket(new byte[receivedLength], receivedLength);
        socket.receive(receivedPacket);
        TCPPacket packet = new TCPPacket();
        packet.extractPacket(receivedPacket);
        ackNumber++;
    }

    private void sendHandshakeAck() throws IOException {
        TCPPacket packet = new TCPPacket();
        packet.ackNumber = ackNumber;
        packet.seqNumber = seqNumber;
        packet.isSyn = true;
        packet.isAck = true;
        socket.send(packet.makePacket());
        seqNumber++;
    }

    public byte[] receive() throws InterruptedException {
        synchronized (receiveBuffer) {
            if (receiveNumber >= receiveBuffer.getBase() - receiveBuffer.getWindowSize() && receiveNumber < receiveBuffer.getBase()) {
                return receiveBuffer.getBuffer()[receiveNumber++].buf;
            }
            receiveBuffer.wait();
            return receiveBuffer.getBuffer()[receiveNumber++].buf;
        }
    }

    private void sendNow(TCPPacket packetToSend) throws IOException {
        socket.send(packetToSend.makePacket());
    }

    public void send(byte[] buf) throws IOException {
        TCPPacket packetToSend = new TCPPacket();
        packetToSend.ackNumber = ackNumber;
        packetToSend.seqNumber = seqNumber;
        packetToSend.length = buf.length;
        packetToSend.buf = buf;
        sendBuffer.put(packetToSend);
        if (packetToSend.seqNumber < sendBuffer.getBase() + sendBuffer.getWindowSize()) {
            sendNow(packetToSend);
            sendBuffer.incrementPacketsToAckNumber();
            System.err.println("Sent " + seqNumber);
            sendBuffer.getWaitForSend()[packetToSend.seqNumber] = false;
        } else {
            sendBuffer.getWaitForSend()[packetToSend.seqNumber] = true;
        }
        seqNumber++;
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

    private TCPPacket receivePacket() throws IOException {
        int receivedLength = 1024 + 2 * Integer.BYTES + 1;
        DatagramPacket receivedPacket = new DatagramPacket(new byte[receivedLength], receivedLength);
        socket.receive(receivedPacket);
        TCPPacket packet = new TCPPacket();
        packet.extractPacket(receivedPacket);
        System.err.println("Received " + packet.seqNumber + " Is ack " + packet.isAck);
        return packet;
    }

    private void sendAck() throws IOException {
        TCPPacket packetToSend = new TCPPacket();
        packetToSend.ackNumber = ackNumber;
        packetToSend.seqNumber = seqNumber;
        packetToSend.isAck = true;
        socket.send(packetToSend.makePacket());
        System.err.println("Sent " + packetToSend.seqNumber);
        seqNumber++;
        expectedSeqNum++;
    }

    public class Task implements Runnable {
        public Task() {
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    TCPPacket packet = receivePacket();
                    if (packet.isAck) {
                        ackNumber++;
                        sendBuffer.acknowledge(packet.seqNumber);
                        moveWindow(packet.seqNumber);
                        synchronized (sendBuffer) {
                            sendBuffer.decrementPacketsToAckNumber();
                            if (sendBuffer.getPacketsToAckNumber() == 0) {
                                sendBuffer.notify();
                            }
                        }
//                    } //else if (packet.isAck && packet.ackNumber != seqNumber) {

                    } else {
                        synchronized (receiveBuffer) {
                            ackNumber++;
                            receiveBuffer.put(packet);
                            receiveBuffer.notify();
                            sendAck();
                        }
                    }
                } catch (IOException e) {
//                    e.printStackTrace();
                }
            }
        }
    }

    private Thread receiver;

    public void connect(InetAddress address, int port) {
        makeBuffers();
        socket.connect(address, port);
        try {
            receiveHandshake();
            sendHandshakeAck();
            receiveHandshakeAck();
            ackNumber = 0;
            seqNumber = 0;
            receiver = new Thread(new Task());
            receiver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitThread() throws InterruptedException {
        synchronized (sendBuffer) {
            sendBuffer.wait();
        }
    }

    @Override
    public void close() {
        try {
            if (sendBuffer.getPacketsToAckNumber() > 0) {
                waitThread();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            receiver.interrupt();
            socket.close();
        }
    }
}
