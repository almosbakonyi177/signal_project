package com.alerts.alertFactory;

import com.alerts.Alert;

public class ECGAlertFactory extends AlertFactory {
    public Alert createAlert(int patientId, String condition, long timestamp) {
        return new ECGAlert(patientId, condition, timestamp);
    }
}