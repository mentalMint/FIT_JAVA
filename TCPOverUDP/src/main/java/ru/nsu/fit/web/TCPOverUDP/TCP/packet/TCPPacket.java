package ru.nsu.fit.web.TCPOverUDP.TCP.packet;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

public class TCPPacket {
    public byte[] buf = {};
    private int maxLength = 1024;
    public int length = 0;
    public int ackNumber = 0;
    public int seqNumber = 0;
    public boolean isSyn = false;
    public boolean isFin = false;
    public boolean isAck = false;
    private final int headerLength = 2 * Integer.BYTES + 1;

    public int getMaxLength() {
        return maxLength;
    }

    public int getHeaderLength() {
        return headerLength;
    }

    private byte flagsToByte() {
        return (byte) ((isAck ? 0x1 : 0x0) | (isSyn ? 0x2 : 0x0) | (isFin ? 0x4 : 0x0));
    }

    public DatagramPacket makePacket() {
        byte[] byteSeq = ByteBuffer.allocate(Integer.BYTES).putInt(seqNumber).array();
        byte[] byteAck = ByteBuffer.allocate(Integer.BYTES).putInt(ackNumber).array();
        byte[] byteFlags = ByteBuffer.allocate(1).put(flagsToByte()).array();

        int newLength = length + headerLength;
        byte[] newBuf = new byte[newLength];
        System.arraycopy(byteSeq, 0, newBuf, 0, Integer.BYTES);
        System.arraycopy(byteAck, 0, newBuf, Integer.BYTES, Integer.BYTES);
        System.arraycopy(byteFlags, 0, newBuf, Integer.BYTES * 2, 1);

        System.arraycopy(buf, 0, newBuf, headerLength, length);
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
        byte[] flagBytes = new byte[1];

        buf = new byte[realLength];
        System.arraycopy(receivedBuf, 0, byteSeq, 0, Integer.BYTES);
        System.arraycopy(receivedBuf, Integer.BYTES, byteAck, 0, Integer.BYTES);
        System.arraycopy(receivedBuf, Integer.BYTES * 2, flagBytes, 0, 1);

        System.arraycopy(receivedBuf, Integer.BYTES * 2 + 1, buf, 0, realLength);
        ackNumber = bytesToInt(byteAck);
        seqNumber = bytesToInt(byteSeq);
        isAck = (flagBytes[0] & 1) == 1;
        isSyn= (flagBytes[0] & 2) == 2;
        isFin = (flagBytes[0] & 4) == 4;
    }
}
