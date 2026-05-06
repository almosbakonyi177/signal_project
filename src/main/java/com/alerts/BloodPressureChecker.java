package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

// Responsible for checking if the patients' blood pressure
// data meet the requirements to trigger an alert.
public class BloodPressureChecker implements AlertCondition {

    /**
     * Checks if the patient's blood pressure measurements are
     * in a healthy range.
     * @param patient The patient, for who we would like to check if
     *                the blood pressure is in healthy range.
     * @return List of alerts that need to be triggered, if there was any,
     * otherwise an empty list.
     */
    @Override
    public ArrayList<Alert> check(Patient patient) {
        ArrayList<Alert> alerts = new ArrayList<>();
        String problem ="";

        ValueChecker checker = new ValueChecker();
        boolean firstSystolic = true;
        boolean firstDiastolic = true;

        int increaseCounterSystolic = 0;
        int decreaseCounterSystolic = 0;

        int increaseCounterDiastolic = 0;
        int decreaseCounterDiastolic = 0;

        double lastDiastolicValue = 0;
        double lastSystolicValue = 0;

        for (PatientRecord patientRecord : patient.getAllRecords()) {
            if(patientRecord.getRecordType().equals("SystolicPressure")) {
                // First check if there is a critical outstanding
                if (checker.valueCheck(patientRecord.getMeasurementValue(),
                        180,true)) {
                    problem = "CriticalHighSystolic";
                    Alert alert = new Alert(patient.getPatientId(), problem,
                            patientRecord.getTimestamp(), "BloodPressure");
                    alerts.add(alert);
                }
                if (checker.valueCheck(patientRecord.getMeasurementValue(),90,false)) {
                    problem = "CriticalLowSystolic";
                    Alert alert = new Alert(patient.getPatientId(), problem,
                            patientRecord.getTimestamp(),  "BloodPressure");
                    alerts.add(alert);
                }

                // After critical value checking, we check if it is the first value
                // If this is the first value, and there is a big difference from
                // 0 we do not want to count it as a trend.
                if (firstSystolic) {
                    lastSystolicValue = patientRecord.getMeasurementValue();
                    firstSystolic = false;
                }

                // Check if there was increasing trend
                else if (lastSystolicValue-patientRecord.getMeasurementValue() < -10) {
                    increaseCounterSystolic++;
                }

                else if (lastSystolicValue-patientRecord.getMeasurementValue() > 10) {
                    decreaseCounterSystolic++;
                }

                // If there was neither increase or decrease we stop the trend
                // we start the counting again
                else {
                    increaseCounterSystolic=0;
                    decreaseCounterSystolic=0;
                }

                if (increaseCounterSystolic>2) {
                    problem = "IncreaseTrendSystolic";
                    Alert alert = new Alert(patient.getPatientId(), problem,
                            patientRecord.getTimestamp(), "BloodPressure");
                    alerts.add(alert);
                }

                if(decreaseCounterSystolic>2) {
                    problem = "DecreaseTrendSystolic";
                    Alert alert = new Alert(patient.getPatientId(), problem,
                            patientRecord.getTimestamp(),  "BloodPressure");
                    alerts.add(alert);
                }

                lastSystolicValue = patientRecord.getMeasurementValue();
            }

            else if(patientRecord.getRecordType().equals("DiastolicPressure")) {
                // First check if there is a critical outstanding
                if (checker.valueCheck(patientRecord.getMeasurementValue(),120,true)) {
                    problem = "CriticalHighDiastolic";
                    Alert alert = new Alert(patient.getPatientId(), problem,
                            patientRecord.getTimestamp(),  "BloodPressure");
                    alerts.add(alert);
                }
                if (checker.valueCheck(patientRecord.getMeasurementValue(),60,false)) {
                    problem = "CriticalLowDiastolic";
                    Alert alert = new Alert(patient.getPatientId(), problem,
                            patientRecord.getTimestamp(),   "BloodPressure");
                    alerts.add(alert);
                }

                // After critical value checking, we check if it is the first value
                // If this is the first value, and there is a big difference from
                // 0 we do not want to count it as a trend.
                if(firstDiastolic) {
                    lastDiastolicValue = patientRecord.getMeasurementValue();
                    firstDiastolic = false;
                }

                // Check if there was increasing trend
                else if (lastDiastolicValue-patientRecord.getMeasurementValue() < -10) {
                    increaseCounterDiastolic++;
                }

                else if (lastDiastolicValue-patientRecord.getMeasurementValue() > 10) {
                    decreaseCounterDiastolic++;
                }

                // If there was neither increase or decrease we stop the trend
                // and start the counting again
                else {
                    increaseCounterDiastolic=0;
                    decreaseCounterDiastolic=0;
                }

                if (increaseCounterDiastolic>2) {
                    problem = "IncreaseTrendDiastolic";
                    Alert alert = new Alert(patient.getPatientId(), problem,
                            patientRecord.getTimestamp(),"BloodPressure");
                    alerts.add(alert);
                }
                if(decreaseCounterDiastolic>2) {
                    problem = "DecreaseTrendDiastolic";
                    Alert alert = new Alert(patient.getPatientId(), problem,
                            patientRecord.getTimestamp(),"BloodPressure");
                    alerts.add(alert);
                }

                lastDiastolicValue = patientRecord.getMeasurementValue();
            }
        }
        return alerts;
    }
}