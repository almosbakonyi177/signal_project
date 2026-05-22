package dataAccess;

import com.alerts.AlertGenerator;
import com.alerts.alertStrategies.*;
import com.dataAccess.DataSourceAdapter;
import com.dataAccess.dataParsing.JSONDataParser;
import com.dataAccess.dataReading.DataReader;
import com.dataAccess.dataReading.WebsocketClient;
import com.data_management.DataStorage;
import com.patientIdentification.HospitalPatient;
import com.patientIdentification.IdentityManager;
import com.patientIdentification.MismatchHandler;
import com.patientIdentification.PatientIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Responsible only for Websocket API testing.
 */
public class WebsocketTest {

    /**
     * Need to clear storage before every test, otherwise if we add patient data
     * in one test, it will ruin the others and vice versa.
     */
    @BeforeEach
    void setUp() {
        DataStorage storage = DataStorage.getInstance();
        storage.clearStorage();
    }

    @Test
    void testWebSocketHappyPath() throws URISyntaxException {
        JSONDataParser parser = new JSONDataParser();
        DataStorage dataStorage = DataStorage.getInstance();
        IdentityManager identityManager =
                new IdentityManager(dataStorage,
                        new MismatchHandler(null));

        DataSourceAdapter adapter = new DataSourceAdapter(identityManager);

        URI path=new URI("ws://websocket:1");
        AlertGenerator alertGenerator=new AlertGenerator(DataStorage.getInstance());

        // Add the Strategies in runtime
        alertGenerator.addAlertStrategy(new BloodPressureStrategy());
        alertGenerator.addAlertStrategy(new OxygenSaturationStrategy());
        alertGenerator.addAlertStrategy(new ECGPeakStrategy());
        alertGenerator.addAlertStrategy(new HypotensiveHypoxemiaStrategy());
        alertGenerator.addAlertStrategy(new TriggeredAlertStrategy());

        DataReader reader = new WebsocketClient(parser, adapter, path, alertGenerator);

        String mockInput=
                "{\n" +
                        "\t\"patientId\":1,\n" +
                        "\t\"measurementValue\":100.0,\n" +
                        "\t\"recordType\":\"ECG\",\n" +
                        "\t\"timeStamp\":1000\n" +
                        "}";

        reader.onMessage(mockInput);
        assertEquals("ECG", dataStorage.getPatientById(1).
                getAllRecords().get(0).getRecordType());
    }

    @Test
    void testWebSocketBrokenMessage() throws URISyntaxException {
        JSONDataParser parser = new JSONDataParser();
        DataStorage dataStorage = DataStorage.getInstance();
        IdentityManager identityManager =
                new IdentityManager(dataStorage,
                        new MismatchHandler(null));

        DataSourceAdapter adapter = new DataSourceAdapter(identityManager);

        URI path=new URI("ws://websocket:1");
        AlertGenerator alertGenerator=new AlertGenerator(DataStorage.getInstance());

        // Add the Strategies in runtime
        alertGenerator.addAlertStrategy(new BloodPressureStrategy());
        alertGenerator.addAlertStrategy(new OxygenSaturationStrategy());
        alertGenerator.addAlertStrategy(new ECGPeakStrategy());
        alertGenerator.addAlertStrategy(new HypotensiveHypoxemiaStrategy());
        alertGenerator.addAlertStrategy(new TriggeredAlertStrategy());

        DataReader reader = new WebsocketClient(parser, adapter, path, alertGenerator);

        String mockInput =
                "{\n" +
                        "\t\"patientId\":2,\n" +
                        "\t\"measurementValue\":,\n" +
                        "\t\"recordType\":\"ECG\",\n" +
                        "}";

        reader.onMessage(mockInput);
        assertNull(dataStorage.getPatientById(1));
    }


    // Integration tests

    /**
     * This test checks if the system can integrate the incoming data into the system.
     * @throws URISyntaxException
     */
    @Test
    void testWebSocketDataIntegration() throws URISyntaxException {
        JSONDataParser parser = new JSONDataParser();
        DataStorage dataStorage = DataStorage.getInstance();
        IdentityManager identityManager = new IdentityManager(dataStorage,
                new MismatchHandler(new ArrayList<String>()));
        DataSourceAdapter adapter = new DataSourceAdapter(identityManager);
        URI path=new URI("ws://websocket:1");

        AlertGenerator alertGenerator=new AlertGenerator(DataStorage.getInstance());

        DataReader reader = new WebsocketClient(parser, adapter, path, alertGenerator);
        String mockInput=
                "{\n" +
                        "\t\"patientId\":1,\n" +
                        "\t\"measurementValue\":100.0,\n" +
                        "\t\"recordType\":\"ECG\",\n" +
                        "\t\"timeStamp\":1000\n" +
                        "}";
        reader.onMessage(mockInput);

        assertEquals("ECG", dataStorage.getPatientById(1).
                getAllRecords().get(0).getRecordType());
    }
}
