package com.alerts.alertStrategies;

/**
 * Responsible for checking if measurement values exceeds
 * the given thresholds in the given direction.
 */
public class ValueChecker {
    /**
     * Checks if the given measurement value exceeds the threshold in the given direction.
     * @param measurementValue The measurement value we want to evaluate.
     * @param threshold The threshold value, which should not be crossed.
     * @param upperBound True if the given threshold is an upper bound, otherwise false.
     * @return True if the given threshold is exceeded in the given direction, otherwise false.
     */
    public boolean valueCheck(double measurementValue, double threshold, boolean upperBound) {
        if (threshold > measurementValue && !upperBound) {
            return true;
        }

        if (threshold < measurementValue && upperBound) {
            return true;
        }

        return false;
    }
}