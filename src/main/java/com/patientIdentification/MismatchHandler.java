package com.patientIdentification;

import java.util.List;
import java.util.Set;

/**
 * Responsible for handling mismatches during the incoming data and
 * hospital patient matching and match validation.
 */
public class MismatchHandler {
    private List<String> mismatchLog;
    private int year;
    private int month;
    private int day;

    public MismatchHandler(List<String> mismatchLog, int year, int month, int day) {
        this.mismatchLog = mismatchLog;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * Reports on the log if there was no match between hospital patients and simulation patient id.
     * @param simulatorPatientId Integer of patient Id that came from the simulation.
     */
    public void handleMismatch(int simulatorPatientId) {
        if (mismatchLog != null) {
            mismatchLog.add(Integer.toString(simulatorPatientId) +
                    "," + Integer.toString(year) + "," + Integer.toString(month) + "," + Integer.toString(day));
        }
    }

    public List<String> getMismatchLog() {
        return this.mismatchLog;
    }

    /**
     * Setter for the year.
     * @param year The current year.
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Setter for the month.
     * @param month The current month.
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Setter for the day.
     * @param day The current day.
     */
    public void setDay(int day) {
        this.day = day;
    }

}
