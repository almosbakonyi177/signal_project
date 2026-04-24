package com.data_management;

// Represents one patient data query request
public class DataRequest {
    private int patientId;
    private int staffId;
    private boolean accessGiven;

    public DataRequest(int patientId, int staffId, boolean accessGiven) {
        this.patientId = patientId;
        this.staffId = staffId;
        this.accessGiven = accessGiven;
    }
}