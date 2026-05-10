package patientIdentification;

import static org.junit.jupiter.api.Assertions.*;

import com.dataAccess.DataSourceAdapter;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.patientIdentification.*;
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
    @Test
    void testValidateMatch()
    {
        DataStorage dataStorage = new DataStorage();
        Map<Integer, HospitalPatient> hospital_patients = new HashMap<>();
        HospitalPatient patient = new HospitalPatient(1, null);


        hospital_patients.put(1, patient);
        IdentityManager identityManager = new IdentityManager(hospital_patients, dataStorage,
                null);


        assertTrue(identityManager.validateMatch(1));
    }


    /**
     * Edge case: Test what happens, if we want to validate the match with a non existing
     * hospital patient.
     */
    @Test
    void testValidateMatchNonExistent()
    {
        DataStorage dataStorage = new DataStorage();
        Map<Integer, HospitalPatient> hospital_patients = new HashMap<>();

        IdentityManager identityManager = new IdentityManager(hospital_patients, dataStorage,
                new MismatchHandler(null, 0));


        assertFalse(identityManager.validateMatch(1));
    }


    @Test
    void testHandleMismatch()
    {
        DataStorage dataStorage = new DataStorage();
        Map<Integer, HospitalPatient> hospital_patients = new HashMap<>();
        HospitalPatient patient = new HospitalPatient(1, null);
        MismatchHandler mismatchHandler = new MismatchHandler(new ArrayList<String>(),
                0);

        hospital_patients.put(1, patient);
        IdentityManager identityManager = new IdentityManager(hospital_patients, dataStorage,
                mismatchHandler);

        assertFalse(identityManager.validateMatch(3));
    }


    @Test
    public void testFindHospitalPatient()
    {
        DataStorage dataStorage = new DataStorage();
        dataStorage.addPatientData(1, 0, "null", 0);
        Map<Integer, HospitalPatient> hospital_patients = new HashMap<>();

        IdentityManager identityManager = new IdentityManager(hospital_patients, dataStorage,
                null);

        identityManager.copyHospitalPatients();
        HospitalPatient patient = identityManager.retrieveHospitalPatient(1);
        assertNotNull(patient);
    }


    @Test
    public void testMismatchLog()
    {
        DataStorage dataStorage = new DataStorage();
        Map<Integer, HospitalPatient> hospital_patients = new HashMap<>();
        List<String> log = new ArrayList<>();

        MismatchHandler mismatchHandler = new MismatchHandler(log, 10000L);
        IdentityManager identityManager = new IdentityManager(hospital_patients, dataStorage,
                mismatchHandler);
        identityManager.validateMatch(2);

        //There should be one documented mismatch on the mismatch log
        assertEquals(1, mismatchHandler.getMismatchLog().size());
    }

    @Test
    void IncomingDataPointToNonExistingPatient() {
        DataStorage dataStorage = new DataStorage();
        IncomingDataPoint incomingDataPoint = new IncomingDataPoint(
                1,100,"Saturation",1000L);
        Map<Integer, HospitalPatient> hospital_patients = new HashMap<>();

        MismatchHandler mismatchHandler = new MismatchHandler(
                new ArrayList<String>(),0);

        IdentityManager identityManager = new IdentityManager(hospital_patients, dataStorage,
                mismatchHandler);

        DataSourceAdapter adapter = new DataSourceAdapter(identityManager);

        // We try to integrate the incoming data point to our inner server
        // It will try to validate a match->won't happen->mismatch->mismatch handler documents it
        adapter.integrateData(incomingDataPoint);

        // Check if we got one mismatch and it was handled correctly by the mismatch handler
        assertEquals("1,0", mismatchHandler.getMismatchLog().get(0));
    }

    @Test
    void testAddRecordToExistingHospitalPatient()
    {
        DataStorage dataStorage = new DataStorage();
        Map<Integer, HospitalPatient> hospital_patients = new HashMap<>();
        List<String> log = new ArrayList<>();

        // We add a patient data to the core storing unit, it's like an inner server
        dataStorage.addPatientData(1, 0, "null", 0);

        // An incoming record
        IncomingDataPoint data = new IncomingDataPoint(1,0,
                "Saturation", 1000);

        MismatchHandler mismatchHandler = new MismatchHandler(log, 2000L);

        IdentityManager identityManager = new IdentityManager(hospital_patients, dataStorage,
                mismatchHandler);

        // We retrieve the data from the inner server, and store it in the outer server
        // for short term use
        identityManager.copyHospitalPatients();

        DataSourceAdapter dataSourceAdapter = new DataSourceAdapter(identityManager);

        // We will try to integrate the data to the main server.
        dataSourceAdapter.integrateData(data);

        // If we don't copy we cannot retrieve it
        // We cannot directly retrieve patient data from inner server
        // We copy the data from inner server and store it for short term use(as deep copy)
        identityManager.copyHospitalPatients();

        // There should be 2 records in the patient records:
        // One from the core server, and one from the outside, which was
        // integrated into the core server
        assertEquals(2, identityManager.retrieveHospitalPatient(
                1).getPatientRecords().size());
    }
}