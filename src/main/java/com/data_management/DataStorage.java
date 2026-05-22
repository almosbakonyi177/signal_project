package com.data_management;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.alerts.alertStrategies.*;
import com.dataAccess.*;
import com.dataAccess.dataParsing.DataParser;
import com.dataAccess.dataParsing.JSONDataParser;
import com.dataAccess.dataReading.DataReader;
import com.dataAccess.dataReading.FileDataReader;
import com.dataAccess.dataReading.SimulationDataReader;
import com.dataAccess.dataReading.WebsocketClient;
import com.patientIdentification.HospitalPatient;
import com.patientIdentification.IdentityManager;
import com.patientIdentification.MismatchHandler;
import com.patientIdentification.PatientIdentifier;

/**
 * Manages storage and retrieval of patient data within a healthcare monitoring
 * system.
 * This class serves as a repository for all patient records, organized by
 * patient IDs.
 */
public class DataStorage {
    // Stores patient objects indexed by their unique patient ID.
    private ConcurrentHashMap<Integer, Patient> patientMap;

    private ConcurrentHashMap<Integer, StaffMember>  staffMemberMap;
    private static DataStorage instance;
    /**
     * Constructs a new instance of DataStorage, initializing the underlying storage
     * structure. Uses singleton constructor.
     */
    private DataStorage() {
        this.patientMap = new ConcurrentHashMap<>();
        this.staffMemberMap = new ConcurrentHashMap<>();
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
        Patient patient = patientMap.computeIfAbsent(patientId, Patient::new);
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
            return patient.getAllRecords();
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
     * @return Map of all stuff members.
     */
    public Map<Integer, StaffMember> getStaffMembers() {
        return staffMemberMap;
    }

    /**
     * Adds a staff member to the list.
     * It can happen if someone resigns or gets fired.
     * @param staffMemberId The id of staff member who is being added to the system.
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
     * Retrieves the patient object by given patient Id, if the patient exists.
     * @param patientId The Id of patient, who we search for.
     * @return The patient with the searched Id if the patient exists in the data storage,
     * otherwise null.
     */
    public Patient getPatientById(int patientId) {
        for (Integer patient : patientMap.keySet()) {
            if(patient==patientId){
                return patientMap.get(patient);
            }
        }
        return null;
    }

    /**
     * Returns the only existing Data Storage in the program.
     * If no Data Storage exists, it creates one. Implements Singleton design pattern.
     * @return The only existing Data Storage in the program if exists one,
     * otherwise it creates one.
     */
    public static DataStorage getInstance() {
        if (instance==null){
            instance = new DataStorage();
        }
        return instance;
    }

    public void clearStorage() {
        this.patientMap.clear();
        this.staffMemberMap.clear();
    }


    /**
     * The main method for the DataStorage class.
     * Initializes the system, reads data into storage, and continuously monitors
     * and evaluates patient data.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) throws IOException, URISyntaxException {
        // DataReader is not defined in this scope, should be initialized appropriately.
        DataStorage storage = DataStorage.getInstance();
        DataParser parser = new JSONDataParser();


        MismatchHandler mismatchHandler = new MismatchHandler(new ArrayList<String>());
        IdentityManager identityManager = new IdentityManager(storage, mismatchHandler);

        DataSourceAdapter adapter=new DataSourceAdapter(identityManager);

        // Use injection for interface Data parser and data source adapter, polymorphism
        // We could use any data parser, requires one small change
        URI path=new URI("ws://websocket:1");
        AlertGenerator alertGenerator= new AlertGenerator(DataStorage.getInstance());
        // Add the Strategies in runtime
        alertGenerator.addAlertStrategy(new BloodPressureStrategy());
        alertGenerator.addAlertStrategy(new OxygenSaturationStrategy());
        alertGenerator.addAlertStrategy(new ECGPeakStrategy());
        alertGenerator.addAlertStrategy(new HypotensiveHypoxemiaStrategy());
        alertGenerator.addAlertStrategy(new TriggeredAlertStrategy());

        DataReader reader = new WebsocketClient(parser, adapter, path, alertGenerator);
        reader.connect();

        // Assuming the reader has been properly initialized and can read data into the
        // storage
        // String filePath=System.getProperty("user.dir")+"\\src";
        // System.out.println("Loading data from "+filePath);
        // reader.readData(new File(filePath+"\\SimulationOutput.txt"));


        // Example of using DataStorage to retrieve and print records for a patient
        List<PatientRecord> records = storage.getRecords(1, 0, 1800000000000L);
        for (PatientRecord record : records) {
            System.out.println("Record for Patient ID: " + record.getPatientId() +
                    ", Type: " + record.getRecordType() +
                    ", Data: " + record.getMeasurementValue() +
                    ", Timestamp: " + record.getTimestamp());
        }
    }
}