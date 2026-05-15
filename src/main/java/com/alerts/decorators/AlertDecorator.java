package com.alerts.decorators;

import com.alerts.Alert;

public abstract class AlertDecorator implements Alert {
    private Alert alert;
    public AlertDecorator(Alert alert) {
        this.alert = alert;
    }

    /**
     * Retrieves the patient Id to who we link this alert.
     * @return patient Id to who we link this alert.
     */
    public int getPatientId() {
        return alert.getPatientId();
    }

    public String getCondition() {
        return alert.getCondition();
    }

    /**
     * Retrieves the type of this alert.
     * @return The type of this alert.
     */
    public String getType() {
        return alert.getType();
    }

    /**
     * Retrieves the time when the problem occurred.
     * @return The time when the problem occurred
     */
    public long getTimestamp() {
        return alert.getTimestamp();
    }
}
