package com.alerts.checkers;

import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for checking if a patient has both low blood pressure and
 * low blood saturation levels, therefore the patient is in danger of
 * Hypotensive Hypoxemia
 */
public class HypotensiveHypoxemiaChecker implements AlertCondition {

    /**
     * Checks if the given patient is in danger of HypotensiveHypoxemia.
     * Checks if the patient's blood saturation level dropped below 92%
     * and the systolic blood pressure also dropped under 90 in 10 minutes.
     * @param patient The patient we would like to check if in danger of HypotensiveHypoxemia.
     * @return List of alerts, if there was any, otherwise an empty list.
     */
    @Override
    public ArrayList<Alert> check(Patient patient) {
        ArrayList<Alert> alerts = new ArrayList<>();
        List<Long> saturationTimeStamps = new ArrayList<>();
        List<Long> pressureTimeStamps = new ArrayList<>();

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
        if(!(saturationTimeStamps.isEmpty()|| pressureTimeStamps.isEmpty())) {
            for (Long timeStamp : saturationTimeStamps) {
                for (Long timeStamp2 : pressureTimeStamps) {
                    if (Math.abs(timeStamp2-timeStamp)<300000) {

                        // We check which condition happened later
                        if (timeStamp2-timeStamp<0) {
                            Alert alert = new Alert(patient.getPatientId(), "danger",
                                    timeStamp, "HypotensiveHypoxemia");
                            alerts.add(alert);
                        }

                        else{
                            Alert alert = new Alert(patient.getPatientId(), "danger",
                                    timeStamp2, "HypotensiveHypoxemia");
                            alerts.add(alert);
                        }
                    }
                }
            }
        }

        return alerts;
    }
}
