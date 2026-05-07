package cardio_generator;

import com.cardio_generator.outputs.OutputStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an output strategy only for test purposes.
 */
public class TestOutputStrategy implements OutputStrategy {
    private List<String> history = new ArrayList<>();
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        history.add( ("Patient ID: "+patientId+", Timestamp: "+timestamp+
                ", Label: "+label+", Data: "+data));
    }
    public List<String> getHistory() {
        return history;
    }
}
