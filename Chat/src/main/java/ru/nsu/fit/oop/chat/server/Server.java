package ru.nsu.fit.oop.chat.server;

import ru.nsu.fit.oop.chat.packets.ObjectByteArrayConvertor;
import ru.nsu.fit.oop.chat.packets.Request;
import ru.nsu.fit.oop.chat.packets.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class Server {
    private final HashMap<SocketChannel, User> users = new HashMap<>();
    private final ArrayList<Response> lastMessages = new ArrayList<>();

    private void register(Selector selector, ServerSocketChannel serverSocket)
            throws IOException {
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

    private void broadCast(ByteBuffer buffer, Selector selector) {
        selector.keys().forEach(selectionKey -> {
            if (!selectionKey.isAcceptable()) {
                try {
                    SocketChannel clientReceiver = (SocketChannel) selectionKey.channel();
                    clientReceiver.write(buffer);
                    buffer.rewind();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void start() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("localhost", 5454));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true) {
            try {
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                try {
                    if (key.isAcceptable()) {
                        register(selector, serverSocket);
                    }

                    if (key.isReadable()) {
                        SocketChannel clientSender = (SocketChannel) key.channel();
                        clientSender.read(buffer);
                        buffer.flip();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);
                        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
                        Request request = (Request) inputStream.readObject();
                        inputStream.close();
//                        System.err.println("Request body: " + request.getBody() + "\n");
                        buffer.clear();
                        System.err.println(request.getType());

                        switch (request.getType()) {
                            case REGISTER -> {
                                if (!users.containsKey(clientSender)) {
                                    User user = new User(request.getBody(), clientSender);
                                    users.put(clientSender, user);
                                    Response response = new Response(Response.Type.USER_CONNECTED, "", user.getName());
                                    byte[] byteArray = ObjectByteArrayConvertor.convertObjectToByteArray(response);
                                    buffer.put(byteArray);
                                    buffer.flip();
                                    buffer.clear();
                                    for (Response lastMessage : lastMessages) {
                                        byteArray = ObjectByteArrayConvertor.convertObjectToByteArray(lastMessage);
                                        buffer.put(byteArray);
                                        buffer.flip();
                                        clientSender.write(buffer);
                                        buffer.clear();
                                    }
                                    broadCast(buffer, selector);
                                }
                                System.err.println(lastMessages);
                            }
                            case POST -> {
                                Response response = new Response(Response.Type.MESSAGE, request.getBody(), users.get(clientSender).getName());
                                byte[] byteArray = ObjectByteArrayConvertor.convertObjectToByteArray(response);
                                buffer.put(byteArray);
                                buffer.flip();
                                broadCast(buffer, selector);
                                if (lastMessages.size() == 10) {
                                    lastMessages.remove(9);
                                }
                                lastMessages.add(response);
                                System.err.println(lastMessages);
                            }
                            case GET_MEMBERS -> {
                                StringBuilder userListBuilder = new StringBuilder();
                                users.forEach((socketChannel, user) -> userListBuilder.append(user.getName()).append("\n"));
                                Response response = new Response(Response.Type.MEMBERS, userListBuilder.toString(), users.get(clientSender).getName());
                                byte[] byteArray = ObjectByteArrayConvertor.convertObjectToByteArray(response);
                                buffer.put(byteArray);
                                buffer.flip();
                                clientSender.write(buffer);
                            }
                            case DISCONNECT -> {
                                User user = users.get(clientSender);
                                users.remove(clientSender);
                                Response response = new Response(Response.Type.USER_DISCONNECTED, "", user.getName());
                                byte[] byteArray = ObjectByteArrayConvertor.convertObjectToByteArray(response);
                                buffer.put(byteArray);
                                buffer.flip();
                                broadCast(buffer, selector);
                            }
                        }
                        buffer.clear();
                    }

                    iterator.remove();
                } catch (IOException e) {
                    e.printStackTrace();
                    key.cancel();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
