package com.patientIdentification;

// Represents every incoming data from simulation.
public class IncomingDataPoint {
    private int simulatorPatientId;
    private String measurementValue;
    private String recordType;
    private long timestamp;

    public IncomingDataPoint(int simulatorPatientId, String measurementValue, String recordType) {
        this.simulatorPatientId = simulatorPatientId;
        this.measurementValue = measurementValue;
        this.recordType = recordType;
        this.timestamp = System.currentTimeMillis();
    }
}
