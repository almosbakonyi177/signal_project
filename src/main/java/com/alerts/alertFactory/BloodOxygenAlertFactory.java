package com.alerts.alertFactory;

import com.alerts.Alert;

/**
 *
 */
public class BloodOxygenAlertFactory extends AlertFactory {
    @Override
    public Alert createAlert(int patientId, String condition, long timestamp){
        return new BloodOxygenAlert(patientId, condition, timestamp);
    }
}