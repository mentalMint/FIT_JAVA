package ru.nsu.fit.oop.chat.packets;

import java.io.Serializable;

public interface IPacket extends Serializable {
    String getBody();
}
