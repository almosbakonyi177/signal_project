package com.dataAccess.dataListeners;

import com.dataAccess.DataSourceAdapter;
import com.dataAccess.dataParsing.JSONDataParser;

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
}
