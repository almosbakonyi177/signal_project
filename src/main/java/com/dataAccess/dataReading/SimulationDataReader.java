package com.dataAccess.dataReading;

import com.dataAccess.DataSourceAdapter;
import com.dataAccess.dataParsing.DataParser;
import com.patientIdentification.IncomingDataPoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

/**
 * Reads the Simulation output from a given CSV file, using ExcelDataParser.
 */
public class SimulationDataReader implements FileDataReader {

    private DataSourceAdapter adapter;
    private DataParser parser;
    public SimulationDataReader(DataSourceAdapter adapter, DataParser parser) {
        this.adapter = adapter;
        this.parser = parser;
    }
    public void readData(File file) {
        String data="";
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line+"\n");
            }
            data = sb.toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        List<IncomingDataPoint> incomingDataPoints = parser.parse(data);
        for(IncomingDataPoint incomingDataPoint : incomingDataPoints){
            adapter.integrateData(incomingDataPoint);
        }
    }
}