package com.alerts.decorators;

import com.alerts.Alert;


/**
 * Represents an alert decorator, which wraps the repeated alerts.
 * Extends the original alert condition with repetition information
 * without modifying the original alert object.
 */
public class RepeatedAlertDecorator extends AlertDecorator{
    long interval;

    /**
     * Creates a repeated alert decorator what wraps the repeated alerts
     * with repeated mark.
     * @param alert the alert to be decorated.
     * @param interval interval of alert repetition in milliseconds.
     */
    public RepeatedAlertDecorator(Alert alert, long interval) {
        super(alert);
        this.interval = interval;
    }

    /**
     * Returns the original alert's condition with "repeated"
     * mark and interval information added.
     * @return The decorated alert condition.
     */
    @Override
    public String getCondition(){
        return super.getCondition()+",repeating "+interval+" milliseconds";
    }
}
