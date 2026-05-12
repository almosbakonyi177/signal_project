package com.alerts.alertFactory;

import com.alerts.Alert;

public class BloodPressureAlertFactory extends AlertFactory {
    @Override
    public Alert createAlert(int patientId, String condition, long timestamp) {
        return new BloodPressureAlert(patientId, condition, timestamp);
    }
}