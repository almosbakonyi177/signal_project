package com.dataAccess.dataReading;
import com.alerts.AlertGenerator;
import com.alerts.alertStrategies.*;
import com.dataAccess.DataSourceAdapter;
import com.dataAccess.dataParsing.DataParser;
import com.data_management.DataStorage;
import com.patientIdentification.IncomingDataPoint;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates and manages Websocket connection to receive real time patient data.
 * Listens for incoming records, sends them to the chosen parser to normalize,
 * then using {@link DataSourceAdapter} integrates them to the storage.
 * Also calls for data evaluation using {@link AlertGenerator}.
 */
public class WebsocketClient extends WebSocketClient implements DataReader {
    private DataParser dataParser;
    private DataSourceAdapter dataSourceAdapter;
    private AlertGenerator alertGenerator;

    /**
     * Creates a new Websocket Client.
     * @param dataParser The parser responsible for normalizing the raw string data.
     * @param dataSourceAdapter The adapter used to integrate
     *                          normalized {@link IncomingDataPoint} into storage.
     * @param path The URI path of the target WebSocket server.
     * @param alertGenerator The alert generator used for incoming records evaluation.
     */
    public WebsocketClient(DataParser dataParser,
                           DataSourceAdapter dataSourceAdapter, URI path,
                           AlertGenerator alertGenerator) {
        super(URI.create(path.toString()));
        this.dataParser = dataParser;
        this.dataSourceAdapter = dataSourceAdapter;
        this.alertGenerator = alertGenerator;
    }

    public void connect(){
        super.connect();
    }


    /**
     * Triggered when a successful connection is established with the WebSocket server.
     * @param serverHandshake The details of the handshake established with the server.
     */
    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("Websocket connection opened"+serverHandshake.toString());
    }

    @Override
    public void onOpen() {
        System.out.println("Websocket connection opened");
    }

    /**
     * Processes incoming text messages from the WebSocket server, integrates the into storage.
     * @param message The raw text message received from the server.
     */
    public void onMessage(String message) {
        try {
            // Track which patients have incoming records
            List<Integer> incomingDataPatients=new ArrayList<>();

            List<IncomingDataPoint> data = dataParser.parse(message);
            for (IncomingDataPoint incomingDataPoint : data) {
                dataSourceAdapter.integrateData(incomingDataPoint);
                incomingDataPatients.add(incomingDataPoint.getPatientId());
            }
            DataStorage storage = DataStorage.getInstance();

            // Evaluate all incoming patients' data to check for conditions that may trigger alerts
            for (int i=0;i<incomingDataPatients.size();i++) {
                alertGenerator.evaluateData(storage.getPatientById(incomingDataPatients.get(i)));
            }
            incomingDataPatients.clear();
        }
        catch (Exception e) {
            System.out.println("Error occurred while processing data: " + e.getMessage());
        }
    }

    /**
     * Triggered when the websocket connection gets closed.
     * @param code The HTTP status code indicating the reason for closure.
     * @param reason A String explaining the disconnect.
     * @param remote True if the connection was closed by the remote host, otherwise false.
     */
    public void onClose(int  code, String reason, boolean remote) {
        System.out.println("Disconnected from websocket\nCode: " + code
        + " Reason: " + reason
        + " Remote: " + remote);
    }

    /**
     * Triggered when an error occurs in the WebSocket connection.
     * @param e The exception that was thrown during the error event.
     */
    public void onError(Exception e) {
        System.out.println("Error occurred while trying to connect to websocket:"+e.getMessage());
    }
}
