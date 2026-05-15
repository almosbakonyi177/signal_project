package alerts;

import static org.junit.jupiter.api.Assertions.*;

import com.alerts.Alert;
import com.alerts.alertFactory.*;
import com.alerts.alertStrategies.*;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * Responsible for testing only the alert factories.
 */
public class AlertStrategyTest {

    @Test
    public void testBloodPressureAlertStrategyForSystolicHigh() {
        AlertStrategy alertStrategy = new BloodPressureStrategy();
        Patient patient = new Patient(1);
        patient.addRecord(190,"SystolicPressure",1000L);

        ArrayList<Alert> alerts = alertStrategy.checkAlert(patient);

        assertEquals(1,alerts.size());
        assertEquals("CriticalHighSystolic",alerts.get(0).getCondition());
        assertEquals(BloodPressureAlert.class, alerts.get(0).getClass());
    }


    @Test
    public void testBloodPressureAlertStrategyForSystolicLow() {
        AlertStrategy alertStrategy = new BloodPressureStrategy();
        Patient patient = new Patient(1);
        patient.addRecord(40,"SystolicPressure",1000L);

        ArrayList<Alert> alerts = alertStrategy.checkAlert(patient);

        assertEquals(1,alerts.size());
        assertEquals("CriticalLowSystolic",alerts.get(0).getCondition());
        assertEquals(BloodPressureAlert.class, alerts.get(0).getClass());
    }


    @Test
    public void testBloodPressureAlertStrategyForDecreaseTrendSystolic() {
        AlertStrategy alertStrategy = new BloodPressureStrategy();
        Patient patient = new Patient(1);
        patient.addRecord(170,"SystolicPressure",1000L);
        patient.addRecord(150,"SystolicPressure",10000L);
        patient.addRecord(120,"SystolicPressure",20000L);
        patient.addRecord(100,"SystolicPressure",30000L);

        ArrayList<Alert> alerts = alertStrategy.checkAlert(patient);

        assertEquals(1,alerts.size());
        assertEquals("DecreaseTrendSystolic",alerts.get(0).getCondition());
        assertEquals(BloodPressureAlert.class, alerts.get(0).getClass());
    }


    @Test
    public void testBloodPressureAlertStrategyForIncreaseTrendSystolic() {
        AlertStrategy alertStrategy = new BloodPressureStrategy();
        Patient patient = new Patient(1);
        patient.addRecord(100,"SystolicPressure",1000L);
        patient.addRecord(120,"SystolicPressure",20000L);
        patient.addRecord(150,"SystolicPressure",30000L);
        patient.addRecord(170,"SystolicPressure",40000L);

        ArrayList<Alert> alerts = alertStrategy.checkAlert(patient);

        assertEquals(1,alerts.size());
        assertEquals("IncreaseTrendSystolic",alerts.get(0).getCondition());
        assertEquals(BloodPressureAlert.class, alerts.get(0).getClass());
    }


    @Test
    public void testBloodPressureAlertStrategyForDiastolicHigh() {
        AlertStrategy alertStrategy = new BloodPressureStrategy();
        Patient patient = new Patient(1);
        patient.addRecord(150,"DiastolicPressure",1000L);

        ArrayList<Alert> alerts = alertStrategy.checkAlert(patient);

        assertEquals(1,alerts.size());
        assertEquals("CriticalHighDiastolic",alerts.get(0).getCondition());
        assertEquals(BloodPressureAlert.class, alerts.get(0).getClass());
    }


    @Test
    public void testBloodPressureAlertStrategyForDiastolicLow() {
        AlertStrategy alertStrategy = new BloodPressureStrategy();
        Patient patient = new Patient(1);
        patient.addRecord(50,"DiastolicPressure",1000L);

        ArrayList<Alert> alerts = alertStrategy.checkAlert(patient);

        assertEquals(1,alerts.size());
        assertEquals("CriticalLowDiastolic",alerts.get(0).getCondition());
        assertEquals(BloodPressureAlert.class, alerts.get(0).getClass());
    }


    @Test
    public void testBloodPressureAlertStrategyForDecreaseTrendDiastolic() {
        AlertStrategy alertStrategy = new BloodPressureStrategy();
        Patient patient = new Patient(1);
        patient.addRecord(119,"DiastolicPressure",1000L);
        patient.addRecord(108,"DiastolicPressure",10000L);
        patient.addRecord(90,"DiastolicPressure",20000L);
        patient.addRecord(70,"DiastolicPressure",20000L);

        ArrayList<Alert> alerts = alertStrategy.checkAlert(patient);

        assertEquals(1,alerts.size());
        assertEquals("DecreaseTrendDiastolic",alerts.get(0).getCondition());
        assertEquals(BloodPressureAlert.class, alerts.get(0).getClass());
    }


    @Test
    public void testBloodPressureAlertStrategyForIncreaseTrendDiastolic() {
        AlertStrategy alertStrategy = new BloodPressureStrategy();
        Patient patient = new Patient(1);
        patient.addRecord(70,"DiastolicPressure",1000L);
        patient.addRecord(86,"DiastolicPressure",10000L);
        patient.addRecord(98,"DiastolicPressure",20000L);
        patient.addRecord(119,"DiastolicPressure",20000L);

        ArrayList<Alert> alerts = alertStrategy.checkAlert(patient);

        assertEquals(1,alerts.size());
        assertEquals("IncreaseTrendDiastolic",alerts.get(0).getCondition());
        assertEquals(BloodPressureAlert.class, alerts.get(0).getClass());
    }


    @Test
    public void testBloodPressureAlertStrategyNoAlerts() {
        AlertStrategy alertStrategy = new BloodPressureStrategy();
        Patient patient = new Patient(1);
        patient.addRecord(70,"DiastolicPressure",1000L);
        patient.addRecord(71,"DiastolicPressure",10000L);
        patient.addRecord(75,"DiastolicPressure",20000L);
        patient.addRecord(76,"DiastolicPressure",20000L);

        ArrayList<Alert> alerts = alertStrategy.checkAlert(patient);

        assertEquals(0,alerts.size());
    }


    @Test
    public void testBloodOxygenAlertStrategyHigh() {
        AlertStrategy alertStrategy = new OxygenSaturationStrategy();
        Patient patient = new Patient(1);
        patient.addRecord(90,"Saturation",1000L);

        ArrayList<Alert> alerts = alertStrategy.checkAlert(patient);

        assertEquals(1,alerts.size());
        assertEquals("BloodSaturationLow",alerts.get(0).getCondition());
        assertEquals(BloodOxygenAlert.class, alerts.get(0).getClass());
    }


    @Test
    public void testBloodOxygenAlertStrategyDrop() {
        AlertStrategy alertStrategy = new OxygenSaturationStrategy();
        Patient patient = new Patient(1);
        patient.addRecord(99,"Saturation",1000L);
        patient.addRecord(93,"Saturation",26000L);

        ArrayList<Alert> alerts = alertStrategy.checkAlert(patient);

        assertEquals(1,alerts.size());
        assertEquals("BloodSaturationDrop",alerts.get(0).getCondition());
        assertEquals(BloodOxygenAlert.class, alerts.get(0).getClass());
    }


    /**
     * Test if there is no alert, when there should not be any alert.
     */
    @Test
    public void testBloodOxygenAlertStrategyNoAlerts() {
        AlertStrategy alertStrategy = new OxygenSaturationStrategy();
        Patient patient = new Patient(1);
        patient.addRecord(99,"Saturation",1000L);
        patient.addRecord(96,"Saturation",26000L);

        ArrayList<Alert> alerts = alertStrategy.checkAlert(patient);

        assertEquals(0,alerts.size());
    }


    @Test
    public void testECGAlertStrategyPeak() {
        AlertStrategy alertStrategy = new ECGPeakStrategy();
        Patient patient = new Patient(1);
        patient.addRecord(50,"ECG",1000L);
        patient.addRecord(60,"ECG",26000L);
        patient.addRecord(150,"ECG",80000L);

        ArrayList<Alert> alerts = alertStrategy.checkAlert(patient);

        assertEquals(1,alerts.size());
        assertEquals("Peak",alerts.get(0).getCondition());
        assertEquals(ECGAlert.class, alerts.get(0).getClass());
    }


    @Test
    public void testECGAlertStrategyNoAlerts() {
        AlertStrategy alertStrategy = new ECGPeakStrategy();
        Patient patient = new Patient(1);
        patient.addRecord(50,"ECG",1000L);
        patient.addRecord(60,"ECG",26000L);
        patient.addRecord(50,"ECG",80000L);

        ArrayList<Alert> alerts = alertStrategy.checkAlert(patient);

        assertEquals(0,alerts.size());
    }


    @Test
    public void testTriggeredAlertStrategy() {
        AlertStrategy alertStrategy = new TriggeredAlertStrategy();
        Patient patient = new Patient(1);
        patient.addRecord(1,"Alert",1000L);

        ArrayList<Alert> alerts = alertStrategy.checkAlert(patient);

        assertEquals(1,alerts.size());
        assertEquals("Triggered",alerts.get(0).getCondition());
        assertEquals(TriggeredAlert.class, alerts.get(0).getClass());
    }
}