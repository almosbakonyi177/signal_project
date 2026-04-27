package com.dataAccess;

import com.patientIdentification.IncomingDataPoint;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Responsible for listening for CSV file data, and ,while
 * the listening is active, looking for updates in the given file.
 */
public class    FileDataListener implements  DataListener {
    private String filePath;
    private boolean activeListening = false;
    private ExcelDataParser excelDataParser = new ExcelDataParser();
    private String lastData;
    private DataSourceAdapter dataSourceAdapter;
    private int processedLines =0;


    public FileDataListener(String filePath, DataSourceAdapter dataSourceAdapter) {
        this.filePath = filePath;
        lastData = "";
        this.dataSourceAdapter = dataSourceAdapter;
    }

    /**
     * Activates the listening. After activating,
     * it will look for newly added lines in the file.
     */
    public void startListening() {
        activeListening = true;
        readFile(filePath);
        // checkForUpdate();
    }

    /**
     * Checks for updates/new data lines in the file
     * in every 100 seconds if the listening is active.
     */
    /*public void checkForUpdate() {
        try {
            if (activeListening) {
                // Start checking after 100 secs
                TimeUnit.SECONDS.sleep(100);
                checkForUpdate();
            }
        }

        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

     */


    /**
     * Stops this listener to check for updates in the file.
     */
    public void stopListening() {
        activeListening = false;
    }

    /**
     Reads the given csv file and sends it to the ExcelDataParser, if there was new
     data since last checking.
     * @param filePath CSV file that will be sent to the ExcelData Parser upon new data.
     */
    public void readFile(String filePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            int processedLinesNow = 0;
            // We go through on all lines
            while ((line = br.readLine()) != null) {
                if (line.startsWith("patientId")){
                    continue; // We do not need the header
                }
                processedLinesNow++;
                if (processedLinesNow > processedLines){
                    stringBuilder.append(line).append("\n");
                }
            }
            br.close();


            if (processedLinesNow > processedLines) {
                lastData = lastData + stringBuilder;
                processedLines = processedLinesNow;
                // We only send it to the parser then if there is something new
                List<IncomingDataPoint> incomingDataPoints =
                        excelDataParser.parse(stringBuilder.toString());

                // We go through on all new data points and integrate them one by one
                // Because maybe there are two different new records for two different patient.
                for (IncomingDataPoint incomingDataPoint : incomingDataPoints) {
                    dataSourceAdapter.integrateData(incomingDataPoint);
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Resets the file path for this data listener.
     * @param filePath The new path of the file from which we want to read the data.
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}