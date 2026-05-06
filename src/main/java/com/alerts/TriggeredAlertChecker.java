package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TriggeredAlertChecker implements AlertCondition{
    /**
     *
     * @param patient
     * @return
     */
    @Override
    public ArrayList<Alert> check(Patient patient) {
        ArrayList<Alert> alerts = new ArrayList<Alert>();
        for (PatientRecord record : patient.getAllRecords()) {
            if (record.getRecordType().equals("Alert") && record.getMeasurementValue()==1) {
                Alert alert = new Alert(patient.getPatientId(),
                        "Triggered", record.getTimestamp(), record.getRecordType());
                alerts.add(alert);
            }
        }
        return alerts;
    }
}
