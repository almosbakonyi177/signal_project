package com.dataAccess.dataReading;

import org.java_websocket.client.WebSocketClient;

import java.io.IOException;
import java.net.URI;

public interface DataReader {
    void onOpen();
    void onMessage(String message);
    void connect();
}