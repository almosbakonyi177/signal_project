package com.dataAccess;

import com.patientIdentification.IncomingDataPoint;

import java.util.List;

/**
 * Responsible for looking for data from TCP server
 * while the listening is active.
 */
public class TCPDataListener implements DataListener {
    private int port;
    private JSONDataParser jsonDataParser = new JSONDataParser();
    private DataSourceAdapter dataSourceAdapter;


    public TCPDataListener(int port, DataSourceAdapter dataSourceAdapter) {
        this.port = port;
        this.dataSourceAdapter = dataSourceAdapter;
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
