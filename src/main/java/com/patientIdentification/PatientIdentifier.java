package com.patientIdentification;

import com.data_management.Patient;

import java.util.ArrayList;
import java.util.Map;

// Responsible for identifying the patient from the database data
// and link it to the right hospital patient.
public class PatientIdentifier {
    private Map<Integer, HospitalPatient> hospitalPatients;

    public PatientIdentifier(Map<Integer, HospitalPatient> hospital_patients) {
        this.hospitalPatients = hospital_patients;
    }

    /**
     *
     * @param patientId The hospital patient id for which we want to get the patient.
     * @return The hospital patient if exists a hospital patient with the given id.
     */
    public HospitalPatient findHospitalPatient(int patientId) {
        if (hospitalPatients.containsKey(patientId)) {
            return hospitalPatients.get(patientId);
        }
        return null;
    }
}
