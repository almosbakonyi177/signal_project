package com.alerts.alertFactory;

import com.alerts.Alert;

/**
 * Base class for alert factory.
 */
public interface AlertFactory {
    Alert createAlert(int patientId, String condition, long timestamp);
}