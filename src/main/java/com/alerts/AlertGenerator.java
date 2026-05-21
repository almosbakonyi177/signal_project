package com.alerts;

import com.alerts.alertStrategies.AlertStrategy;
import com.alerts.decorators.AlertDecorator;
import com.alerts.decorators.RepeatedAlertDecorator;
import com.data_management.DataStorage;
import com.data_management.Patient;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    private AlertManager alertManager = new AlertManager();
    // Store checking methods in a list, therefore we can add a new checking method anytime
    // without a lot of work on the existing code
    private List<AlertStrategy> alertStrategies = new ArrayList<AlertStrategy>();

    private List<String> triggeredAlertsHistory = new ArrayList<>();
    private List<Alert> alertHistory = new ArrayList<>();
    private AlertDecorator  alertDecorator;


    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {

        // We go through on all checking methods
        for (AlertStrategy alertCondition : alertStrategies) {
            ArrayList<Alert> alerts = alertCondition.checkAlert(patient);
            // If there were alerts we go through on them and trigger the alerts
            if (!alerts.isEmpty()) {
                for (Alert alert : alerts) {
                    if(checkIfSimilarInHistory(alert)) {
                        alert=new RepeatedAlertDecorator(alert, alert.getTimestamp());
                    }
                    else{
                        addToTriggeredAlertsHistory(alert);
                        alertHistory.add(alert);
                    }
                    triggerAlert(alert);
                }
            }
        }

    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        alertManager.notifyStaff(alert);
        alertManager.uploadAlert(alert);
    }

    /**
     * Checks if there is a similar alert(similar condition and type)
     * in history like the given alert.
     * @param alert The alert for which want to check if there is a
     *              similar one already triggered in the past.
     * @return True, if there was a similar to given alert triggered in the past, otherwise false.
     */
    public boolean checkIfSimilarInHistory(Alert alert) {
        for(Alert alert1 : alertHistory){
            if(alert1.getPatientId()==alert.getPatientId()
            &&alert1.getType().equals(alert.getType())&&
                    alert1.getCondition().equals(alert.getCondition())) {

                return true;
            }
        }
        return false;
    }

    /**
     * Adds the alert to the triggered alerts history in String format.
     * @param alert The alert we want to add to the history.
     */
    public void addToTriggeredAlertsHistory(Alert alert) {
        triggeredAlertsHistory.add(alert.getPatientId()+","+
                alert.getTimestamp()+","+alert.getType()+","+alert.getCondition());
    }

    public List<String> getTriggeredAlertsHistory() {
        return triggeredAlertsHistory;
    }

    public void addAlertStrategy(AlertStrategy alertStrategy) {
        this.alertStrategies.add(alertStrategy);
    }

    /**
     * Returns the alert manager that this class uses.
     * @return the alert manager that this class uses.
     */
    public AlertManager getAlertManager() {
        return alertManager;
    }
}
