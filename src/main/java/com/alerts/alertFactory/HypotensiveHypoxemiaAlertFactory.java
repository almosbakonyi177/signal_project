package com.alerts.alertFactory;

import com.alerts.Alert;

/**
 * Represents a factory that creates HypotensiveHypoxemia alerts.
 * Instantiates {@link HypotensiveHypoxemiaAlert} objects based on given
 * patient id, condition and timestamp.
 */
public class HypotensiveHypoxemiaAlertFactory implements AlertFactory{
    @Override
    public Alert createAlert(int patientId, String condition, long timestamp){
        return new HypotensiveHypoxemiaAlert(patientId, condition, timestamp);
    }
}