package dataAccess;

import com.dataAccess.DataSourceAdapter;
import com.dataAccess.ExcelDataParser;
import com.dataAccess.JSONDataParser;
import com.dataAccess.WebSocketDataListener;
import com.data_management.DataStorage;
import com.patientIdentification.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAccessTest {

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
    void excelParserTestWithHeader() {
        String mockCSVInput="PatientId,Measurement Value,Measurement Type,Time Stamp\n" +
                "1,100,Saturation,1000\n" +
                "1,90,Saturation,1000";

        ExcelDataParser parser = new ExcelDataParser();
        List< IncomingDataPoint> data = parser.parse(mockCSVInput);

        // Check if it read correctly and parsed the first line
        assertEquals(100, data.get(0).getMeasurementValue());
    }


    @Test
    void excelParserTestWithoutHeader() {
        String mockCSVInput=
                "1,100,Saturation,1000\n" +
                "1,90,Saturation,1000";

        ExcelDataParser parser = new ExcelDataParser();
        List< IncomingDataPoint> data = parser.parse(mockCSVInput);

        // Check if it read correctly and parsed the first line
        assertEquals(100, data.get(0).getMeasurementValue());
    }


    /**
     * Potential edge case: when the parser get broken data.
     */
    @Test
    void brokenCSVdata() {
        String mockCSVInput=
                "1,,Saturation,1000\n" +
                        "1,90,,1000";

        ExcelDataParser parser = new ExcelDataParser();
        List< IncomingDataPoint> data = parser.parse(mockCSVInput);

        // Check if it read correctly and parsed the first line
        assertTrue(data.isEmpty());
    }


    /**
     * Edge case: maybe the parser can handle if there is only broken or only correct data
     * but what happens if there is both. We need to integrate the correct data, which is not broken.
     */
    @Test
    void brokenandCorrectCSVdata() {
        String mockCSVInput=
                "1,,Saturation,1000\n" +
                        "1,90,Saturation,1000";

        ExcelDataParser parser = new ExcelDataParser();
        List< IncomingDataPoint> data = parser.parse(mockCSVInput);

        // Check if it read correctly and parsed the first line
        assertEquals(1, data.size()); // Only can get 1 record
        assertEquals(90, data.get(0).getMeasurementValue());
    }


    /**
     * Potential 2 edge cases: We may mark the time stamp with L in the JSON, or may not.
     * The reader should be able o read both, if the data is not broken just marked otherwise.
     */
    @Test
    void JSONParserTestWithTimeStampL() {
        String mockInput=
                "{\n" +
                        "\t\"patientId\":1,\n" +
                        "\t\"measurementValue\":100.0,\n" +
                        "\t\"recordType\":\"ECG\",\n" +
                        "\t\"timeStamp\":1000L\n" +
                        "}";

        // In this test we marked the time stamp with L measurement type, in the JSON: 1000L
        JSONDataParser parser = new JSONDataParser();
        List< IncomingDataPoint> data = parser.parse(mockInput);

        // Check if it read correctly and parsed the first line
        assertEquals(1000, data.get(0).getTimestamp());
    }


    @Test
    void JSONParserTestWithoutTimeStampL() {
        String mockInput=
                "{\n" +
                        "\t\"patientId\":1,\n" +
                        "\t\"measurementValue\":100.0,\n" +
                        "\t\"recordType\":\"ECG\",\n" +
                        "\t\"timeStamp\":1000\n" +
                        "}";

        JSONDataParser parser = new JSONDataParser();
        List< IncomingDataPoint> data = parser.parse(mockInput);


        // In this test I did not mark the time stamp with L measurement type, in the JSON: 1000
        // Check if it read correctly and parsed the first line
        assertEquals(1000, data.get(0).getTimestamp());
    }


    @Test
    void JSONParserTestBrokenData() {
        String mockInput=
                "{\n" +
                        "\t\"patientId\":2,\n" +
                        "\t\"measurementValue\":,\n" +
                        "\t\"recordType\":\"ECG\",\n" +
                        "}";

        JSONDataParser parser = new JSONDataParser();
        List< IncomingDataPoint> data = parser.parse(mockInput);


        // In this test I did not mark the time stamp with L measurement type, in the JSON: 1000
        // Check if it read correctly and parsed the first line
        assertTrue(data.isEmpty());
    }


    @Test
    void WebsocketDataListenerTest() {
        JSONDataParser parser = new JSONDataParser();
        Map<Integer, HospitalPatient> patients = new HashMap<>();
        DataStorage dataStorage = DataStorage.getInstance();
        PatientIdentifier identifier = new PatientIdentifier(patients);
        IdentityManager identityManager =
                new IdentityManager(patients, dataStorage,
                        new MismatchHandler(null,0),identifier);


        // Need to add at least one record, in order to have the patient in the server
        // If the patient does not exist on server, then there will be a mismatch
        // when we try to add the incoming data point, because there no exists a patient
        // with the same id as the incoming data point record's id
        // Purpose: We can only 'add' simulation data for existing patients(Who exists in hospital)
        // This is my strategy for mismatch handling
        // There is a test when you try to add data for non-existing patient
        // When you try to add incoming data point(record from simulator) to a patient
        // who does not exist=try to add an element to a null list
        dataStorage.addPatientData(1,100,"Saturation",11000L);
        DataSourceAdapter adapter = new DataSourceAdapter(identityManager);

        WebSocketDataListener listener = new WebSocketDataListener(parser, adapter);
        String mockInput=
                "{\n" +
                        "\t\"patientId\":1,\n" +
                        "\t\"measurementValue\":100.0,\n" +
                        "\t\"recordType\":\"ECG\",\n" +
                        "\t\"timeStamp\":1000\n" +
                        "}";

        listener.onMessage(mockInput);
        assertTrue(dataStorage.getPatientById(1).
                getAllRecords().get(1).getRecordType().equals("ECG"));
    }

    /**
     * Not working yet.
     */
    /*@Test
    public void JSONParserTestBrokenDataAndCorrectData() {
        String mockCSVInput= "{\n" +
                "\t\"patientId\":1,\n" +
                "\t\"measurementValue\":100.0,\n" +
                "\t\"timeStamp\":1000\n" +
                "}\n" +
                "{\n" +
                "\t\"patientId\":1,\n" +
                "\t\"measurementValue\":100.0,\n" +
                "\t\"recordType\":\"ECG\",\n" +
                "\t\"timeStamp\":1000\n" +
                "}";

        JSONDataParser parser = new JSONDataParser();
        List< IncomingDataPoint> data = parser.parse(mockCSVInput);


        // In this test I did not mark the time stamp with L measurement type, in the JSON: 1000
        // Check if it read correctly and parsed the first line
        assertTrue(data.isEmpty());
    }

     */
}
