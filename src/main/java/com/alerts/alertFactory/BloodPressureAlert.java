package com.alerts.alertFactory;

import com.alerts.Alert;

/**
 * Represents an alert in blood pressure category.
 */
public class BloodPressureAlert implements Alert {
    private int patientId;
    private String condition;
    private String alertType;
    private long timestamp;

    public BloodPressureAlert(int patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
        this.alertType = "BloodPressure";
    }

    /**
     * Retrieves the patient Id to who we link this alert.
     * @return patient Id to who we link this alert.
     */
    @Override
    public int getPatientId() {
        return patientId;
    }

    @Override
    public String getCondition() {
        return condition;
    }

    /**
     * Retrieves the type of this alert, for example BloodPressure.
     * @return The type of this alert.
     */
    public String getType() {
        return alertType;
    }

    /**
     * Retrieves the time when the problem occurred.
     * @return The time when the problem occurred
     */
    public long getTimestamp() {
        return timestamp;
    }
}