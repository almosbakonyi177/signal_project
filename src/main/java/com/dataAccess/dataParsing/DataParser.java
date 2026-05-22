package com.dataAccess.dataParsing;

import com.patientIdentification.IncomingDataPoint;

import java.util.List;

/**
 * Normalizes the raw string input, turns it into {@link IncomingDataPoint} objects,
 * and sends it to the DataSourceAdapter, which will integrate it.
 */
public interface DataParser {
    /**
     * Parses the raw data and turns it into {@link IncomingDataPoint} objects.
     * @param rawData The incoming raw data in String.
     * @return A list of {@link IncomingDataPoint} objects, in which the raw patient(s)'
     * data is/are standardized.
     */
    List<IncomingDataPoint> parse(String rawData);
}