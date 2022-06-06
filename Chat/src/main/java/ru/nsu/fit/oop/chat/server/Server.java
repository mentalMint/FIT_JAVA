package ru.nsu.fit.oop.chat.server;

import ru.nsu.fit.oop.chat.client.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class Server {
    //    private final ArrayList<User> users = new ArrayList<>();
    private final HashMap<SocketChannel, User> users = new HashMap<>();

    private void register(Selector selector, ServerSocketChannel serverSocket)
            throws IOException {
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

    public void start() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("localhost", 5454));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer buffer = ByteBuffer.allocate(300);

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
                        System.err.println("Read count: " + clientSender.read(buffer));
                        buffer.flip();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);
//                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
//                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer.array());
                        System.err.println(Arrays.toString(bytes));
                        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
                        System.err.println(2);
                        Request request = (Request) inputStream.readObject();
                        System.err.println(3);
//                        inputStream.close();
                        System.err.println("Request body: " + request.getBody() + "\n");
                        if (!users.containsKey(clientSender) && request.getType() == Request.Type.REGISTER) {
                            users.put(clientSender, new User(request.getBody(), clientSender));
                            buffer.clear();
                        } else if (users.containsKey(clientSender) && request.getType() == Request.Type.POST) {
                            buffer.flip();
                            selector.keys().forEach(selectionKey -> {
                                if (!selectionKey.isAcceptable()) {
                                    try {
                                        SocketChannel clientReceiver = (SocketChannel) selectionKey.channel();
                                        clientReceiver.write(buffer);
                                        buffer.rewind();
                                    } catch (IOException e) {
                                        try {
                                            clientSender.close();
                                            e.printStackTrace();
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                }
                            });
                            buffer.clear();
                        }
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
