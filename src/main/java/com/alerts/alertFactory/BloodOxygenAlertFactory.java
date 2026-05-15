package com.alerts.alertFactory;

import com.alerts.Alert;

/**
 * Represents a factory that creates blood pressure alerts.
 * Instantiates {@link BloodOxygenAlert} objects based on given
 * patient id, condition and timestamp.
 */
public class BloodOxygenAlertFactory implements AlertFactory {
    @Override
    public Alert createAlert(int patientId, String condition, long timestamp){
        return new BloodOxygenAlert(patientId, condition, timestamp);
    }
}