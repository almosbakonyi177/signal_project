package com.alerts;

import com.cardio_generator.outputs.ConsoleOutputStrategy;
import com.cardio_generator.outputs.TcpOutputStrategy;

//Dispatches alerts to the staff and uploads them to tcp server for easy tracking
public class AlertManager {
    private TcpOutputStrategy server = new TcpOutputStrategy(1);
    private ConsoleOutputStrategy console = new ConsoleOutputStrategy();


    /**
     * Notifies the staff for an alert by writing it on console.
     * @param alert 
     */
    public void notifyStaff(Alert alert) {
        console.output(alert.getPatientId(),alert.getTimestamp(),
                "threshold_exceed", alert.getCondition());
    }

    /**
     * Uploads the given alert to a tcp server.
     * @param alert
     */
    public void uploadAlert(Alert alert) {
        server.output(alert.getPatientId(),alert.getTimestamp(),
                "threshold_exceed", alert.getCondition());
    }
}
