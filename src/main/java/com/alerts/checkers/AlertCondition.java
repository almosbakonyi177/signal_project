package com.alerts.checkers;

import com.alerts.Alert;
import com.data_management.Patient;

import java.util.ArrayList;

/**
 * Represents the basis for a condition checker method.
 * Any checker(Blood pressure checker, etc) will check if the measurement
 * meets the alert trigger requirements.
 */
public interface AlertStrategy {
    /** Checks if the patient meets any requirement to trigger an alert.
     * For example extremely low blood pressure.
     * @param patient
     * @return List of alerts that need to be triggered, if there was any,
     *      * otherwise an empty list.
     */
    ArrayList<Alert> check(Patient patient);
}
