package com.dataAccess;

import com.patientIdentification.IncomingDataPoint;

import java.util.List;

/**
 * Responsible for looking for data from the web socket, while
 * the listening is active.
 */
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
     * @param message The message that will be sent to the parser.
     */
    public void onMessage (String message) {
        List<IncomingDataPoint> incomingDataPoints = jsonDataParser.parse(message);

        for (IncomingDataPoint incomingDataPoint : incomingDataPoints) {
            dataSourceAdapter.integrateData(incomingDataPoint);
        }
    }
}
