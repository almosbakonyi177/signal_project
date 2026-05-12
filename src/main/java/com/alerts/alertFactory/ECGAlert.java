package com.alerts.alertFactory;

import com.alerts.Alert;

public class ECGAlert extends Alert {
    public ECGAlert(int patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp, "ECG");
    }
}
