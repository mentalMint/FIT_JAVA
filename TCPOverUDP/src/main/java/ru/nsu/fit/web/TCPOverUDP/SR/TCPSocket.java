package ru.nsu.fit.web.TCPOverUDP.SR;

import ru.nsu.fit.web.TCPOverUDP.TCP.socket.packet.TCPPacket;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public abstract class TCPSocket implements Closeable {
    private final DatagramSocket socket;
    private Integer seq = 0;
    private Integer ack = 0;
    private final long timeoutInterval = 500;
    private SendBuffer sendBuffer = null;
    private ReceiveBuffer receiveBuffer = null;


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

    protected void makeBuffers() {
        sendBuffer = new SendBuffer();
        receiveBuffer = new ReceiveBuffer();
    }

    abstract public void connect(InetAddress address, int port);

    abstract public byte[] receive();

    private void sendNow(TCPPacket packetToSend) throws IOException {
        socket.send(packetToSend.makePacket());
    }

    public void send(byte[] buf) {
        TCPPacket packetToSend = new TCPPacket();
        packetToSend.ack = ack;
        packetToSend.seq = seq;
        packetToSend.length = buf.length;
        packetToSend.buf = buf;
        sendBuffer.put(packetToSend);
        seq++;
    }

    private void sendAck() throws IOException {
        TCPPacket packetToSend = new TCPPacket();
        packetToSend.ack = ack;
        packetToSend.seq = seq;
        packetToSend.isAck = true;
        socket.send(packetToSend.makePacket());
        seq++;
    }

    private void receivePacket() throws IOException {
        int receivedLength = 2 * Integer.BYTES;
        DatagramPacket receivedPacket = new DatagramPacket(new byte[receivedLength], receivedLength);
        socket.receive(receivedPacket);
        TCPPacket packet = new TCPPacket();
        packet.extractPacket(receivedPacket);
        System.err.println("Seq: " + packet.seq + " Ack: " + packet.ack);
        ack++;
        receiveBuffer.put(packet);
    }

    @Override
    public void close() {
        socket.close();
    }

}
