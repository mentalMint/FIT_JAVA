package ru.nsu.fit.oop.chat.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ObjectByteArrayConvertor {
    public static byte[] convertObjectToByteArray(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
        outputStream.writeObject(object);
        outputStream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
