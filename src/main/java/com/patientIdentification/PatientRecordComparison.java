package com.patientIdentification;

import com.data_management.PatientRecord;

import java.util.List;

/**
 * Responsible for checking if the incoming patient record data is in the patient records
 * already.
 */
public class PatientRecordComparison {
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
