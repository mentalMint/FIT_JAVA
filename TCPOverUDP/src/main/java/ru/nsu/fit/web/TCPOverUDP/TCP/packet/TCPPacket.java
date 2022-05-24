package ru.nsu.fit.web.TCPOverUDP.TCP.packet;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

public class TCPPacket {
    public byte[] buf = {};
    public int length = 0;
    public int ackNumber = 0;
    public int seqNumber = 0;
    public boolean isSyn = false;
    public boolean isAck = false;

    public DatagramPacket makePacket() {
        byte[] byteSeq = ByteBuffer.allocate(Integer.BYTES).putInt(seqNumber).array();
        byte[] byteAck = ByteBuffer.allocate(Integer.BYTES).putInt(ackNumber).array();
        byte[] byteIsAck = ByteBuffer.allocate(1).put(isAck ? (byte) 1 : 0).array();

        int newLength = length + 2 * Integer.BYTES + 1;
        byte[] newBuf = new byte[newLength];
        System.arraycopy(byteSeq, 0, newBuf, 0, Integer.BYTES);
        System.arraycopy(byteAck, 0, newBuf, Integer.BYTES, Integer.BYTES);
        System.arraycopy(byteIsAck, 0, newBuf, Integer.BYTES * 2, 1);

        System.arraycopy(buf, 0, newBuf, 2 * Integer.BYTES + 1, length);
        return new DatagramPacket(newBuf, newLength);
    }

    private int bytesToInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.put(bytes);
        buffer.rewind();
        return buffer.getInt();
    }

    public void extractPacket(DatagramPacket receivedPacket) {
        int realLength = receivedPacket.getLength() - 2 * Integer.BYTES - 1;
        byte[] receivedBuf = receivedPacket.getData();
        byte[] byteSeq = new byte[Integer.BYTES];
        byte[] byteAck = new byte[Integer.BYTES];
        byte[] byteIsAck = new byte[1];

        buf = new byte[realLength];
        System.arraycopy(receivedBuf, 0, byteSeq, 0, Integer.BYTES);
        System.arraycopy(receivedBuf, Integer.BYTES, byteAck, 0, Integer.BYTES);
        System.arraycopy(receivedBuf, Integer.BYTES * 2, byteIsAck, 0, 1);

        System.arraycopy(receivedBuf, Integer.BYTES * 2 + 1, buf, 0, realLength);
        ackNumber = bytesToInt(byteAck);
        seqNumber = bytesToInt(byteSeq);
        isAck = byteIsAck[0] == 1;
    }
}
