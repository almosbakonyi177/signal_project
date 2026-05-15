package com.alerts.alertStrategies;

import com.alerts.Alert;
import com.alerts.alertFactory.AlertFactory;
import com.alerts.alertFactory.TriggeredAlertFactory;
import com.alerts.alertStrategies.AlertStrategy;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;

/**
 *
 */
public class TriggeredAlertStrategy implements AlertStrategy {
    /**
     *
     * @param patient
     * @return
     */
    @Override
    public ArrayList<Alert> checkAlert(Patient patient) {
        AlertFactory alertFactory = new TriggeredAlertFactory();
        ArrayList<Alert> alerts = new ArrayList<Alert>();
        for (PatientRecord record : patient.getAllRecords()) {
            if (record.getRecordType().equals("Alert") && record.getMeasurementValue()==1) {
                Alert alert = alertFactory.createAlert(patient.getPatientId(),
                        "Triggered", record.getTimestamp());
                alerts.add(alert);
            }
        }
        return alerts;
    }
}
