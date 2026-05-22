package com.patientIdentification;

import com.data_management.PatientRecord;

import java.util.List;

/**
 * Compares the incoming patient records against existing patient records in the storage.
 * Prevents record duplication.
 */
public class PatientRecordComparison {
    /**
     * Checks if the incoming patient record is already in the storage, helps to avoid duplicates.
     * @param records List of {@link PatientRecord} objects, in which list we want to
     *                check if the given Incoming Data points is already in.
     * @param incomingRecord The incoming record we check if already in the storage.
     * @return True if the incoming record is already in the storage, otherwise false.
     */
    public boolean recordsContain(List<PatientRecord> records, PatientRecord incomingRecord) {
        if (records==null || records.isEmpty() ||  incomingRecord==null) {
            return false;
        }

        for (PatientRecord record : records) {
            if(record.getPatientId()==incomingRecord.getPatientId()
            && record.getRecordType().equals(incomingRecord.getRecordType())
            && record.getTimestamp()==incomingRecord.getTimestamp()
            && record.getMeasurementValue()==incomingRecord.getMeasurementValue()){
                return true;
            }
        }
        return false;
    }
}
