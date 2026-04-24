package com.data_management;

import javax.xml.crypto.Data;
import java.util.ArrayList;

// Notes and stores all the requests for patient data.
// Stores the staff member who requested the patient data, the patient Id,
// whose data was requested and if the access was given to the data.
public class AuditLogger {
    private ArrayList<DataRequest> requests;

    public AuditLogger() {
        this.requests = new ArrayList<>();
    }

    public ArrayList<DataRequest> getRequests() {
        return requests;
    }

    /**
     * Adds a data request to the data requests list.
     * @param patientId The patient's Id whose records were requested.
     * @param staffId The Id of staff member who requested the records.
     * @param accessGiven True if access was given, otherwise false.
     */
    public void addRequest(int patientId, int staffId, boolean accessGiven) {
        DataRequest request = new DataRequest(patientId, staffId, accessGiven);
        this.requests.add(request);
    }
}
