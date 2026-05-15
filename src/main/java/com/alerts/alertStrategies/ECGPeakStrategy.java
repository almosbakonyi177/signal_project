package com.alerts.alertStrategies;

import com.alerts.Alert;
import com.alerts.alertFactory.AlertFactory;
import com.alerts.alertFactory.BloodPressureAlertFactory;
import com.alerts.alertFactory.ECGAlertFactory;
import com.alerts.alertStrategies.AlertStrategy;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;

/**
 * Responsible for checking if a patient's ECG values are at peak in
 * a reasonable time interval.
 */
public class ECGPeakStrategy implements AlertStrategy {
    /**
     * Checks if the patient has an ECG peak value within one hour.
     * @param patient The id of patient whom data is being checked for ECG peaks.
     * @return List of alerts that need to be triggered, if there was any,
      * otherwise an empty list.
     */
    public ArrayList<Alert> checkAlert(Patient patient) {
        // The timestamp limit is the time interval tolerance
        // We do not want to make an alert if there was a big increase, but after many hours
        // We only alert if there was a big change in one hour max
        long timeStampLimit = 3600000;
        ArrayList<Long> ECGTimeStamps = new ArrayList<Long>();
        ArrayList<Double> ECGValues = new ArrayList<Double>();

        AlertFactory alertFactory = new ECGAlertFactory();

        ArrayList<Alert> alerts = new ArrayList<>();

        for (PatientRecord record : patient.getAllRecords()) {
            deleteOldValues(ECGValues, ECGTimeStamps, record.getTimestamp());

            if (record.getRecordType().equals("ECG")) {
                if (!ECGTimeStamps.isEmpty()) {
                    if (record.getTimestamp() - ECGTimeStamps.get(ECGTimeStamps.size() - 1)
                            > timeStampLimit) {

                        // If the last check was too late, we start a new moving window
                        ECGValues = new ArrayList<>();
                        ECGTimeStamps = new ArrayList<>();
                        ECGValues.add(record.getMeasurementValue());
                        ECGTimeStamps.add(record.getTimestamp());
                    } else {
                        if (checkIfPeak(calculateAverage(ECGValues), record.getMeasurementValue())) {

                            // Call the alert factory without knowing which exact alert
                            // Factory I'm calling to create the alert, I know it can create
                            // alert because I use interface, polymorphism
                            Alert alert = alertFactory.createAlert(patient.getPatientId(),
                                    "Peak", record.getTimestamp());
                            alerts.add(alert);
                        }
                    }
                }
                ECGValues.add(record.getMeasurementValue());
                ECGTimeStamps.add(record.getTimestamp());
            }
        }


        return alerts;
    }

    /**
     * Calculates an average for a given list of double values.
     * @param values A list of double values.
     * @return The average of the list of values.
     */
    private double calculateAverage(ArrayList<Double> values) {
        if (values.isEmpty()) {
            return 0;
        }
        int counter = 0;
        double sum = 0;
        for(Double value : values) {
            sum += value;
            counter++;
        }
        return sum / counter;
    }

    /**
     * Checks if the given current ECG value is a peak.
     * @return True if the given ECG value is at least 1.5 larger than average(so it is a peak),
     * otherwise false.
     */
    private boolean checkIfPeak(double average, double current) {
        // If there is a 1.5 larger ECG value than the average, it is a peak
        if (1.5*average < current) {
            return true;
        }
        return false;
    }

    /**
     * Checks if an ECG value is older than 6 hours, if yes, then delete the
     * value timestamp pair from the moving window.
     * @param values
     * @param timeStamps
     * @param currentTimeStamp
     */
    private void deleteOldValues(ArrayList<Double> values, ArrayList<Long> timeStamps,
                                 long currentTimeStamp) {

        for (int i = timeStamps.size()-1; i >=0 ; i--) {
            // We delete the
            if(currentTimeStamp - timeStamps.get(i) > 21600000) {
                values.remove(values.get(i));
                timeStamps.remove(timeStamps.get(i));
            }
        }
    }
}