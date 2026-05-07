package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.*;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Responsible for testing the Data storing and retrieval module.
 */
class DataStorageTest {


    //Unit tests first
    @Test
    void testAddAndGetRecords() {
        // TODO Perhaps you can implement a mock data reader to mock the test data?
        // DataReader reader
        DataStorage storage = new DataStorage();
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);

        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(2, records.size()); // Check if two records are retrieved
        assertEquals(100.0, records.get(0).getMeasurementValue()); // Validate first record
    }


    @Test
    void testGetAllRecordsForPatient() {
        DataStorage storage = new DataStorage();
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(2, 200.0, "WhiteBloodCells", 1714376789051L);
        storage.addPatientData(2, 100.0, "SystolicPressure", 1814376789051L);
        storage.addPatientData(2, 100.0, "SystolicPressure", 1814378789051L);


        List<PatientRecord> records = storage.getPatientById(2).getAllRecords();
        assertEquals(3, records.size()); // Check if three records are retrieved
        assertEquals(100.0, records.get(2).getMeasurementValue()); // Validate last record
    }


    @Test
    void testAuditLoggerAddRequest() {
        AuditLogger auditLogger = new AuditLogger();
        auditLogger.addRequest(1,1,false);
        auditLogger.addRequest(2,1,false);

        assertEquals(2, auditLogger.getRequests().size());
    }


    @Test
    void AddPatientRecord() {
        Patient patient = new Patient(1);
        patient.addRecord(90, "SystolicPressure", 1814376789051L);
        patient.addRecord(100,"DiastolicPressure", 1814376789053L);

        assertEquals(patient.getAllRecords().size(), 2);
        assertEquals(patient.getAllRecords().get(0).getMeasurementValue(), 90);
    }






    // Integration tests
    @Test
    void testDataRetrieveForStaff() {

        DataStorage storage = new DataStorage();
        // Create a staff member
        storage.addStaffMemberData(1,"Lucas", "Man",3);

        // Every staff member can retrieve patient data above role level 1
        DataRetriever dataRetriever = new DataRetriever(storage,2);

        StaffMember staffMember = storage.getStaffMembers().get(1);

                // We add a patient data/record
        storage.addPatientData(1, 100.0,
                "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(2, 100.0,
                "WhiteBloodCells", 1714376789050L);


        List<PatientRecord> records = dataRetriever.makeQuery(staffMember,1);
        assertEquals(1, records.size()); // Should be retrieved only one record for patient 1
        assertEquals(100.0, records.get(0).getMeasurementValue());
    }


    @Test
    void testLowLevelStaffDataRequest() {

        DataStorage storage = new DataStorage();
        // Create a staff member
        storage.addStaffMemberData(1,"Lucas", "Man",1);

        // Every staff member can retrieve patient data above role level 1
        DataRetriever dataRetriever = new DataRetriever(storage,2);

        StaffMember staffMember = storage.getStaffMembers().get(1);

        // We add a patient data/record
        storage.addPatientData(1, 100.0,
                "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(2, 100.0,
                "WhiteBloodCells", 1714376789050L);


        List<PatientRecord> records = dataRetriever.makeQuery(staffMember,1);
        assertEquals(null, records); // Should be no retrieved data because no access
        assertEquals(false, dataRetriever.getAuditLogger().getRequests().get(0).isAccessGiven());
    }


    /**
     * Edge case: Test what happens if we want to retrieve data from a non-existing patient.
     */
    @Test
    void testRetrieveNonExistingPatientId() {

        DataStorage storage = new DataStorage();
        // Create a staff member
        storage.addStaffMemberData(1,"Lucas", "Man",3);

        // Every staff member can retrieve patient data above role level 1
        DataRetriever dataRetriever = new DataRetriever(storage,2);

        StaffMember staffMember = storage.getStaffMembers().get(1);

        List<PatientRecord> records = dataRetriever.makeQuery(staffMember,1);
        assertEquals(null, records);
    }


    @Test
    void deletedStaffMemberDataRequest() {
        DataStorage storage = new DataStorage();
        storage.addStaffMemberData(1,"Lucas", "Man",3);
        DataRetriever dataRetriever = new DataRetriever(storage,2);
        storage.addPatientData(1, 100.0,
                "WhiteBloodCells", 1714376789050L);

        StaffMember staffMember = storage.getStaffMembers().get(0);
        storage.deleteStaffMember(1);

        assertNull(dataRetriever.makeQuery(staffMember,1));
    }
}