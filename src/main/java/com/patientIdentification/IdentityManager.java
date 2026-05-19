package com.patientIdentification;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Oversees the integrity, validates matches and handles mismatches between the incoming patient data
 * and hospital patient data. The coordinator of patient identification process.
 */
public class IdentityManager {
    private PatientIdentifier patientIdentifier;
    private Map<Integer, HospitalPatient> hospitalPatientMap;
    private DataStorage dataStorage;
    private MismatchHandler mismatchHandler;
    private PatientRecordComparison comparator;

    public IdentityManager(Map<Integer, HospitalPatient> hospitalPatientMap,
                           DataStorage dataStorage,
                           MismatchHandler mismatchHandler,
                           PatientIdentifier identifier) {

        this.hospitalPatientMap = hospitalPatientMap;
        this.patientIdentifier = identifier;

        this.dataStorage = dataStorage;
        this.mismatchHandler = mismatchHandler;
        this.comparator = new PatientRecordComparison();
    }


    /**
     * Makes hard copy of all the patients data to a hospital patient,
     * this helps to provide protection at data retrieval.
     */
    public void copyHospitalPatients() {
        for (Patient patient : dataStorage.getAllPatients()) {
            HospitalPatient hospitalPatient = new HospitalPatient(patient.getPatientId(),
                    copyPatientRecords(patient.getPatientId()));

            this.hospitalPatientMap.put(patient.getPatientId(), hospitalPatient);
        }
    }

    /**
     * Makes a hard copy of all records of the given patient.
     * @param patientId The patient of whose records we want to copy.
     * @return A list of hard copy patient records.
     */
    public List<PatientRecord> copyPatientRecords(int patientId) {
        List<PatientRecord> records = dataStorage.getPatientById(patientId).getAllRecords();
        List<PatientRecord> returner = new ArrayList<>();
        for (PatientRecord record : records) {
            PatientRecord record1=new PatientRecord(patientId, record.getMeasurementValue(),
                    record.getRecordType(),record.getTimestamp());
            returner.add(record1);
        }
        return returner;
    }


    /**
     * Decides if there is a hospital patient with the same id as the patient id
     * that we got from simulation.
     * @param simulatorPatientId The patient id that we got from simulation.
     * @return True if there exists a hospital patient with the same patient id
     * that it got from the simulation, otherwise false.
     */
    public boolean validateMatch(int simulatorPatientId) {
        // Make sure that we updated our short term use data storage
        copyHospitalPatients();

        if (patientIdentifier.findHospitalPatient(simulatorPatientId) != null) {
            return true;
        }
        mismatchHandler.handleMismatch(simulatorPatientId);
        return false;
    }

    /**
     * Returns the hospital patient with all their data if there was a match between the
     * simulation patient id and a real hospital patient id.
     * @param simulatorPatientId
     * @return
     */
    public HospitalPatient retrieveHospitalPatient(int simulatorPatientId) {
        if (validateMatch(simulatorPatientId)) {
            return hospitalPatientMap.get(simulatorPatientId);
        }
        return null;
    }

    /**
     * Adds the incoming data point to the hospital patient.
     * @param patientId The id of patient to whom the record will be linked.
     * @param patientRecord The incoming data point transformed to patient record.
     */
    public void addRecord(int patientId, PatientRecord patientRecord) {
        if (!validateMatch(patientId)) {
            // If there was no patient with this id, we document it and add that patient to the system
            dataStorage.addPatientData(patientId, patientRecord.getMeasurementValue(),
                    patientRecord.getRecordType(), patientRecord.getTimestamp());
        }

        // We add the incoming record to the original patient records.
        // We add only if it is not in already in the system.
        if (!(comparator.recordsContain(dataStorage.getPatientRecords(patientId),patientRecord))) {
            dataStorage.addPatientData(patientId, patientRecord.getMeasurementValue(),
                    patientRecord.getRecordType(), patientRecord.getTimestamp());
        }
    }
}
