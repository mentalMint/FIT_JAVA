package ru.nsu.fit.oop.chat.server;

import java.nio.channels.SocketChannel;

public class User {
    private final String name;
    private final SocketChannel socketChannel;

    public String getName() {
        return name;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public User(String name, SocketChannel socketChannel) {
        this.name = name;
        this.socketChannel = socketChannel;
    }
}
