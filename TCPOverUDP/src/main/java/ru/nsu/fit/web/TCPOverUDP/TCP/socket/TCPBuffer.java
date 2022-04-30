package ru.nsu.fit.web.TCPOverUDP.TCP.socket;

public class TCPBuffer {
    private byte[] buffer = new byte[1024];
    private int windowSize = 1024;
    private int base;

    public int getWindowSize() {
        return windowSize;
    }

    public int getBase() {
        return base;
    }


}
