package com.dataAccess;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import com.patientIdentification.HospitalPatient;
import com.patientIdentification.IdentityManager;
import com.patientIdentification.IncomingDataPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Receives the unified data from the Data Parser,
// sends it and integrate it into the storage.
public class DataSourceAdapter {
    private IdentityManager identityManager;


    public DataSourceAdapter(IdentityManager identityManager) {
        this.identityManager = identityManager;
    }

    public void integrateData(IncomingDataPoint incomingDataPoint) {
        PatientRecord record = new PatientRecord(incomingDataPoint.getPatientId(),
                incomingDataPoint.getMeasurementValue(), incomingDataPoint.getRecordType(),
                incomingDataPoint.getTimestamp());
        identityManager.addRecord(incomingDataPoint.getPatientId(), record);
    }

}
