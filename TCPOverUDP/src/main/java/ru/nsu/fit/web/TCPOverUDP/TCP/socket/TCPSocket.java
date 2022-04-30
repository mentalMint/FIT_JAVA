package ru.nsu.fit.web.TCPOverUDP.TCP.socket;

import java.io.Closeable;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public abstract class TCPSocket implements Closeable {
    protected final DatagramSocket socket;
    protected Integer seq = 0;
    protected Integer ack = 0;


    public TCPSocket(int port) throws SocketException {
        this.socket = new DatagramSocket(port);
    }

    abstract public void connect(InetAddress address, int port);

    private int bytesToInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.put(bytes);
        buffer.rewind();
        return buffer.getInt();
    }

    public void receive(DatagramPacket packet) throws IOException {
        int receivedLength = packet.getLength() + 2 * Integer.BYTES;
        DatagramPacket receivedPacket = new DatagramPacket(new byte[receivedLength], receivedLength);
        socket.receive(receivedPacket);
        receivedLength = receivedPacket.getLength();
        int length = receivedLength - 2 * Integer.BYTES;
        byte[] receivedBuf = receivedPacket.getData();
        byte[] byteSeq = new byte[Integer.BYTES];
        byte[] byteAck = new byte[Integer.BYTES];
        byte[] buf = new byte[length];
        System.arraycopy(receivedBuf, 0, byteSeq, 0, Integer.BYTES);
        System.arraycopy(receivedBuf, Integer.BYTES, byteAck, 0, Integer.BYTES);
        System.arraycopy(receivedBuf, Integer.BYTES * 2, buf, 0, length);
        int receivedSeq = bytesToInt(byteSeq);
        int receivedAck = bytesToInt(byteAck);
        System.err.println("Seq: " + receivedSeq + " Ack: " + receivedAck);
        packet.setData(buf);
        packet.setLength(length);
        ack++;
    }

    public void sendNow(DatagramPacket packet) throws IOException {
        byte[] byteSeq = ByteBuffer.allocate(Integer.BYTES).putInt(seq).array();
        byte[] byteAck = ByteBuffer.allocate(Integer.BYTES).putInt(ack).array();
        int length = packet.getLength() + 2 * Integer.BYTES;
        byte[] buf = new byte[length];
        System.arraycopy(byteSeq, 0, buf, 0, Integer.BYTES);
        System.arraycopy(byteAck, 0, buf, Integer.BYTES, Integer.BYTES);
        System.arraycopy(packet.getData(), 0, buf, 2 * Integer.BYTES, length - 2 * Integer.BYTES);
        socket.send(new DatagramPacket(buf, length));
        seq++;
    }

    public void send(byte[] message) {

    }

    @Override
    public void close() {
        socket.close();
    }
}
