package com.patientIdentification;

import com.data_management.PatientRecord;
import java.util.HashMap;
import java.util.List;

// Represents the hospital patient, to who the measurements will be linked.
public class HospitalPatient {
    private int patientId;
    private List<PatientRecord> patientRecords;
    // Each patient has their own thresholds for measurement types
    private HashMap<String, Double> alertThresholds = new HashMap<>();

    public HospitalPatient(int patientId,  List<PatientRecord> patientRecords,
                           HashMap<String, Double> alertThresholds) {
        this.patientId = patientId;
        this.patientRecords = patientRecords;
        this.alertThresholds = alertThresholds;
    }

    /**
     * Retrieves this hospital patient's records.
     * @return This hospital patient's records.
     */
    public List<PatientRecord> getPatientRecords() {
        return this.patientRecords;
    }
}