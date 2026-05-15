package com.alerts.alertStrategies;

import com.alerts.Alert;
import com.alerts.alertFactory.AlertFactory;
import com.alerts.alertFactory.HypotensiveHypoxemiaAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for checking if a patient has both low blood pressure and
 * low blood saturation levels, therefore the patient is in danger of
 * Hypotensive Hypoxemia.
 */
public class HypotensiveHypoxemiaStrategy implements AlertStrategy {

    /**
     * Checks if the given patient is in danger of HypotensiveHypoxemia.
     * Checks if the patient's blood saturation level dropped below 92%
     * and the systolic blood pressure also dropped under 90 in 10 minutes.
     * @param patient The patient we would like to check if in danger of HypotensiveHypoxemia.
     * @return List of alerts that need to be triggered, if there was any,
     * otherwise an empty list.
     */
    @Override
    public ArrayList<Alert> checkAlert(Patient patient) {
        ArrayList<Alert> alerts = new ArrayList<>();
        List<Long> saturationTimeStamps = new ArrayList<>();
        List<Long> pressureTimeStamps = new ArrayList<>();

        AlertFactory alertFactory = new HypotensiveHypoxemiaAlertFactory();

        for (PatientRecord record : patient.getAllRecords()) {
            if (record.getRecordType().equals("Saturation")) {
                if (record.getMeasurementValue() < 92) {
                    saturationTimeStamps.add(record.getTimestamp());
                }
            }

            if(record.getRecordType().equals("SystolicPressure")){
                if (record.getMeasurementValue() < 90) {
                    pressureTimeStamps.add(record.getTimestamp());
                }
            }
        }

        // Below the moving window technique
        if(!(saturationTimeStamps.isEmpty()|| pressureTimeStamps.isEmpty())) {
            for (Long timeStamp : saturationTimeStamps) {
                for (Long timeStamp2 : pressureTimeStamps) {
                    if (Math.abs(timeStamp2-timeStamp)<300000) {

                        // Check which condition happened later
                        if (timeStamp2-timeStamp<0) {
                            Alert alert = alertFactory.createAlert(patient.getPatientId(),
                                    "danger", timeStamp);
                            alerts.add(alert);
                        }

                        else{
                            Alert alert = alertFactory.createAlert(patient.getPatientId(),
                                    "danger", timeStamp2);
                            alerts.add(alert);
                        }
                    }
                }
            }
        }

        return alerts;
    }
}
