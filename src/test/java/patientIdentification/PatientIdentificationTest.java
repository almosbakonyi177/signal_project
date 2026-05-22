package patientIdentification;

import static org.junit.jupiter.api.Assertions.*;

import com.dataAccess.DataSourceAdapter;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.patientIdentification.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for testing the Patient Identification module.
 */
public class PatientIdentificationTest {


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
    void testValidateMatch()
    {
        DataStorage dataStorage = DataStorage.getInstance();
        dataStorage.addPatientData(1, 100,"ECG",1000L);

        IdentityManager identityManager = new IdentityManager(dataStorage, null);


        assertTrue(identityManager.validateMatch(1));
    }


    /**
     * Edge case: Test what happens, if we want to validate the match with a non existing
     * hospital patient.
     */
    @Test
    void testValidateMatchNonExistent()
    {
        DataStorage dataStorage = DataStorage.getInstance();

        IdentityManager identityManager = new IdentityManager(dataStorage,
                new MismatchHandler(null));


        assertFalse(identityManager.validateMatch(1));
    }


    @Test
    void testHandleMismatch()
    {
        DataStorage dataStorage = DataStorage.getInstance();
        MismatchHandler mismatchHandler = new MismatchHandler(new ArrayList<String>());
        dataStorage.addPatientData(1, 100,"ECG",1000L);
        IdentityManager identityManager = new IdentityManager(dataStorage,
                mismatchHandler);

        assertFalse(identityManager.validateMatch(3));
    }


    @Test
    public void testFindHospitalPatient()
    {
        DataStorage dataStorage = DataStorage.getInstance();
        dataStorage.addPatientData(1, 0, "null", 0);

        IdentityManager identityManager = new IdentityManager(dataStorage, null);

        HospitalPatient patient = identityManager.retrieveHospitalPatient(1);
        assertNotNull(patient);
    }


    @Test
    public void testMismatchLog()
    {
        DataStorage dataStorage = DataStorage.getInstance();
        List<String> log = new ArrayList<>();

        MismatchHandler mismatchHandler = new MismatchHandler(log);
        IdentityManager identityManager = new IdentityManager(dataStorage,
                mismatchHandler);

        identityManager.validateMatch(2);

        // There should be one documented mismatch on the mismatch log
        assertEquals(1, mismatchHandler.getMismatchLog().size());
    }


    @Test
    void IncomingDataPointToNonExistingPatient() {
        DataStorage dataStorage = DataStorage.getInstance();
        IncomingDataPoint incomingDataPoint = new IncomingDataPoint(
                1,100,"Saturation",1000L);


        MismatchHandler mismatchHandler = new MismatchHandler(
                new ArrayList<String>());


        IdentityManager identityManager = new IdentityManager(dataStorage,
                mismatchHandler);

        DataSourceAdapter adapter = new DataSourceAdapter(identityManager);

        // We try to integrate the incoming data point to our inner server
        // If the patient does not exist in the server yet, we document the mismatch first,
        // Then add the patient to the inner server.
        adapter.integrateData(incomingDataPoint);

        // Check if we got one mismatch and it was handled correctly by the mismatch handler
        assertEquals(1,mismatchHandler.getMismatchLog().size());
        assertEquals(1, dataStorage.getPatientById(1).getAllRecords().size());
    }

    @Test
    void testAddRecordToExistingHospitalPatient()
    {
        DataStorage dataStorage = DataStorage.getInstance();
        Map<Integer, HospitalPatient> hospital_patients = new HashMap<>();

        // We add a patient data to the core storing unit, it's like an inner server
        dataStorage.addPatientData(1, 0, "null", 0);

        // An incoming record
        IncomingDataPoint data = new IncomingDataPoint(1,0,
                "Saturation", 1000);


        IdentityManager identityManager = new IdentityManager(dataStorage, null);

        DataSourceAdapter dataSourceAdapter = new DataSourceAdapter(identityManager);

        // We will try to integrate the data to the main server.
        dataSourceAdapter.integrateData(data);

        // There should be 2 records in the patient records:
        // One from the core server, and one from the outside, which was
        // integrated into the core server
        assertEquals(2, identityManager.retrieveHospitalPatient(
                1).getPatientRecords().size());
    }


    /**
     * Edge case: What happens when we try to add an already existing record to a patient
     * We should not have duplicates.
     * Duplicate means: 2 records have the same type, measurement value, timestamp,(and
     * obviously they are linked to the same patient)
     */
    @Test
    void addExistingRecord(){
        DataStorage dataStorage = DataStorage.getInstance();
        Map<Integer, HospitalPatient> hospital_patients = new HashMap<>();

        // We add a patient data to the core storing unit, it's like an inner server
        dataStorage.addPatientData(1, 100, "Saturation", 1000L);

        // An incoming record
        IncomingDataPoint data = new IncomingDataPoint(1,100,
                "Saturation", 1000L);

        IdentityManager identityManager = new IdentityManager(dataStorage, null);


        DataSourceAdapter dataSourceAdapter = new DataSourceAdapter(identityManager);

        // We will try to integrate the data to the main server.
        dataSourceAdapter.integrateData(data);


        // There should be 1 record in the patient records, because the second that we tried to add
        // is a duplicate
        assertEquals(1, identityManager.retrieveHospitalPatient(
                1).getPatientRecords().size());
    }
}