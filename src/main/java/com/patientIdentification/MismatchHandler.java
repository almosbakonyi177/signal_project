package com.patientIdentification;

import java.util.List;
import java.util.Set;

/**
 * Responsible for handling mismatches during the incoming data and
 * hospital patient matching and match validation.
 */
public class MismatchHandler {
    private List<String> mismatchLog;
    private long timeStamp;

    public MismatchHandler(List<String> mismatchLog, long timeStamp) {
        this.mismatchLog = mismatchLog;
        this.timeStamp = timeStamp;
    }

    /**
     * Reports on the log if there was no match between hospital patients and simulation patient id.
     * @param simulatorPatientId Integer of patient Id that came from the simulation.
     */
    public void handleMismatch(int simulatorPatientId) {
        if (mismatchLog != null) {
            mismatchLog.add(Integer.toString(simulatorPatientId) +
                    "," + Long.toString(timeStamp));
        }
    }

    public List<String> getMismatchLog() {
        return this.mismatchLog;
    }

    public void setTime(long time) {
        this.timeStamp=time;
    }

}
