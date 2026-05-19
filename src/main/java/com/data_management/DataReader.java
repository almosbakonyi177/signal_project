package com.data_management;

import java.io.File;
import java.io.IOException;

public interface DataReader {
    /**
     * Reads data from a specified source and stores it in the data storage.
     * 
     * @param file The file from where the data will be read.
     * @throws IOException if there is an error reading the data
     */
    void readData(File file) throws IOException;
}