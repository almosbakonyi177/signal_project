package com.data_management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a patient and manages their medical records.
 * This class stores patient-specific data, allowing for the addition and
 * retrieval
 * of medical records based on specified criteria.
 */
public class Patient {
    private int patientId;
    private List<PatientRecord> patientRecords;
    // Each patient has their own thresholds for measurement types
    private HashMap<String, Double> alertThresholds = new HashMap<>();

    /**
     * Constructs a new Patient with a specified ID.
     * Initializes an empty list of patient records.
     *
     * @param patientId the unique identifier for the patient
     */
    public Patient(int patientId) {
        this.patientId = patientId;
        this.patientRecords = new ArrayList<>();
    }

    /**
     * Adds a new record to this patient's list of medical records.
     * The record is created with the specified measurement value, record type, and
     * timestamp.
     *
     * @param measurementValue the measurement value to store in the record
     * @param recordType       the type of record, e.g., "HeartRate",
     *                         "BloodPressure"
     * @param timestamp        the time at which the measurement was taken, in
     *                         milliseconds since UNIX epoch
     */
    public void addRecord(double measurementValue, String recordType, long timestamp) {
        PatientRecord record = new PatientRecord(this.patientId, measurementValue, recordType, timestamp);
        this.patientRecords.add(record);
    }

    /**
     * Goes through on all the records of this patient and delete the
     * records that are older than 180 days.
     */
    public void removeOldRecords() {
        for (PatientRecord record : this.patientRecords) {
            if (record.getDaysAfterCreation() > 180) {
                this.patientRecords.remove(record);
            }
        }
    }

    /**
     * Retrieves a list of PatientRecord objects for this patient that fall within a
     * specified time range.
     * The method filters records based on the start and end times provided.
     *
     * @param startTime the start of the time range, in milliseconds since UNIX
     *                  epoch
     * @param endTime   the end of the time range, in milliseconds since UNIX epoch
     * @return a list of PatientRecord objects that fall within the specified time
     *         range
     */
    public List<PatientRecord> getRecords(long startTime, long endTime) {
        ArrayList<PatientRecord> patientRecords = new ArrayList<>();
        for(PatientRecord record : this.patientRecords) {
            if(record.getTimestamp() >= startTime && record.getTimestamp() <= endTime) {
                patientRecords.add(record);
            }
        }
        return patientRecords;
    }

    /**
     * Returns a collection of all patient records.
     * @return collection of all patient records.
     */
    public List<PatientRecord> getAllRecords() {
        ArrayList<PatientRecord> patientRecords = new ArrayList<>();
        for(PatientRecord record : this.patientRecords) {
            patientRecords.add(record);
        }
        return patientRecords;
    }

    /**
     * Returns all the alert thresholds for this patient.
     * @return all the measurement type and the threshold for them.
     */
    public HashMap<String, Double> getAlertThresholds() {
        return alertThresholds;
    }

    /**
     * Adds alert threshold name and threshold value for the patient.
     * @param measurement The measurement type we want to establish a threshold for.
     * @param alertThreshold The threshold value.
     */
    public void addAlertThreshold(String measurement, double alertThreshold) {
        this.alertThresholds.put(measurement, alertThreshold);
    }

    /**
     * Returns this patient's patient id.
     * @return This patient's patient id.
     */
    public int getPatientId() {
        return patientId;
    }
}
