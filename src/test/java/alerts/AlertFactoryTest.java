package alerts;
import static org.junit.jupiter.api.Assertions.*;

import com.alerts.Alert;
import com.alerts.alertFactory.*;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * Responsible for testing only the alert factories.
 */
public class AlertFactoryTest {


    /**
     * Tests if the factory creates the correct alert instance.
     */
    @Test
    void testBloodPressureAlertFactory()
    {
        AlertFactory alertFactory = new BloodPressureAlertFactory();
        Alert alert = alertFactory.createAlert(1, "CritialLow",1000L);

        // check if a blood pressure alert was created
        assertEquals(BloodPressureAlert.class, alert.getClass());
        assertEquals("BloodPressure", alert.getType());
    }


    /**
     * Tests if the factory creates the correct alert instance.
     */
    @Test
    void testBloodOxygenAlertFactory()
    {
        AlertFactory alertFactory = new BloodOxygenAlertFactory();
        Alert alert = alertFactory.createAlert(1, "CritialLow",1000L);

        // check if a blood oxygen alert was created
        assertEquals(BloodOxygenAlert.class, alert.getClass());
        assertEquals("BloodOxygen", alert.getType());
    }


    /**
     * Tests if the factory creates the correct alert instance.
     */
    @Test
    void testECGAlertFactory()
    {
        AlertFactory alertFactory = new ECGAlertFactory();
        Alert alert = alertFactory.createAlert(1, "Peak",1000L);

        // check if an ECG alert was created
        assertEquals(ECGAlert.class, alert.getClass());
        assertEquals("ECG", alert.getType());
    }


    /**
     * Tests if the factory creates the correct alert instance.
     */
    @Test
    void testHypotensiveHypoxemiaAlertFactory()
    {
        AlertFactory alertFactory = new HypotensiveHypoxemiaAlertFactory();
        Alert alert = alertFactory.createAlert(1, "Peak",1000L);

        // check if an HypotensiveHypoxemia alert was created
        assertEquals(HypotensiveHypoxemiaAlert.class, alert.getClass());
        assertEquals("HypotensiveHypoxemia", alert.getType());
    }


    /**
     * Tests if the factory creates the correct alert instance.
     */
    @Test
    void testTriggeredAlertFactory()
    {
        AlertFactory alertFactory = new TriggeredAlertFactory();
        Alert alert = alertFactory.createAlert(1, "Triggered",1000L);

        // check if an HypotensiveHypoxemia alert was created
        assertEquals(TriggeredAlert.class, alert.getClass());
        assertEquals("TriggeredAlert", alert.getType());
    }
}
