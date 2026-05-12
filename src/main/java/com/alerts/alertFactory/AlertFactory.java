package com.alerts.alertFactory;

import com.alerts.Alert;

/**
 * Base class for alert factory.
 */
public abstract class AlertFactory {
    public abstract Alert createAlert(int patientId, String condition, long timestamp);
}