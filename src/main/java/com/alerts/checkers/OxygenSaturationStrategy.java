package com.alerts.checkers;

import com.alerts.Alert;
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
     * Checks
     * @param patient
     * @return List of alerts, if there was any, otherwise an empty list.
     */
    @Override
    public ArrayList<Alert> check(Patient patient) {
        ArrayList<Alert> alerts = new ArrayList<>();
        String problem ="";
        long lastTimeStamp =0;
        double lastValue = 0;
        boolean firstCheck = true;

        for (PatientRecord record : patient.getAllRecords()) {
            if (record.getRecordType().equals("Saturation")) {
                if (record.getMeasurementValue() < 92) {
                    problem = "BloodSaturationLow";
                    Alert alert = new Alert(patient.getPatientId(), problem,
                            record.getTimestamp(), "BloodSaturation");
                    alerts.add(alert);
                }
                if(!firstCheck) {
                    if ((lastValue-record.getMeasurementValue()) > 5 &&
                            record.getTimestamp() - lastTimeStamp < 600000) {

                        problem = "BloodSaturationDrop";
                        Alert alert = new Alert(patient.getPatientId(), problem,
                                record.getTimestamp(), "BloodSaturation");
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