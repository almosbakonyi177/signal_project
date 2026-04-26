package com.patientIdentification;

// Represents every incoming data from simulation.
public class IncomingDataPoint {
    private int simulatorPatientId;
    private double measurementValue;
    private String recordType;
    private long timestamp;

    public IncomingDataPoint(int simulatorPatientId, double measurementValue, String recordType
    , long timestamp) {
        this.simulatorPatientId = simulatorPatientId;
        this.measurementValue = measurementValue;
        this.recordType = recordType;
        this.timestamp = timestamp;
    }

    /**
     * Retrieves the hospital patient id of this record.
     * @return Id of this record's hospital patient.
     */
    public int getPatientId() {
        return simulatorPatientId;
    }

    /**
     * Retrieves the measurement value of this record.
     * @return Measurement value of this record.
     */
    public double getMeasurementValue() {
        return measurementValue;
    }

    /**
     * Retrieves the RecordType of this record.
     * @return Record Type of this record.
     */
    public String getRecordType() {
        return recordType;
    }

    /**
     * Retrieves the Timestamp of this record.
     * @return Timestamp of this record.
     */
    public long getTimestamp() {
        return timestamp;
    }
}
