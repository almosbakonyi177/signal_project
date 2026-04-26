package com.dataAccess;

import com.patientIdentification.IncomingDataPoint;

import java.util.List;

//
public class TCPDataListener implements DataListener {
    private int port;
    private JSONDataParser jsonDataParser = new JSONDataParser();


    public TCPDataListener(int port) {
        this.port = port;
    }


    public void startListening() {

    }

    /**
     * Stops this listener to check for updates from Tcp server.
     */
    public void stopListening() {

    }

    public void readFromStream () {

    }
}
