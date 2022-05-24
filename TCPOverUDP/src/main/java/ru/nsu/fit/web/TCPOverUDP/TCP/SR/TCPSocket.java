package ru.nsu.fit.web.TCPOverUDP.TCP.SR;

import ru.nsu.fit.web.TCPOverUDP.TCP.packet.TCPPacket;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;

public abstract class TCPSocket implements Closeable {
    private final DatagramSocket socket;
    private final long timeoutInterval = 500;
    private final SendBuffer sendBuffer = new SendBuffer();
    private final ReceiveBuffer receiveBuffer = new ReceiveBuffer();
    private Integer seqNumber = 0;
    private Integer ackNumber = 0;
    private Integer receiveNumber = 0;
    private final Thread receiver = new Thread(new Task());
    private final Timer timer = new Timer();

    private class TCPTimerTask extends TimerTask {

        @Override
        public void run() {
            try {
                if (sendBuffer.getWaitForSend()[sendBuffer.getBase()])
                sendNow(sendBuffer.getPacket(sendBuffer.getBase()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Thread getReceiver() {
        return receiver;
    }

    protected DatagramSocket getSocket() {
        return socket;
    }

    protected void setSeq(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    protected void setAck(Integer ackNumber) {
        this.ackNumber = ackNumber;
    }

    protected Integer getSeq() {
        return seqNumber;
    }

    protected Integer getAck() {
        return ackNumber;
    }

    public TCPSocket(int port) throws SocketException {
        this.socket = new DatagramSocket(port);
    }

    protected void makeBuffers() {
        sendBuffer.init();
        receiveBuffer.init();
    }

    abstract public void connect(InetAddress address, int port);

    public byte[] receive() throws InterruptedException {
        synchronized (receiveBuffer) {
            if (receiveNumber >= receiveBuffer.getBase() - receiveBuffer.getWindowSize() && receiveNumber < receiveBuffer.getBase()) {
                return receiveBuffer.getPacket(receiveNumber++).buf;
            }
            receiveBuffer.wait();
            return receiveBuffer.getPacket(receiveNumber++).buf;
        }
    }

    private synchronized void sendNow(TCPPacket packetToSend) throws IOException {
        socket.send(packetToSend.makePacket());
    }

    public void send(byte[] buf) throws IOException {
        TCPPacket packetToSend = new TCPPacket();
        packetToSend.ackNumber = receiveBuffer.getBase();
        packetToSend.seqNumber = seqNumber;
        packetToSend.length = buf.length;
        packetToSend.buf = buf;
        sendBuffer.put(packetToSend);
        if (packetToSend.seqNumber < sendBuffer.getBase() + sendBuffer.getWindowSize()) {
            sendNow(packetToSend);
            sendBuffer.incrementPacketsToAckNumber();
//            System.err.println("Sent " + seqNumber);
            sendBuffer.getWaitForSend()[packetToSend.seqNumber] = false;
        } else {
            sendBuffer.getWaitForSend()[packetToSend.seqNumber] = true;
            sendBuffer.incrementPacketsWaitToSendNumber();
        }
        seqNumber++;
        try {
            timer.schedule(new TCPTimerTask(), timeoutInterval, timeoutInterval);
        } catch (IllegalStateException e) {

        }
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

    private TCPPacket receivePacket() throws IOException {
        int receivedLength = 1024 + 2 * Integer.BYTES + 1;
        DatagramPacket receivedPacket = new DatagramPacket(new byte[receivedLength], receivedLength);
        socket.receive(receivedPacket);
        TCPPacket packet = new TCPPacket();
        packet.extractPacket(receivedPacket);
        System.err.println("Received " + packet.seqNumber + " Is ack " + packet.isAck);
        return packet;
    }

    private void sendAck(int number) throws IOException {
        TCPPacket packetToSend = new TCPPacket();
        packetToSend.ackNumber = number;
        packetToSend.seqNumber = seqNumber;
        packetToSend.isAck = true;
        socket.send(packetToSend.makePacket());
//        System.err.println("Sent " + packetToSend.seqNumber);
    }

    private void sendNewPacketsFromBuffer() throws IOException {
        for (int i = sendBuffer.getBase(); i < sendBuffer.getWindowSize(); i++) {
            if (sendBuffer.getWaitForSend()[i]) {
                sendNow(sendBuffer.getBuffer()[i]);
                sendBuffer.decrementPacketsToAckNumber();
            } else {
                break;
            }
        }

    }

    public class Task implements Runnable {
        public Task() {
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    TCPPacket packet = receivePacket();
                    if (packet.isAck && packet.ackNumber > sendBuffer.getBase()) {
//                        expectedSeqNum++;
//                        ackNumber++;
                        sendBuffer.setBase(packet.ackNumber);
//                        sendBuffer.acknowledge(packet.seqNumber);
//                        moveWindow(packet.seqNumber);
                        sendNewPacketsFromBuffer();
                        synchronized (sendBuffer) {
                            sendBuffer.decrementPacketsToAckNumber();
                            if (sendBuffer.getPacketsToAckNumber() == 0 && sendBuffer.getPacketsWaitToSendNumber() == 0) {
                                sendBuffer.notify();
                            }
                        }
                    } else if (packet.isAck && sendBuffer.getDuplicateAckNumber(packet.ackNumber) == 3) {
                        sendNow(sendBuffer.getPacket(packet.ackNumber));
                        sendBuffer.incrementDuplicateAckNumber(packet.ackNumber);
                    } else if (packet.isAck && sendBuffer.getDuplicateAckNumber(packet.ackNumber) < 3) {
                        sendBuffer.incrementDuplicateAckNumber(packet.ackNumber);
                    } else if (packet.seqNumber >= receiveBuffer.getBase() && packet.seqNumber < sendBuffer.getBase() + sendBuffer.getWindowSize()) {
//                        expectedSeqNum++;
//                        ackNumber++;
                        synchronized (receiveBuffer) {
                            receiveBuffer.setBase(packet.seqNumber + 1);
                            receiveBuffer.put(packet);
                            receiveBuffer.notify();
                        }
                        sendAck(packet.seqNumber + 1);
                    } else if (packet.seqNumber >= receiveBuffer.getBase() - sendBuffer.getWindowSize() && packet.seqNumber < sendBuffer.getBase()) {
                        sendAck(sendBuffer.getBase());
                    }
                } catch (IOException e) {
                    receiver.interrupt();
//                    e.printStackTrace();
                }
            }
        }
    }

    private void waitReceiver() throws InterruptedException {
        synchronized (sendBuffer) {
            sendBuffer.wait();
        }
    }

    @Override
    public void close() {
        try {
            if (sendBuffer.getPacketsToAckNumber() > 0 || sendBuffer.getPacketsWaitToSendNumber() > 0) {
                waitReceiver();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            timer.cancel();
            receiver.interrupt();
            socket.close();
        }
    }

}
