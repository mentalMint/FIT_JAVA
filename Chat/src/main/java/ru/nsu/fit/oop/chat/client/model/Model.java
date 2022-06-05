package ru.nsu.fit.oop.chat.client.model;

import ru.nsu.fit.oop.chat.observer.Observable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Model extends Observable {
    private SocketChannel client;
    private ByteBuffer buffer = ByteBuffer.allocate(256);
    private String message;

    public String getMessage() {
        return message;
    }

    private final Thread receiver = new Thread() {
        @Override
        public void run() {
            super.run();
            try {
                while (true) {
                    message = receiveMessage();
                    notifyObservers();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public void stop() throws IOException {
        receiver.interrupt();
        if (client != null) {
            client.close();
        }
        buffer = null;
    }

    public Model() {
        try {
            client = SocketChannel.open(new InetSocketAddress("localhost", 5454));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendMessage(String msg) {
        try {
            client.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String receiveMessage() throws IOException {
        client.read(buffer);
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        String response = new String(bytes);
        System.out.println("response=" + response);
        buffer.clear();
        return response;
    }

    public void start() throws IOException {
        receiver.start();
    }
}
