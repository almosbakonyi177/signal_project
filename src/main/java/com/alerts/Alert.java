package com.alerts;

/**
 * Represents an alert, which shows the type and condition of a problem(High blood pressure etc),
 * links these data to the patient and time when it occurred.
 */
public abstract class Alert {
    private int patientId;
    private String condition;
    private String alertType;
    private long timestamp;

    public Alert(int patientId, String condition, long timestamp, String alertType) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
        this.alertType = alertType;
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
     * Retrieves the type of this alert.
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
