package com.dataAccess;

import com.data_management.PatientRecord;
import com.patientIdentification.IdentityManager;
import com.patientIdentification.IncomingDataPoint;

// Receives the unified/standardized data from the Data Parser
// and integrates it into the storage.
public class DataSourceAdapter {
    private IdentityManager identityManager;


    public DataSourceAdapter(IdentityManager identityManager) {
        this.identityManager = identityManager;
    }

    /**
     * Integrates the incoming data point to the main data storage.
     * @param incomingDataPoint One incoming record data.
     */
    public void integrateData(IncomingDataPoint incomingDataPoint) {
        PatientRecord record = new PatientRecord(incomingDataPoint.getPatientId(),
                incomingDataPoint.getMeasurementValue(), incomingDataPoint.getRecordType(),
                incomingDataPoint.getTimestamp());
        identityManager.addRecord(incomingDataPoint.getPatientId(), record);
    }

}
