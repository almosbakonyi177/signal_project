package com.alerts;

/**
 * Represents an alert, which is associated to a patient.
 * Contains patientId, a detected condition, an alert type and
 * a timestamp when the alert was triggered.
 */
public interface Alert {
    /**
     * Retrieves the patient Id to who we link this alert.
     * @return patient Id to who we link this alert.
     */
    int getPatientId();

    /**
     * Retrieves the type of this alert, for example BloodPressure.
     * @return The type of this alert.
     */
    String getCondition();

    /**
     * Retrieves the time when the problem occurred.
     * @return The time when the problem occurred
     */
    String getType();

    /**
     * Retrieves the time in milliseconds when the alert was triggered.
     * @return Timestamp of this alert.
     */
    long getTimestamp();
}