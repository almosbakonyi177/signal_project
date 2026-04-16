package com.cardio_generator.outputs;

/**
 * This interface represents the basis of outputs.
 * The implementations must define an output void function.
 *
 * @author Almos Bakonyi
 */
public interface OutputStrategy {
    /**
     * Creates an output for the given information. Saves, uploads or prints
     * the data.
     * @param patientId Integer of the ID of the patient, who is linked to the information.
     * @param timestamp Long of the timestamp of the given process.
     * @param label The variable name in the process/a status name.
     * @param data The value of the variable or status.
     */
    void output(int patientId, long timestamp, String label, String data);
}
