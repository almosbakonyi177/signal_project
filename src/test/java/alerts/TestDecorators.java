package alerts;

import static org.junit.jupiter.api.Assertions.*;

import com.alerts.Alert;
import com.alerts.alertFactory.*;
import com.alerts.alertStrategies.*;
import com.alerts.decorators.AlertDecorator;
import com.alerts.decorators.PriorityAlertDecorator;
import com.alerts.decorators.RepeatedAlertDecorator;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TestDecorators {

    @Test
    void testPriorityAlertDecorator(){
        Alert alert = new BloodPressureAlert(100,"SystolicHigh",1000L);
        AlertDecorator decorator = new PriorityAlertDecorator(alert);
        assertEquals("SystolicHigh,Priority level",decorator.getCondition());
    }


    @Test
    void testRepeatedAlertDecorator(){
        Alert alert = new BloodPressureAlert(100,"SystolicHigh",1000L);
        AlertDecorator decorator = new RepeatedAlertDecorator(alert,1000L);
        assertEquals("SystolicHigh,repeating 1000 milliseconds",decorator.getCondition());
    }
}
