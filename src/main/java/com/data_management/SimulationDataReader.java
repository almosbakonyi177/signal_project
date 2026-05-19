package com.data_management;

import com.dataAccess.DataParser;
import com.dataAccess.DataSourceAdapter;
import com.patientIdentification.IncomingDataPoint;

import javax.swing.text.html.parser.Parser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class SimulationDataReader implements DataReader {

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
