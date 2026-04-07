package com.cardio_generator.outputs;

// I corrected the ordering of the imports by 3.3.3 to be in ASCII order since '.' and before ';'
import java.util.concurrent.ConcurrentHashMap;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Paths;
import java.io.PrintWriter;
import java.nio.file.StandardOpenOption;

public class FileOutputStrategy implements OutputStrategy {

    // I corrected the name of the non-constant baseDirectory variable
    // to be written in lower case letters
    private String baseDirectory;

    public final ConcurrentHashMap<String, String> file_map = new ConcurrentHashMap<>();

    public FileOutputStrategy(String baseDirectory) {

        this.baseDirectory = baseDirectory;
    }

    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        // I wrapped the line below by principle 4.5.1, it was over 100 characters
        String FilePath = file_map.computeIfAbsent(label, k ->
                Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                // I wrapped the line below after the method name, the line was over 100 characters
                Files.newBufferedWriter(
                        Paths.get(FilePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            // I wrapped the line in parameter list, the line was over 100 characters
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n",
                        patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + FilePath + ": " + e.getMessage());
        }
    }
}