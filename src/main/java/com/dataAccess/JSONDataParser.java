package com.dataAccess;

import com.patientIdentification.IncomingDataPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for parsing raw String data from JSON file.
 */
public class JSONDataParser implements DataParser {

    /**
     * Parses the raw text from a JSON file and turns it into an incoming data point.
     * @param rawData Raw text from a JSON file.
     * @return A list of IncomingDataPoint objects, in which the
     * data are standardized.
     */
    @Override
    public List<IncomingDataPoint> parse(String rawData) {
        String[] lines = rawData.split("\\r?\\n");
        List<IncomingDataPoint> incomingDataPoints = new ArrayList<>();

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].isBlank()) {
                continue;
            }
            if (lines[i].trim().startsWith("{")) {
                int patientId=0;
                double MeasurementValue =0;
                String recordType="";
                long timestamp=0;
                boolean brokenBlock = false;

                try {
                    int counter = 1;
                    // We go through on one record block
                    for (int j = i + 1; j <= i + 4; j++) {
                        String line = lines[j].trim();
                        int colonIndex = line.indexOf(':');
                        String valuePart = line.substring(colonIndex + 1).trim();

                        if (line.endsWith(",")) {
                            valuePart = valuePart.substring(0, valuePart.length() - 1).trim();
                        }

                        // If there is broken data record we skip it
                        if(valuePart.isEmpty()){
                            brokenBlock = true;
                            break;
                        }

                        if (counter == 1) {
                            patientId = Integer.parseInt(valuePart);
                        }
                        if (counter == 2) {
                            MeasurementValue = Double.parseDouble(valuePart);
                        }
                        if (counter == 3) {
                            // We don't need the ""
                            if (valuePart.startsWith("\"")) {
                                recordType = valuePart.substring(1, valuePart.length() - 1).trim();
                            }
                        }
                        if (counter == 4) {
                            if (valuePart.contains("L")){
                                valuePart = valuePart.substring(0, valuePart.length() - 1).trim();
                            }
                            timestamp = Long.parseLong(valuePart);
                        }
                        counter++;
                    }
                    if(!brokenBlock) {
                        i = i + 4;
                    }
                    else{
                        // If there was a broken block, maybe only 3 lines,
                        // We don't jump 4, we search for the next(hopefully not broken)
                        // record's start
                        for(int j = i + 1; j <= i + 5 && j<lines.length; j++){
                            if(lines[j].trim().startsWith("{")){
                                i=j;
                            }
                        }
                    }

                    if(!brokenBlock){
                        IncomingDataPoint incomingDataPoint =
                                new IncomingDataPoint(
                                        patientId, MeasurementValue, recordType, timestamp);

                        incomingDataPoints.add(incomingDataPoint);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return incomingDataPoints;
    }
}
