package ru.nsu.fit.web.TCPOverUDP.TCP;

import ru.nsu.fit.web.TCPOverUDP.TCP.packet.TCPPacket;

import java.io.Closeable;
import java.io.IOException;
import java.net.*;

public abstract class TCPSocket implements Closeable {
    private final DatagramSocket socket;
    private Integer seq = 0;
    private Integer ack = 0;
    private long timeoutInterval = 500;

    public DatagramSocket getSocket() {
        return socket;
    }

    protected void setSeq(Integer seq) {
        this.seq = seq;
    }

    protected void setAck(Integer ack) {
        this.ack = ack;
    }

    protected Integer getSeq() {
        return seq;
    }

    protected Integer getAck() {
        return ack;
    }

    public TCPSocket(int port) throws SocketException {
        this.socket = new DatagramSocket(port);
    }

    abstract public void connect(InetAddress address, int port);

    public byte[] receive(int length) throws IOException {
        int receivedLength = length + 2 * Integer.BYTES;
        DatagramPacket receivedPacket = new DatagramPacket(new byte[receivedLength], receivedLength);
        socket.receive(receivedPacket);
        TCPPacket packet = new TCPPacket();
        packet.extractPacket(receivedPacket);
        System.err.println("Seq: " + packet.seqNumber + " Ack: " + packet.ackNumber);
        ack++;
        sendAck();
        return packet.buf;
    }

    public void send(byte[] buf) throws IOException {
        TCPPacket packetToSend = new TCPPacket();
        packetToSend.ackNumber = ack;
        packetToSend.seqNumber = seq;
        packetToSend.length = buf.length;
        packetToSend.buf = buf;
        socket.send(packetToSend.makePacket());

        long start = System.currentTimeMillis();
        TCPPacket receivedPacket = receivePacket();
        while (!receivedPacket.isAck && receivedPacket.ackNumber == seq - 1) {
            if (System.currentTimeMillis() - start > timeoutInterval) {
                socket.send(packetToSend.makePacket());
            }
            receivedPacket = receivePacket();
        }
        seq++;
    }

    private void sendAck() throws IOException {
        TCPPacket packetToSend = new TCPPacket();
        packetToSend.ackNumber = ack;
        packetToSend.seqNumber = seq;
        packetToSend.isAck = true;
        socket.send(packetToSend.makePacket());
        seq++;
    }

    private TCPPacket receivePacket() throws IOException {
        int receivedLength = 2 * Integer.BYTES;
        DatagramPacket receivedPacket = new DatagramPacket(new byte[receivedLength], receivedLength);
        socket.receive(receivedPacket);
        TCPPacket packet = new TCPPacket();
        packet.extractPacket(receivedPacket);
        System.err.println("Seq: " + packet.seqNumber + " Ack: " + packet.ackNumber);
        ack++;
        return packet;
    }

    @Override
    public void close() {
        socket.close();
    }

}
