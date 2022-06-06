package ru.nsu.fit.oop.chat.client.model;

import ru.nsu.fit.oop.chat.client.Request;
import ru.nsu.fit.oop.chat.observer.Observable;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class Model extends Observable {
//    private Socket client;
    private SocketChannel client;
    private ByteBuffer buffer = ByteBuffer.allocate(300);
    private String message;

    public String getMessage() {
        return message;
    }

    private final Thread receiver = new Thread() {
        @Override
        public void run() {
            super.run();
//            try {
//                Selector selector = Selector.open();
//                client.register(selector, SelectionKey.OP_ACCEPT);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            try {
                while (true) {
                    message = receiveMessage();
                    notifyObservers();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    };

    private String receiveMessage() throws IOException, ClassNotFoundException {
//        Request request = (Request) inputStream.readObject();
//        inputStream.close();

        System.err.println("Read count: " + client.read(buffer));
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer.array());
        ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);
        Request request = (Request) inputStream.readObject();
        inputStream.close();
        buffer.clear();
        System.err.println(request.getType());
        System.err.println(request.getBody() + "\n");
        return request.getBody();
    }

    public void stop() throws IOException {
        receiver.interrupt();
        if (client != null) {
            client.close();
        }
        buffer = null;
    }

    public Model() {
        try {
//            client = new Socket();
            client = SocketChannel.open();
            client.configureBlocking(true);
//            client.bind(new InetSocketAddress(0));
            client.connect(new InetSocketAddress("localhost", 5454));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendMessage(String message) throws IOException {
        Request request = new Request(Request.Type.POST, message);
        sendRequest(request);
    }


    public void start() throws IOException {
        sendRequest(new Request(Request.Type.REGISTER, "Boba"));
        receiver.start();
    }

    private void sendRegisterRequest() {

    }

    private void sendRequest(Request request) throws IOException {
//        ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
//        outputStream.writeObject(request);
//        outputStream.close();
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
//        outputStream.writeObject(request);
//        outputStream.close();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
        outputStream.writeObject(request);
        outputStream.close();
        System.err.println(Arrays.toString(byteArrayOutputStream.toByteArray()));
        buffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        try {
            client.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer.clear();
    }
}
