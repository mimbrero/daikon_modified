package daikon.executionTimes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static daikon.executionTimes.FileManager.createFileIfNotExists;
import static daikon.executionTimes.FileManager.deleteFile;

public class CSVManager {

    public static void createCSVwithHeader(String path, String header) {
        deleteFile(path); // delete file if it exists
        createFileIfNotExists(path);
        writeCSVRow(path, header);
    }

    public static void writeCSVRow(String path, String row) {
        File csvFile = new File(path);
        try(FileOutputStream oCsvFile = new FileOutputStream(csvFile, true)) {
            row += "\n";
            oCsvFile.write(row.getBytes());
        } catch (IOException e) {
            System.err.println("The line could not be written to the CSV: " + path);
            System.err.println("Exception: " + e);
        }

    }

}
