package com.patientIdentification;

import java.util.List;

/**
 * Responsible for handling mismatches during the incoming data and
 * hospital patient matching and match validation.
 */
public class MismatchHandler {
    private List<String> mismatchLog;
    long timeStamp = 0;

    public MismatchHandler(List<String> mismatchLog) {
        this.mismatchLog = mismatchLog;
    }

    /**
     * Reports on the log if there was no match between hospital patients and simulation patient id.
     * @param simulatorPatientId Integer of patient Id that came from the simulation.
     */
    public void handleMismatch(int simulatorPatientId) {
        timeStamp = System.currentTimeMillis();
        if (mismatchLog != null) {
            mismatchLog.add(Integer.toString(simulatorPatientId) +
                    "," + Long.toString(timeStamp));
        }
    }

    /**
     * Retrieves the mismatch log of this MismatchHandler.
     * @return The mismatch log of this MismatchHandler.
     */
    public List<String> getMismatchLog() {
        return this.mismatchLog;
    }

}
