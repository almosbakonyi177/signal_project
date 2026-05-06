package alerts;

import static org.junit.jupiter.api.Assertions.*;

import com.alerts.*;
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
        patient.addRecord(100,"SystolicPressure", 1000);
        patient.addRecord(120,"SystolicPressure", 10000);
        patient.addRecord(131,"SystolicPressure", 20000);
        patient.addRecord(142,"SystolicPressure", 20100);
        BloodPressureChecker bloodPressureChecker = new BloodPressureChecker();
        ArrayList alerts = bloodPressureChecker.check(patient);
        assertEquals(alerts.size(), 1);
    }

    @Test
    public void testCriticalSystolic() {
        // Edge case, should happen more alert
        Patient patient = new Patient(1);
        patient.addRecord(100,"SystolicPressure", 1000);
        patient.addRecord(120,"SystolicPressure", 10000);
        patient.addRecord(185,"SystolicPressure", 20000);
        patient.addRecord(198,"SystolicPressure", 20100);
        BloodPressureChecker bloodPressureChecker = new BloodPressureChecker();
        ArrayList alerts = bloodPressureChecker.check(patient);
        assertEquals(alerts.size(), 3);
    }

    @Test
    public void testCriticalDiastolic() {
        Patient patient = new Patient(1);
        patient.addRecord(10,"DiastolicPressure", 1000);
        BloodPressureChecker bloodPressureChecker = new BloodPressureChecker();
        ArrayList alerts = bloodPressureChecker.check(patient);
        assertEquals(alerts.size(), 1);
    }

    @Test
    public void testSaturationDrop() {
        Patient patient = new Patient(1);
        patient.addRecord(100,"Saturation", 1000);
        patient.addRecord(94,"Saturation", 1000000);//One million
        patient.addRecord(100,"Saturation", 5000000);
        patient.addRecord(94,"Saturation", 5010000);
        BloodSaturationChecker bloodSaturationChecker = new BloodSaturationChecker();
        ArrayList alerts = bloodSaturationChecker.check(patient);
        assertEquals(alerts.size(), 1);
    }

    @Test
    public void testSaturationCriticalLow() {
        // Edge case, should happen more alert
        Patient patient = new Patient(1);
        patient.addRecord(100,"Saturation", 1000);
        patient.addRecord(90,"Saturation", 1000000);//One million
        patient.addRecord(100,"Saturation", 5000000);
        patient.addRecord(94,"Saturation", 5010000);
        BloodSaturationChecker bloodSaturationChecker = new BloodSaturationChecker();
        ArrayList alerts = bloodSaturationChecker.check(patient);
        assertEquals(alerts.size(), 2);
    }

    @Test
    public void testHypotensiveHypoxemia() {
        Patient patient = new Patient(1);
        patient.addRecord(89,"SystolicPressure", 1000);
        patient.addRecord(91,"Saturation", 20000);
        HypotensiveHypoxemiaChecker hypotensiveHypoxemiaChecker = new HypotensiveHypoxemiaChecker();
        ArrayList alerts = hypotensiveHypoxemiaChecker.check(patient);
        assertEquals(alerts.size(), 1);
    }

    @Test
    public void testECGPeak() {
        Patient patient = new Patient(1);
        patient.addRecord(89,"ECG", 1000);
        patient.addRecord(91,"ECG", 20000);
        patient.addRecord(100,"ECG", 2000000);//Million
        patient.addRecord(110,"ECG", 2010000);//Million
        patient.addRecord(180,"ECG", 2020000);//Million
        ECGPeakChecker ecgPeakChecker = new ECGPeakChecker();
        ArrayList alerts = ecgPeakChecker.check(patient);
        assertEquals(alerts.size(), 1);
    }
}