package com.dataAccess;

import java.util.ArrayList;
import com.patientIdentification.IncomingDataPoint;
import java.util.List;

/**
 * Responsible only for parsing raw CSV data.
 */
public class ExcelDataParser implements DataParser {

    /**
     * Parses the raw text from a CSV file.
     * @param rawData Raw text from a CSV file.
     * @return A list of IncomingDataPoint objects, in which the
     * raw data are standardized.
     */
    @Override
    public List<IncomingDataPoint> parse(String rawData) {
        try {
            String[] lines = rawData.split("\\r?\\n");
            List<IncomingDataPoint> incomingDataPoints = new ArrayList<>();
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                if (line.startsWith("patientId")) continue; // We do not need the header
                String parts[] = line.split(",");
                if (parts.length != 4) continue;
                int patientId = Integer.parseInt(parts[0]);
                double measurement = Double.parseDouble(parts[1]);
                String recordType = parts[2];
                long timestamp = Long.parseLong(parts[3]);
                IncomingDataPoint dataPoint =
                        new IncomingDataPoint(patientId, measurement, recordType, timestamp);
                incomingDataPoints.add(dataPoint);
            }
            return incomingDataPoints;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
