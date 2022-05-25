package ru.nsu.fit.web.TCPOverUDP.TCP.sockets;

import ru.nsu.fit.web.TCPOverUDP.TCP.buffers.ReceiveBuffer;
import ru.nsu.fit.web.TCPOverUDP.TCP.buffers.SendBuffer;
import ru.nsu.fit.web.TCPOverUDP.TCP.packet.TCPPacket;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;

public abstract class TCPSocket implements Closeable {
    private final DatagramSocket socket;
    private final long timeoutInterval = 500;
    private final SendBuffer sendBuffer = new SendBuffer();
    private final ReceiveBuffer receiveBuffer = new ReceiveBuffer();
    private Integer seqNumber = 0;
    private final Thread receiver = new TCPThread(new Task());
    private final Timer timer = new Timer();
    private final LinkedList<TCPPacket> receiveQueue = new LinkedList<>();
    private Boolean isFinReceived = false;
    private final Object mutex = new Object();
    private final Object mutex2 = new Object();
    private final Object mutex3 = new Object();

    private class TCPTimerTask extends TimerTask {

        @Override
        public void run() {
            try {
                if (!sendBuffer.isPacketAcknowledged(sendBuffer.getBase()) && sendBuffer.getPacket(sendBuffer.getBase()) != null) {
                    sendNow(sendBuffer.getPacket(sendBuffer.getBase()));
                }
            } catch (IOException e) {
                timer.cancel();
                e.printStackTrace();
            }
        }
    }

    protected Boolean getIsFinReceived() {
        return isFinReceived;
    }

    protected Timer getTimer() {
        return timer;
    }

    protected Object getMutex() {
        return mutex;
    }

    protected Object getMutex2() {
        return mutex2;
    }
    protected Object getMutex3() {
        return mutex3;
    }
    protected SendBuffer getSendBuffer() {
        return sendBuffer;
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

    protected Integer getSeq() {
        return seqNumber;
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
        TCPPacket packet = new TCPPacket();
        int receivedLength = packet.getMaxLength() + packet.getHeaderLength();
        DatagramPacket receivedPacket = new DatagramPacket(new byte[receivedLength], receivedLength);
        socket.receive(receivedPacket);
        packet.extractPacket(receivedPacket);
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

    protected void sendFin() throws IOException, InterruptedException {
        TCPPacket packetToSend = new TCPPacket();
        packetToSend.ackNumber = receiveBuffer.getBase();
        packetToSend.seqNumber = seqNumber;
        packetToSend.isFin = true;
        synchronized (sendBuffer) {
            if (packetToSend.seqNumber < sendBuffer.getBase() + sendBuffer.getWindowSize()) {
                sendBuffer.put(packetToSend);
                sendNow(packetToSend);
                sendBuffer.incrementPacketsToAckNumber();
                sendBuffer.setIsPacketWaitingForSend(packetToSend.seqNumber, false);
            } else if (packetToSend.seqNumber >= sendBuffer.getBase() + sendBuffer.getWindowSize() &&
                    packetToSend.seqNumber < sendBuffer.getBase() + 2 * sendBuffer.getWindowSize()) {
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

    protected void sendFinAck() throws IOException, InterruptedException {
        TCPPacket packetToSend = new TCPPacket();
        packetToSend.ackNumber = receiveBuffer.getBase();
        packetToSend.seqNumber = seqNumber;
        packetToSend.isFin = true;
        packetToSend.isAck = true;
        synchronized (sendBuffer) {
            if (packetToSend.seqNumber < sendBuffer.getBase() + sendBuffer.getWindowSize()) {
                sendBuffer.put(packetToSend);
                sendNow(packetToSend);
                sendBuffer.incrementPacketsToAckNumber();
                sendBuffer.setIsPacketWaitingForSend(packetToSend.seqNumber, false);
            } else if (packetToSend.seqNumber >= sendBuffer.getBase() + sendBuffer.getWindowSize() &&
                    packetToSend.seqNumber < sendBuffer.getBase() + 2 * sendBuffer.getWindowSize()) {
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

    private void pushBufferedPackets() throws IOException {
        int i;
        for (i = receiveBuffer.getBase(); i < receiveBuffer.getBase() + receiveBuffer.getWindowSize(); i++) {
            if (receiveBuffer.isPacketReceived(i)) {
                receiveBuffer.setIsPacketReceived(i, false);
                if (receiveBuffer.getPacket(i).isFin) {
                    if (!receiveBuffer.getPacket(i).isAck) {
                        synchronized (mutex) {
                            isFinReceived = true;
                            mutex.notify();
                        }
                    }
                } else {
                    receiveQueue.offer(receiveBuffer.getPacket(i));
                }
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

    public class TCPThread extends Thread {
        @Override
        public void interrupt() {
//            System.err.println("Thread finishes work");
            if (finTimer != null) {
                finTimer.cancel();
            }
            super.interrupt();
            synchronized (mutex3) {
                isFinReceived = true;
                mutex3.notify();
            }
        }

        public TCPThread() {
        }

        public TCPThread(Runnable target) {
            super(target);
        }
    }

    Timer finTimer;

    public class CloseTimerTask extends TimerTask {

        @Override
        public void run() {
            receiver.interrupt();
        }
    }

    public class Task implements Runnable {
        public Task() {
        }
        @Override
        public void run() {
            try {
                while (true) {
                    TCPPacket packet = receivePacket();
                    if (!isLost()) {
                        if (packet.isAck) {
                            System.err.println("Ack " + packet.ackNumber + " is received");
                        } else {
                            System.err.println("Seq " + packet.seqNumber + " is received");
                        }

                        if (packet.isAck && packet.isFin) {
                            System.err.println("Fin " + packet.seqNumber + " is received");
//                            receiveBuffer.put(packet);
//                            receiveBuffer.setIsPacketReceived(packet.seqNumber, true);
                            synchronized (receiveQueue) {
                                if (packet.seqNumber == receiveBuffer.getBase()) {
                                    receiveQueue.notify();
                                }
                                pushBufferedPackets();
                            }

                            while (true) {
                                sendAck(packet.seqNumber + 1);
                                finTimer = new Timer();
                                finTimer.schedule(new CloseTimerTask(), 1000, 1000);
                                TCPPacket finAckPacket = receivePacket();
                                finTimer.cancel();
                            }

//                            break;
                        } else if (packet.isAck && packet.ackNumber > sendBuffer.getBase()) {
                            synchronized (sendBuffer) {
                                sendBuffer.notify();
                                for (int i = sendBuffer.getBase(); i < packet.ackNumber; i++) {
                                    sendBuffer.setPacket(i, null);
                                    sendBuffer.setDuplicateAckNumber(i, 0);
                                    sendBuffer.decrementPacketsToAckNumber();
                                }
                                sendBuffer.setBase(packet.ackNumber);

                                synchronized (mutex2) {
                                    sendNewPacketsFromBuffer();
                                    if (sendBuffer.getPacketsToAckNumber() == 0 && sendBuffer.getPacketsWaitToSendNumber() == 0) {
                                        mutex2.notify();
                                    }
                                }
                            }
                        } else if (packet.isAck && sendBuffer.getDuplicateAckNumber(packet.ackNumber) == 3) {
                            sendNow(sendBuffer.getPacket(packet.ackNumber));
                            sendBuffer.incrementDuplicateAckNumber(packet.ackNumber);
                        } else if (packet.isAck && sendBuffer.getDuplicateAckNumber(packet.ackNumber) < 3) {
                            sendBuffer.incrementDuplicateAckNumber(packet.ackNumber);
                        } else if (packet.seqNumber >= receiveBuffer.getBase() &&
                                packet.seqNumber < receiveBuffer.getBase() + receiveBuffer.getWindowSize()) {
                            receiveBuffer.put(packet);
                            receiveBuffer.setIsPacketReceived(packet.seqNumber, true);
                            synchronized (receiveQueue) {
                                if (packet.seqNumber == receiveBuffer.getBase()) {
                                    receiveQueue.notify();
                                }
                                pushBufferedPackets();
                                sendAck(receiveBuffer.getBase());
                            }
                        } else if (packet.seqNumber >= receiveBuffer.getBase() - receiveBuffer.getWindowSize() &&
                                packet.seqNumber < receiveBuffer.getBase()) {
                            sendAck(packet.seqNumber + 1);
                        }
                    } else {
                        if (packet.isAck) {
                            System.err.println("Ack " + packet.ackNumber + " is lost");
                        } else {
                            System.err.println("Seq " + packet.seqNumber + " is lost");
                        }
                    }
                }
            }
            catch (IOException e) {
//                System.err.println("Thread finishes work");
//                synchronized (mutex3) {
//                    isFinReceived = true;
//                    mutex3.notify();
//                }
            }
        }
    }
}
