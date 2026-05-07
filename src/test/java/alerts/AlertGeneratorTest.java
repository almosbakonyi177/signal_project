package alerts;

import static org.junit.jupiter.api.Assertions.*;

import com.alerts.*;
import com.data_management.DataStorage;
import org.junit.jupiter.api.Test;
import com.data_management.Patient;

import java.util.ArrayList;

/**
 * Responsible for testing the Alert Generation and handling module.
 */
public class AlertGeneratorTest {

    @Test
    public void testIncreaseSystolic() {
        Patient patient = new Patient(1);
        patient.addRecord(100,"SystolicPressure", 1000L);
        patient.addRecord(120,"SystolicPressure", 10000L);
        patient.addRecord(131,"SystolicPressure", 20000L);
        patient.addRecord(142,"SystolicPressure", 20100L);
        BloodPressureChecker bloodPressureChecker = new BloodPressureChecker();
        ArrayList alerts = bloodPressureChecker.check(patient);
        assertEquals(1, alerts.size()); // Check if there was only one alert
    }


    @Test
    public void testCriticalSystolic() {
        // Edge case, should happen more alert
        Patient patient = new Patient(1);
        patient.addRecord(100,"SystolicPressure", 1000L);
        patient.addRecord(120,"SystolicPressure", 10000L);
        patient.addRecord(185,"SystolicPressure", 20000L);
        patient.addRecord(198,"SystolicPressure", 20100L);
        BloodPressureChecker bloodPressureChecker = new BloodPressureChecker();
        ArrayList alerts = bloodPressureChecker.check(patient);
        assertEquals(3, alerts.size()); // Check if there were 3 alerts:
                                            // 1 for increasing trend, 2 for critical high value
    }


    @Test
    public void testCriticalDiastolic() {
        Patient patient = new Patient(1);
        patient.addRecord(10,"DiastolicPressure", 1000L);
        BloodPressureChecker bloodPressureChecker = new BloodPressureChecker();
        ArrayList alerts = bloodPressureChecker.check(patient);
        assertEquals(1, alerts.size()); // Check if there was one alert
    }


    @Test
    public void testSaturationDrop() {
        Patient patient = new Patient(1);
        patient.addRecord(100,"Saturation", 1000L);
        patient.addRecord(94,"Saturation", 1000000L);//One million
        // There was a more than 5% drop but passed more than 10 mins


        patient.addRecord(100,"Saturation", 5000000L);
        patient.addRecord(94,"Saturation", 5010000L);
        // There was a drop in ten mins

        BloodSaturationChecker bloodSaturationChecker = new BloodSaturationChecker();
        ArrayList alerts = bloodSaturationChecker.check(patient);

        assertEquals(1, alerts.size());
        // Check if there was one alert:
        // for more than 5% drop in 10 mins: only once in 10 mins
    }


    @Test
    public void testSaturationNoDrop() {
        Patient patient = new Patient(1);
        patient.addRecord(100,"Saturation", 1000L);
        patient.addRecord(94,"Saturation", 1000000L);//One million
        // There was a more than 5% drop but passed more than 10 mins

        BloodSaturationChecker bloodSaturationChecker = new BloodSaturationChecker();
        ArrayList alerts = bloodSaturationChecker.check(patient);

        assertEquals(0, alerts.size());
    }


    @Test
    public void testSaturationCriticalLow() {
        // Edge case, should happen more alert
        Patient patient = new Patient(1);
        patient.addRecord(100,"Saturation", 1000L);
        patient.addRecord(90,"Saturation", 1000000L);//One million
        patient.addRecord(100,"Saturation", 5000000L);
        patient.addRecord(94,"Saturation", 5010000L);
        BloodSaturationChecker bloodSaturationChecker = new BloodSaturationChecker();
        ArrayList alerts = bloodSaturationChecker.check(patient);
        assertEquals(2, alerts.size());
    }


    @Test
    public void testHypotensiveHypoxemia() {
        Patient patient = new Patient(1);
        patient.addRecord(89,"SystolicPressure", 1000L);
        patient.addRecord(91,"Saturation", 20000L);
        HypotensiveHypoxemiaChecker hypotensiveHypoxemiaChecker = new HypotensiveHypoxemiaChecker();
        ArrayList alerts = hypotensiveHypoxemiaChecker.check(patient);
        assertEquals(1, alerts.size());
    }


    @Test
    public void testECGPeak() {
        Patient patient = new Patient(1);
        patient.addRecord(89,"ECG", 1000L);
        patient.addRecord(91,"ECG", 20000L);
        patient.addRecord(100,"ECG", 2000000L);//Million
        patient.addRecord(110,"ECG", 2010000L);//Million
        patient.addRecord(180,"ECG", 2020000L);//Million
        ECGPeakChecker ecgPeakChecker = new ECGPeakChecker();
        ArrayList alerts = ecgPeakChecker.check(patient);
        assertEquals(1, alerts.size());
    }


    //Integration Test
    @Test
    void evaluationFirstTest() {
        DataStorage storage = new DataStorage();
        AlertGenerator alertGenerator = new AlertGenerator(storage);

        //First alert, drop in 10 mins
        storage.addPatientData(
                1,100,"Saturation", 1000L);
        storage.addPatientData(
                1, 94,"Saturation", 10000L);


        // Second alert, critical low saturation
        storage.addPatientData(
                1,91,"Saturation", 20000L);


        // Third alert, critical low Diastolic pressure
        storage.addPatientData(
                1,10,"DiastolicPressure", 2000000L);

        alertGenerator.addAlertCondition(new BloodSaturationChecker());
        alertGenerator.addAlertCondition(new BloodPressureChecker());
        alertGenerator.evaluateData(storage.getPatientById(1));

        // We first check the Saturation in the evaluation method.
        // The evaluation method does not sort all alerts by time yet,
        // Therefore alerts in a category will be ordered by time, for example
        // alerts in blood pressure or saturation, but not overall.


        assertEquals("1,10000,BloodSaturation,BloodSaturationDrop",
                alertGenerator.getAddToTriggeredAlertsHistory().get(0).toString());
        // First happened the saturation in the saturation changes
    }


    @Test
    void evaluationSecondTest() {
        DataStorage storage = new DataStorage();
        AlertGenerator alertGenerator = new AlertGenerator(storage);

        //First alert, HypotensiveHypoxemia
        storage.addPatientData(
                1,90,"Saturation", 1000L);
        storage.addPatientData(
                1, 89,"SystolicPressure", 10000L);


        // Second alert, ECG Peak
        storage.addPatientData(
                1,80,"ECG", 20000L);
        storage.addPatientData(
                1,90,"ECG", 22000L);
        storage.addPatientData(
                1,175,"ECG", 24000L);


        alertGenerator.addAlertCondition(new HypotensiveHypoxemiaChecker());
        alertGenerator.addAlertCondition(new ECGPeakChecker());
        alertGenerator.evaluateData(storage.getPatientById(1));

        assertEquals("1,10000,HypotensiveHypoxemia,danger",
                alertGenerator.getAddToTriggeredAlertsHistory().get(0));

        assertEquals("1,24000,ECG,Peak",
                alertGenerator.getAddToTriggeredAlertsHistory().get(1));
    }
}