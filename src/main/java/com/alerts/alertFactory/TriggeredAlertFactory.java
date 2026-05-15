package com.alerts.alertFactory;

import com.alerts.Alert;

public class TriggeredAlertFactory implements  AlertFactory
{
    @Override
    public Alert createAlert(int patientId, String condition, long timestamp){
        return new TriggeredAlert(patientId, condition, timestamp);
    }
}