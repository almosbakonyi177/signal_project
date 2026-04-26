package com.dataAccess;

import com.patientIdentification.IncomingDataPoint;

import java.util.List;

public class WebSocketDataListener implements DataListener {
    private JSONDataParser jsonDataParser;
    private DataSourceAdapter  dataSourceAdapter;

    public WebSocketDataListener (JSONDataParser jsonDataParser,
                                  DataSourceAdapter  dataSourceAdapter) {

        this.jsonDataParser = jsonDataParser;
        this.dataSourceAdapter = dataSourceAdapter;
    }

    @Override
    public void startListening() {

    }


    /**
     * Stops this listener to check for from the web socket.
     */
    @Override
    public void stopListening() {

    }

    /**
     * Sends the raw message to the JSON parser.
     * @param message The message that it sends to the parser.
     */
    public void onMessage (String message) {
        List<IncomingDataPoint> incomingDataPoints = jsonDataParser.parse(message);

        for (IncomingDataPoint incomingDataPoint : incomingDataPoints) {
            dataSourceAdapter.integrateData(incomingDataPoint);
        }
    }
}
