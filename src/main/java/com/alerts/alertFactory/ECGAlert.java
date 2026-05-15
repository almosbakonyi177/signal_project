package com.alerts.alertFactory;

import com.alerts.Alert;

/**
 * Represents an alert triggered by ECG peak.
 */
public class ECGAlert implements Alert {
    private int patientId;
    private String condition;
    private String alertType;
    private long timestamp;

    public ECGAlert(int patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
        this.alertType = "ECG";
    }

    /**
     * Retrieves the patient Id to who we link this alert.
     * @return patient Id to who we link this alert.
     */
    public int getPatientId() {
        return patientId;
    }

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
