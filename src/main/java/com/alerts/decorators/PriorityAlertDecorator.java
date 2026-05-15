package com.alerts.decorators;

import com.alerts.Alert;

/**
 * Represents an alert decorator, which wraps the priority alerts.
 * Responsible for wrapping standard alerts with urgency mark. It modifies the
 * condition of the alert, adds a priority mark.
 */
public class PriorityAlertDecorator extends AlertDecorator{

    public PriorityAlertDecorator(Alert alert) {
        super(alert);
    }

    /**
     * Returns the original alert's condition with "Priority level"
     * mark.
     * @return The decorated alert condition.
     */
    @Override
    public String getCondition() {
        return super.getCondition()+",Priority level";
    }
}