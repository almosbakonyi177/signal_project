package com.dataAccess.dataListeners;

/**
 * Responsible for looking for data from the web socket, while
 * the listening is active.
 */
/*public class WebSocketDataListener implements DataListener {
    private DataParser dataParser;
    private DataSourceAdapter  dataSourceAdapter;
    WebsocketClient  websocketClient;


    public WebSocketDataListener (DataParser dataParser,
                                  DataSourceAdapter  dataSourceAdapter,
                                  WebsocketClient websocketClient) {

        this.dataParser = dataParser;
        this.dataSourceAdapter = dataSourceAdapter;
        this.websocketClient = websocketClient;
    }

    @Override
    public void startListening() {

    }


    /**
     * Stops this listener to check for from the web socket.
     */
    /*@Override
    public void stopListening() {

    }

    /**
     * Sends the raw message to the JSON parser.
     * @param message The message that will be sent to the parser.
     */
    /*public void onMessage (String message) {
        List<IncomingDataPoint> incomingDataPoints = dataParser.parse(message);

        for (IncomingDataPoint incomingDataPoint : incomingDataPoints) {
            dataSourceAdapter.integrateData(incomingDataPoint);
        }
    }
}

     */