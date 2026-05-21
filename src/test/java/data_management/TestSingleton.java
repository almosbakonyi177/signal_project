package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
public class TestSingleton {

    @Test
    void firstPartTest(){
        DataStorage storage = DataStorage.getInstance();
        storage.clearStorage();
        storage.addPatientData(1,100,"Saturation",1000L);
        storage.addPatientData(1,95,"Saturation",11000L);
    }

    @Test
    void secondPartTest(){
        DataStorage storage = DataStorage.getInstance();
        assertEquals(2,storage.getPatientById(1).getAllRecords().size());
    }
}
