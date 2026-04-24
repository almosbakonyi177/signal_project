package com.data_management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alerts.AlertGenerator;

/**
 * Manages storage and retrieval of patient data within a healthcare monitoring
 * system.
 * This class serves as a repository for all patient records, organized by
 * patient IDs.
 */
public class DataStorage {
    private Map<Integer, Patient> patientMap; // Stores patient objects indexed by their unique patient ID.
    private Map<Integer, StaffMember>  staffMemberMap;
    /**
     * Constructs a new instance of DataStorage, initializing the underlying storage
     * structure.
     */
    public DataStorage() {
        this.patientMap = new HashMap<>();
        this.staffMemberMap = new HashMap<>();
    }

    /**
     * Adds or updates patient data in the storage.
     * If the patient does not exist, a new Patient object is created and added to
     * the storage.
     * Otherwise, the new data is added to the existing patient's records.
     *
     * @param patientId        the unique identifier of the patient
     * @param measurementValue the value of the health metric being recorded
     * @param recordType       the type of record, e.g., "HeartRate",
     *                         "BloodPressure"
     * @param timestamp        the time at which the measurement was taken, in
     *                         milliseconds since the Unix epoch
     */
    public void addPatientData(int patientId, double measurementValue, String recordType, long timestamp) {
        Patient patient = patientMap.get(patientId);
        if (patient == null) {
            patient = new Patient(patientId);
            patientMap.put(patientId, patient);
        }
        patient.addRecord(measurementValue, recordType, timestamp);
    }

    /**
     * Retrieves a list of PatientRecord objects for a specific patient, filtered by
     * a time range.
     *
     * @param patientId the unique identifier of the patient whose records are to be
     *                  retrieved
     * @param startTime the start of the time range, in milliseconds since the Unix
     *                  epoch
     * @param endTime   the end of the time range, in milliseconds since the Unix
     *                  epoch
     * @return a list of PatientRecord objects that fall within the specified time
     *         range
     */
    public List<PatientRecord> getRecords(int patientId, long startTime, long endTime) {
        Patient patient = patientMap.get(patientId);
        if (patient != null) {
            return patient.getRecords(startTime, endTime);
        }
        return new ArrayList<>(); // return an empty list if no patient is found
    }


    public List<PatientRecord> getPatientRecords(int patientId) {
        Patient patient = patientMap.get(patientId);
        if(patient != null) {
            patient.getAllRecords();
        }
        return new ArrayList<>();
    }

    /**
     * Retrieves a collection of all patients stored in the data storage.
     *
     * @return a list of all patients
     */
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patientMap.values());
    }


    /**
     * Retrieves a collection of all staff members stored in the data storage.
     * @return list of all stuff members.
     */
    public Map<Integer, StaffMember> getStaffMembers() {
        return staffMemberMap;
    }

    /**
     * Adds a staff member to the list.
     * It can happen if someone resigns or gets fired.
     * @param staffMemberId
     */
    public void addStaffMemberData(int staffMemberId, String firstname,
                                   String lastname, int roleLevel) {

        StaffMember staffMember = new StaffMember(staffMemberId, firstname, lastname, roleLevel);
        staffMemberMap.put(staffMemberId, staffMember);
    }

    /**
     * Removes the chosen staff member from the staff member Map.
     * It can happen if someone resigns or gets fired.
     * @param staffMemberId The Id of staff member that will be deleted from the staff member Map.
     */
    public void deleteStaffMember(int staffMemberId) {
        if (staffMemberMap.containsKey(staffMemberId)) {
            staffMemberMap.remove(staffMemberId);
        }
    }


    /**
     * The main method for the DataStorage class.
     * Initializes the system, reads data into storage, and continuously monitors
     * and evaluates patient data.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // DataReader is not defined in this scope, should be initialized appropriately.
        // DataReader reader = new SomeDataReaderImplementation("path/to/data");
        DataStorage storage = new DataStorage();

        // Assuming the reader has been properly initialized and can read data into the
        // storage
        // reader.readData(storage);

        // Example of using DataStorage to retrieve and print records for a patient
        List<PatientRecord> records = storage.getRecords(1, 1700000000000L, 1800000000000L);
        for (PatientRecord record : records) {
            System.out.println("Record for Patient ID: " + record.getPatientId() +
                    ", Type: " + record.getRecordType() +
                    ", Data: " + record.getMeasurementValue() +
                    ", Timestamp: " + record.getTimestamp());
        }

        // Initialize the AlertGenerator with the storage
        AlertGenerator alertGenerator = new AlertGenerator(storage);

        // Evaluate all patients' data to check for conditions that may trigger alerts
        for (Patient patient : storage.getAllPatients()) {
            alertGenerator.evaluateData(patient);
        }
    }
}