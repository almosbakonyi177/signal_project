package com.data_management;

import java.util.ArrayList;
import java.util.List;

//Handles the patient data queries by medical staff
public class DataRetriever {
    private DataStorage dataStorage;
    private int minimumLevel;
    private AuditLogger auditLogger;

    public DataRetriever(DataStorage dataStorage, int minimumLevel) {
        this.dataStorage = dataStorage;
        this.minimumLevel = minimumLevel;
        // Each Data retriever should have its own audit logger
        this.auditLogger = new AuditLogger();
    }


    /**
     * Creates a query for all patient records for a patient.
     * @param staffMember The staff member object, who requested data.
     * @param patientId The id of the patient whose records are requested.
     * @return The hard copy of the requested patient's records,
     * only if the staff member has the correct role level to access.
     */
    public List<PatientRecord> makeQuery(StaffMember staffMember, int patientId) {

        // We check if the staff member is in the active member list
        if (!dataStorage.getStaffMembers().containsKey(staffMember)) {
            return null;
        }

        // We check if the staff member has the role level to access the data
        if (!validateAccess(staffMember)) {
            auditLogger.addRequest(patientId, staffMember.getId(), false);
            return null;
        }

        List<PatientRecord> patientRecords = new ArrayList<>();
        // We make a hard copy
        for (PatientRecord record : dataStorage.getPatientRecords(patientId)) {
            patientRecords.add(record);
        }
        // We add the data request to the requests list
        auditLogger.addRequest(patientId, staffMember.getId(), true);
        // Only return hard copy
        return patientRecords;
    }

    /**
     * Decides if a staff member has the correct role level to request patient data.
     * @param staffMember The staff member object, who requested data.
     * @return True, if the staff member can access to data, otherwise false.
     */
    public boolean validateAccess(StaffMember staffMember) {
        if (minimumLevel > staffMember.getRoleLevel()) {
            return false;
        }
        return true;
    }
}
