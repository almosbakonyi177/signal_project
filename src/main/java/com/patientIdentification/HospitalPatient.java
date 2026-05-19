package com.patientIdentification;

import com.data_management.PatientRecord;
import java.util.List;

/**
 * Represents the copy of a hospital patient, whose data can be retrieved and
 * to whom the measurements will be linked.
 */
public class HospitalPatient {
    private int patientId;
    private List<PatientRecord> patientRecords;


    public HospitalPatient(int patientId,  List<PatientRecord> patientRecords) {
        this.patientId = patientId;
        this.patientRecords = patientRecords;
    }

    /**
     * Retrieves this hospital patient's records.
     * @return This hospital patient's records.
     */
    public List<PatientRecord> getPatientRecords() {
        return this.patientRecords;
    }
}