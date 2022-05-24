package ru.nsu.fit.web.TCPOverUDP.TCP.SR;

import ru.nsu.fit.web.TCPOverUDP.TCP.packet.TCPPacket;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.SynchronousQueue;

public abstract class TCPSocket implements Closeable {
    private final DatagramSocket socket;
    private final long timeoutInterval = 500;
    private final SendBuffer sendBuffer = new SendBuffer();
    private final ReceiveBuffer receiveBuffer = new ReceiveBuffer();
    private Integer seqNumber = 0;
    private Integer ackNumber = 0;
    private final Thread receiver = new Thread(new Task());
    private final Timer timer = new Timer();
    private final LinkedList<TCPPacket> receiveQueue = new LinkedList<>();

    private class TCPTimerTask extends TimerTask {

        @Override
        public void run() {
            try {
                if (!sendBuffer.isPacketAcknowledged(sendBuffer.getBase()) && sendBuffer.getPacket(sendBuffer.getBase()) != null) {
//                    System.err.println(sendBuffer.getBase());
                    sendNow(sendBuffer.getPacket(sendBuffer.getBase()));
                }
            } catch (IOException e) {
                timer.cancel();
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
        synchronized (receiveQueue) {
            if (receiveQueue.isEmpty()) {
                receiveQueue.wait();
            }
//            assert receiveQueue.peek() != null;
            return Objects.requireNonNull(receiveQueue.poll()).buf;
        }
    }

    private synchronized void sendNow(TCPPacket packetToSend) throws IOException {
        socket.send(packetToSend.makePacket());
        System.err.println("Send " + packetToSend.seqNumber);
    }

    public void send(byte[] buf) throws IOException, InterruptedException {
        TCPPacket packetToSend = new TCPPacket();
        packetToSend.ackNumber = receiveBuffer.getBase();
        packetToSend.seqNumber = seqNumber;
        packetToSend.length = buf.length;
        packetToSend.buf = buf;
        synchronized (sendBuffer) {
            if (packetToSend.seqNumber < sendBuffer.getBase() + sendBuffer.getWindowSize()) {
                sendBuffer.put(packetToSend);
                sendNow(packetToSend);
                sendBuffer.incrementPacketsToAckNumber();
//            System.err.println("Sent " + seqNumber);
                sendBuffer.setIsPacketWaitingForSend(packetToSend.seqNumber, false);
            } else if (packetToSend.seqNumber >= sendBuffer.getBase() + sendBuffer.getWindowSize() && packetToSend.seqNumber < sendBuffer.getBase() + 2 * sendBuffer.getWindowSize()) {
                sendBuffer.put(packetToSend);
                sendBuffer.setIsPacketWaitingForSend(packetToSend.seqNumber, true);
                sendBuffer.incrementPacketsWaitToSendNumber();
            } else {
                sendBuffer.wait();
                sendBuffer.put(packetToSend);
                sendBuffer.setIsPacketWaitingForSend(packetToSend.seqNumber, true);
                sendBuffer.incrementPacketsWaitToSendNumber();
            }
        }
        seqNumber++;
        try {
            timer.schedule(new TCPTimerTask(), timeoutInterval, timeoutInterval);
        } catch (IllegalStateException e) {

        }
    }

    private TCPPacket receivePacket() throws IOException {
        int receivedLength = 1024 + 2 * Integer.BYTES + 1;
        DatagramPacket receivedPacket = new DatagramPacket(new byte[receivedLength], receivedLength);
        socket.receive(receivedPacket);
        TCPPacket packet = new TCPPacket();
        packet.extractPacket(receivedPacket);
//        System.err.println("Received " + packet.seqNumber + " Is ack " + packet.isAck);
        return packet;
    }

    private void sendAck(int number) throws IOException {
        TCPPacket packetToSend = new TCPPacket();
        packetToSend.ackNumber = number;
        packetToSend.seqNumber = seqNumber;
        packetToSend.isAck = true;
        socket.send(packetToSend.makePacket());
        System.err.println("Send ack " + number);
    }

    private void sendNewPacketsFromBuffer() throws IOException {
        for (int i = sendBuffer.getBase(); i < sendBuffer.getBase() + sendBuffer.getWindowSize(); i++) {
            if (sendBuffer.isPacketWaitingForSend(i)) {
                sendNow(sendBuffer.getPacket(i));
                sendBuffer.incrementPacketsToAckNumber();
                sendBuffer.decrementPacketsWaitToSendNumber();
                sendBuffer.setIsPacketWaitingForSend(i, false);
            } else {
                break;
            }
        }
    }

    private void pushBufferedPackets() throws InterruptedException {
        int i;
        for (i = receiveBuffer.getBase(); i < receiveBuffer.getBase() + receiveBuffer.getWindowSize(); i++) {
            if (receiveBuffer.isPacketReceived(i)) {
//                synchronized (receiveQueue) {
//                System.err.println(i);
                receiveQueue.offer(receiveBuffer.getPacket(i));
//                }
                receiveBuffer.setIsPacketReceived(i, false);
            } else {
                break;
            }
        }
        receiveBuffer.setBase(i);
    }

    private boolean isLost() {
        double x = Math.random() * 10;
        return x > 8;
    }

    public class Task implements Runnable {
        public Task() {
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    TCPPacket packet = receivePacket();
                    if (!isLost()) {
                        if (packet.isAck) {
                            System.err.println("Ack " + packet.ackNumber + " is received");
                        } else {
                            System.err.println("Seq " + packet.seqNumber + " is received");
                        }

                        if (packet.isAck && packet.ackNumber > sendBuffer.getBase()) {
                            synchronized (sendBuffer) {
                                for (int i = sendBuffer.getBase(); i < packet.ackNumber; i++) {
                                    sendBuffer.setPacket(i, null);
                                }
                                sendBuffer.setBase(packet.ackNumber);
                                sendNewPacketsFromBuffer();
                                sendBuffer.decrementPacketsToAckNumber();
//                            if (sendBuffer.getPacketsToAckNumber() == 0 && sendBuffer.getPacketsWaitToSendNumber() == 0) {
                                sendBuffer.notify();
//                            }
                            }
                        } else if (packet.isAck && sendBuffer.getDuplicateAckNumber(packet.ackNumber) == 3) {
                            sendNow(sendBuffer.getPacket(packet.ackNumber));
                            sendBuffer.incrementDuplicateAckNumber(packet.ackNumber);
                        } else if (packet.isAck && sendBuffer.getDuplicateAckNumber(packet.ackNumber) < 3) {
                            sendBuffer.incrementDuplicateAckNumber(packet.ackNumber);
                        } else if (packet.seqNumber >= receiveBuffer.getBase() && packet.seqNumber < receiveBuffer.getBase() + receiveBuffer.getWindowSize()) {
                            receiveBuffer.put(packet);
                            receiveBuffer.setIsPacketReceived(packet.seqNumber, true);
                            synchronized (receiveQueue) {
                                if (packet.seqNumber == receiveBuffer.getBase()) {
                                    receiveQueue.notify();
                                }
                                pushBufferedPackets();
                            }
                            sendAck(receiveBuffer.getBase());
                        } else if (packet.seqNumber >= receiveBuffer.getBase() - receiveBuffer.getWindowSize() && packet.seqNumber < receiveBuffer.getBase()) {
                            sendAck(packet.seqNumber + 1);
                        }
                    } else {
                        if (packet.isAck) {
                            System.err.println("Ack " + packet.ackNumber + " is lost");
                        } else {
                            System.err.println("Seq " + packet.seqNumber + " is lost");
                        }
                    }
                } catch (IOException e) {
                    synchronized (receiver) {
                        Thread.currentThread().notifyAll();
                        receiver.interrupt();
                    }
//                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
//                waitReceiver();
                synchronized (receiver) {
                    receiver.wait();
                }
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
