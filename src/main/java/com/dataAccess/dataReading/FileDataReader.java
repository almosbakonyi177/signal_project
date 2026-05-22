package com.dataAccess.dataReading;

import java.io.File;
import java.io.IOException;

public interface FileDataReader{
    void readData(File file) throws IOException;
}
