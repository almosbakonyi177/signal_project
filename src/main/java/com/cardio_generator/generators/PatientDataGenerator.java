package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * This interface represents the basis of Pantient
 * data generators. It defines the generate function,
 * which every  type of patient data generator needs
 * to implement.
 *
 * @author Almos Bakonyi
 */
public interface PatientDataGenerator {
    /**
     * Generates a type of data for a given patient. For example generates blood pressure data.
     * @param patientId Integer of patient ID, about which patient I generate the information.
     * @param outputStrategy The output strategy, which I would like to use to save the
     *                       generated, linked to the patient.
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
