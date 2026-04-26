package com.patientIdentification;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Oversees the integrity, validates matches and handles mismatches between the incoming patient data
// and hospital patient data. The coordinator of patient identification process.
public class IdentityManager {
    private PatientIdentifier patientIdentifier;
    private List<String> mismatchLog;
    private Map<Integer, HospitalPatient> hospitalPatientMap;
    private int year;
    private int month;
    private int day;
    private DataStorage dataStorage;

    public IdentityManager(PatientIdentifier patientIdentifier,
                           int year, int month, int day, DataStorage dataStorage) {
        this.patientIdentifier = patientIdentifier;
        hospitalPatientMap = new HashMap<>();
        this.year = year;
        this.month = month;
        this.day = day;
        this.dataStorage = dataStorage;
        this.mismatchLog = new ArrayList<>();
    }


    public void copyHospitalPatients() {
        for (Patient patient : dataStorage.getAllPatients()) {
            HospitalPatient hospitalPatient = new HospitalPatient(patient.getPatientId(),
                    patient.getAllRecords(), patient.getAlertThresholds());

            this.hospitalPatientMap.put(patient.getPatientId(), hospitalPatient);
        }
    }


    /**
     * Decides if there is a hospital patient with the same id as the patient id
     * that we got from simulation.
     * @param simulatorPatientId The patient id that we got from simulation.
     * @return True if there exists a hospital patient with the same patient id
     * that it got from the simulation, otherwise false.
     */
    public boolean validateMatch(int simulatorPatientId) {
        if (patientIdentifier.findHospitalPatient(simulatorPatientId) != null) {
            return true;
        }
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
            if (patientIdentifier.findHospitalPatient(simulatorPatientId) != null) {
                return hospitalPatientMap.get(simulatorPatientId);
            }
        }
        return null;
    }

    /**
     * Reports on the log if there was no match between hospital patients and simulation patient id.
     * @param simulatorPatientId Integer of patient Id that came from the simulation.
     */
    public void handleMismatch(int simulatorPatientId) {
        mismatchLog.add(Integer.toString(simulatorPatientId)+
                ","+Integer.toString(year)+","+Integer.toString(month)+","+Integer.toString(day));
    }

    /**
     *
     * @param patientId
     * @param patientRecord
     */
    public void addRecord(int patientId, PatientRecord patientRecord) {
        if (!validateMatch(patientId)) {
            // If there was no patient with this id, we document it
            handleMismatch(patientId);
            return;
        }

        // We add the incoming record to the original patient records.
        if (!(dataStorage.getPatientRecords(patientId).contains(patientRecord))) {
            dataStorage.getPatientRecords(patientId).add(patientRecord);
        }
    }

    /**
     * Setter for the year.
     * @param year The current year.
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Setter for the month.
     * @param month The current month.
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Setter for the day.
     * @param day The current day.
     */
    public void setDay(int day) {
        this.day = day;
    }
}
