package cardio_generator;

import com.cardio_generator.generators.BloodLevelsDataGenerator;
import com.cardio_generator.generators.BloodPressureDataGenerator;
import com.cardio_generator.generators.BloodSaturationDataGenerator;
import com.cardio_generator.generators.ECGDataGenerator;
import com.cardio_generator.outputs.ConsoleOutputStrategy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardioGeneratorTest {

    @Test
    void BloodLevelsGeneratorTest() {
        BloodLevelsDataGenerator generator = new BloodLevelsDataGenerator(50);
        TestOutputStrategy output = new TestOutputStrategy();
        generator.generate(1,output);
        generator.generate(2,output);

        // 3 blood level measures each patient
        assertEquals(6, output.getHistory().size());
    }

    @Test
    void BloodPressureDataGeneratorTest() {
        BloodPressureDataGenerator generator = new BloodPressureDataGenerator(50);
        TestOutputStrategy output = new TestOutputStrategy();
        generator.generate(1,output);
        generator.generate(2,output);

        // 2 blood pressure type(Systolic, Diastolic) each patient
        assertEquals(4, output.getHistory().size());
    }


    @Test
    void SystolicDataGeneratorTest() {
        BloodPressureDataGenerator generator = new BloodPressureDataGenerator(50);
        double generatedData = generator.generateBloodPressureSystolic(1);

        // generated systolic blood pressure is in safe range
        assertTrue(generatedData > 90 && generatedData < 180);
    }


    @Test
    void DiastolicDataGeneratorTest() {
        BloodPressureDataGenerator generator = new BloodPressureDataGenerator(50);
        double generatedData = generator.generateBloodPressureDiastolic(1);

        // generated systolic blood pressure is in safe range
        assertTrue(generatedData > 60 && generatedData < 120);
    }


    @Test
    void BloodSaturationDataGeneratorTest() {
        BloodSaturationDataGenerator generator = new BloodSaturationDataGenerator(50);
        TestOutputStrategy output = new TestOutputStrategy();
        generator.generate(1,output);
        generator.generate(2,output);

        assertEquals(2, output.getHistory().size());
    }


    @Test
    void ECGDataGeneratorTest() {
        ECGDataGenerator generator = new ECGDataGenerator(50);
        TestOutputStrategy output = new TestOutputStrategy();
        generator.generate(1,output);
        generator.generate(2,output);

        assertEquals(2, output.getHistory().size());
    }
}
