package com.alerts;

import com.cardio_generator.outputs.ConsoleOutputStrategy;
import com.cardio_generator.outputs.TcpOutputStrategy;

/**
 * Manages alert delivery by notifying the staff members and uploading
 * the alerts to the chosen Tcp server.
 */
public class AlertManager {
    private TcpOutputStrategy server = new TcpOutputStrategy(1);
    private ConsoleOutputStrategy console = new ConsoleOutputStrategy();


    /**
     * Notifies the staff about the given alert through writing it on console.
     * @param alert the alert that needs to be printed on console.
     */
    public void notifyStaff(Alert alert) {
        console.output(alert.getPatientId(),alert.getTimestamp(),
                alert.getType(), alert.getCondition());
    }

    /**
     * Uploads the given alert to a tcp server.
     * @param alert The alert to be uploaded.
     */
    public void uploadAlert(Alert alert) {
        server.output(alert.getPatientId(),alert.getTimestamp(),
                alert.getType(), alert.getCondition());
    }
}