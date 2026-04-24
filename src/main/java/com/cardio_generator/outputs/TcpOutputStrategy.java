package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * Represents a server which can be set up on a chosen port.
 * To this server we can upload outputs, which contain patient information.
 *
 * @author Almos Bakonyi
 */
public class TcpOutputStrategy implements OutputStrategy {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;

    /**
     * This function is the constructor of this class.
     * It sets up the server on a chosen port.
     * @param port Integer of the chosen port number that we want to set up the server on.
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The function takes patientID, timestamp, a label and a string data as input,
     * and uploads these parameters to the server divided by commas. For example,
     * it can be helpful
     * for alert generators. For example: call the function output(3, 0.1, "Message", "done"),
     * the function will upload "3,0.1,Message,done" to the server.
     * @param patientId Integer of the patient ID.
     * @param timestamp long value of the timestamp.
     * @param label String of alert/variable type.
     * @param data String of status/value.
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
