package com.alerts.alertStrategies;

import com.alerts.Alert;
import com.alerts.alertFactory.AlertFactory;
import com.alerts.alertFactory.BloodOxygenAlertFactory;
import com.alerts.alertFactory.ECGAlertFactory;
import com.alerts.alertStrategies.AlertStrategy;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;

/**
 * Responsible for checking if the patient's Blood Saturation level
 * is too low or dropped more than 5% in 10 minutes. If any of these requirements
 * happens then we need to trigger an alert.
 */
public class OxygenSaturationStrategy implements AlertStrategy {

    /**
     * Evaluates the given patient's blood saturation records and generates a list
     * of alerts if there was any critical value or trends.
     * @param patient The patient, whose blood saturation records are being evaluated.
     * @return List of alerts that need to be triggered, if there was any,
     * otherwise an empty list.
     */
    @Override
    public ArrayList<Alert> checkAlert(Patient patient) {
        ArrayList<Alert> alerts = new ArrayList<>();
        String problem ="";
        long lastTimeStamp =0;
        double lastValue = 0;
        boolean firstCheck = true;

        AlertFactory alertFactory = new BloodOxygenAlertFactory();

        for (PatientRecord record : patient.getAllRecords()) {
            if (record.getRecordType().equals("Saturation")) {
                if (record.getMeasurementValue() < 92) {
                    problem = "BloodSaturationLow";
                    Alert alert = alertFactory.createAlert(patient.getPatientId(),
                            problem, record.getTimestamp());
                    alerts.add(alert);
                }
                if(!firstCheck) {
                    if ((lastValue-record.getMeasurementValue()) > 5 &&
                            record.getTimestamp() - lastTimeStamp < 600000) {

                        problem = "BloodSaturationDrop";
                        Alert alert = alertFactory.createAlert(patient.getPatientId(),
                                problem, record.getTimestamp());
                        alerts.add(alert);
                    }
                }

                else{
                    firstCheck = false;
                }
                lastValue = record.getMeasurementValue();
                lastTimeStamp = record.getTimestamp();
            }
        }

        return alerts;
    }
}