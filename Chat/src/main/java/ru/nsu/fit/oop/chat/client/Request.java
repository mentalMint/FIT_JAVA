package ru.nsu.fit.oop.chat.client;

import java.io.Serializable;

public class Request implements Serializable {
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
}
