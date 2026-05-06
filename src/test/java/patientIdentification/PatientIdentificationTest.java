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
        HospitalPatient patient = new HospitalPatient(1, null, null);


        hospital_patients.put(1, patient);
        IdentityManager identityManager = new IdentityManager(hospital_patients, dataStorage,
                null);


        assertTrue(identityManager.validateMatch(1));
    }

    @Test
    void testHandleMismatch()
    {
        DataStorage dataStorage = new DataStorage();
        Map<Integer, HospitalPatient> hospital_patients = new HashMap<>();
        HospitalPatient patient = new HospitalPatient(1, null, null);
        MismatchHandler mismatchHandler = new MismatchHandler(new ArrayList<String>(),
                0,0,0);

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
        assertTrue(patient!=null);
    }

    @Test
    public void testMismatchLog()
    {
        DataStorage dataStorage = new DataStorage();
        Map<Integer, HospitalPatient> hospital_patients = new HashMap<>();
        List<String> log = new ArrayList<>();

        MismatchHandler mismatchHandler = new MismatchHandler(log, 2026,
                5,1);
        IdentityManager identityManager = new IdentityManager(hospital_patients, dataStorage,
                mismatchHandler);
        identityManager.validateMatch(2);

        //There should be one documented mismatch on the mismatch log
        assertEquals(mismatchHandler.getMismatchLog().size(), 1);
    }

    @Test
    public void testAddRecordToExistingHospitalPatient()
    {
        DataStorage dataStorage = new DataStorage();
        Map<Integer, HospitalPatient> hospital_patients = new HashMap<>();
        List<String> log = new ArrayList<>();

        // We add a patient data to the core storing unit, it's like an inner server
        dataStorage.addPatientData(1, 0, "null", 0);

        // An incoming record
        IncomingDataPoint data = new IncomingDataPoint(1,0,
                "Saturation", 1000);

        MismatchHandler mismatchHandler = new MismatchHandler(log, 2026,
                5,1);

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
        identityManager.copyHospitalPatients();

        // There should be 2 records in the patient records:
        // One from the core server, and one from the outside, which was
        // integrated into the core server
        assertEquals(identityManager.retrieveHospitalPatient(
                1).getPatientRecords().size(), 2);
    }
}
