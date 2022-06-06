package ru.nsu.fit.oop.chat.packets;

import java.io.Serial;

public class Request implements IPacket {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Type type;
    private final String body;

    public Request(Type type, String body) {
        this.type = type;
        this.body = body;
    }

    public enum Type {
        POST,
        REGISTER,
        GET_MEMBERS
    }

    public Type getType() {
        return type;
    }

    @Override
    public String getBody() {
        return body;
    }
}
