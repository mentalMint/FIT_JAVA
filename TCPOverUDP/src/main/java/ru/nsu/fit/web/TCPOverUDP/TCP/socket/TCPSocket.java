package ru.nsu.fit.web.TCPOverUDP.TCP.socket;

import ru.nsu.fit.web.TCPOverUDP.TCP.socket.packet.TCPPacket;

import java.io.Closeable;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public abstract class TCPSocket implements Closeable {
    private final DatagramSocket socket;
    private Integer seq = 0;
    private Integer ack = 0;

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
        System.err.println("Seq: " + packet.seq + " Ack: " + packet.ack);
        ack++;
        return packet.buf;
    }

    public void sendNow(byte[] buf) throws IOException {
        TCPPacket packet = new TCPPacket();
        packet.ack = ack;
        packet.seq = seq;
        packet.length = buf.length;
        packet.buf = buf;
        socket.send(packet.makePacket());
        seq++;
    }

    @Override
    public void close() {
        socket.close();
    }

}
