package ru.nsu.fit.oop.chat.client.model;

import ru.nsu.fit.oop.chat.packets.ObjectByteArrayConvertor;
import ru.nsu.fit.oop.chat.packets.Request;
import ru.nsu.fit.oop.chat.observer.Observable;
import ru.nsu.fit.oop.chat.packets.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class Model extends Observable {
    private SocketChannel client;
    private ByteBuffer buffer = ByteBuffer.allocate(300);
    private String message;
    private Response response;

    public Response getResponse() {
        return response;
    }

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
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    };

    private String receiveMessage() throws IOException, ClassNotFoundException {
        System.err.println("Read count: " + client.read(buffer));
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer.array());
        ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);
        response = (Response) inputStream.readObject();
        inputStream.close();
        buffer.clear();
        System.err.println(response.getType());
        System.err.println(response.getBody() + "\n");
        return response.getBody();
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
            client = SocketChannel.open();
            client.configureBlocking(true);
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

    private void sendRequest(Request request) throws IOException {
        byte[] byteArray = ObjectByteArrayConvertor.convertObjectToByteArray(request);
        System.err.println(Arrays.toString(byteArray));
        buffer.put(byteArray);
        buffer.flip();
        try {
            client.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer.clear();
    }
}
