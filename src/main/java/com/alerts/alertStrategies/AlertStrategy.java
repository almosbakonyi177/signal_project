package com.alerts.alertStrategies;

import com.alerts.Alert;
import com.data_management.Patient;

import java.util.ArrayList;

public interface AlertStrategy {
    ArrayList<Alert> checkAlert(Patient patient);
}
