package com.dataAccess;

import com.patientIdentification.IncomingDataPoint;

import java.util.List;

// Responsible for standardizing the raw input,
// and sends it to the DataSourceAdapter, which will integrate it.
public interface DataParser {
    /**
     * Parses the raw data and turns it into an incoming data points object.
     * @param rawData The incoming raw data in String.
     * @return A list of IncomingDataPoint objects, in which the raw patient(s)'
     * data is/are standardized.
     */
    List<IncomingDataPoint> parse(String rawData);
}
