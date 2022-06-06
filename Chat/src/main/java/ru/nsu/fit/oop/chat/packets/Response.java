package ru.nsu.fit.oop.chat.packets;

import java.io.Serial;

public class Response implements IPacket {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Type type;
    private final String body;
    private final String initiatorName;

    public Response(Type type, String body, String initiatorName) {
        this.type = type;
        this.body = body;
        this.initiatorName = initiatorName;
    }

    public enum Type {
        MESSAGE,
        MEMBERS,
        USER_CONNECTED,
        USER_DISCONNECTED
    }

    public String getInitiatorName() {
        return initiatorName;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String getBody() {
        return body;
    }
}
