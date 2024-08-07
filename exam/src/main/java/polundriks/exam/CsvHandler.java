package polundriks.exam;

import com.opencsv.*;
import com.opencsv.exceptions.CsvException;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The CsvHandler class provides methods for importing data from CSV files and exporting data to CSV files.
 */

public class CsvHandler {
    
    /**
     * Imports data from the specified CSV file.
     * @param startRow row from which import begins
     * @param rowCount amount of rows
     * @param file     the CSV file to import data from
     * @return an ArrayList containing the imported data as a list of lists
     * @throws IOException   if an I/O error occurs
     * @throws CsvException if an exception is encountered while parsing the CSV file
     */

    public static ArrayList<ArrayList<Object>> CsvImport(File file, int startRow, int rowCount) throws IOException, CsvException {
        String detectedCharset = null;
        try {
            InputStream in = new FileInputStream(file);
            Charset charset = detectCharset(in);
            detectedCharset = charset.displayName();
            System.out.println(detectedCharset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        CSVReader csvReader = new CSVReaderBuilder(new FileReader(file, Charset.forName(detectedCharset))).withCSVParser(parser).build();
        ArrayList<ArrayList<Object>> data = new ArrayList<>();
        List<String[]> rows = csvReader.readAll();
        while (Objects.equals(rows.get(0)[0], "")) {
            rows.remove(0);
        }
        for (int i = startRow - 1, t = 1; i < rowCount + startRow - 1 && i < rows.size(); i++) {
            ArrayList<Object> objectRow = new ArrayList<>();
            for (int j = 0; j < rows.get(i).length; j++) {
                if (rows.get(i)[j].isBlank()) {
                    rows.get(i)[j] = rows.get(i)[j - t] + t;
                    t++;
                }
            }
            t = 1;
            objectRow.addAll(Arrays.asList(rows.get(i)));
            data.add(objectRow);
        }
        System.out.println(data);
        return data;
    }
    
    /**
     * Detects the charset of the given input stream.
     *
     * @param in the input stream to detect the charset from
     * @return the charset detected from the input stream
     * @throws IOException if an I/O error occurs
     */

    private static Charset detectCharset(InputStream in) throws IOException {
        byte[] buf = new byte[4096];
        UniversalDetector detector = new UniversalDetector(null);

        int nread;
        while ((nread = in.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nread);
        }
        detector.dataEnd();

        String encoding = detector.getDetectedCharset();
        if (encoding == null) {
            return Charset.defaultCharset();
        } else {
            return Charset.forName(encoding);
        }
    }
    
    /**
     * Exports data to the specified CSV file.
     *
     * @param objects     the data to export as a list of lists
     * @param startRow    the starting row index for exporting
     * @param rowCount    the number of rows to export
     * @param columnNames the column names to include in the CSV file
     * @param file        the CSV file to export data to
     * @throws IOException if an I/O error occurs
     */
    
    public static void ExportToCsv(ArrayList<ArrayList<Object>> objects, int startRow, int rowCount, String[] columnNames, File file) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(file));
        for (int i = 0; i < startRow - 1; i++) {
            objects.remove(0);
        }
        if (columnNames != null && columnNames.length > 0) {
            objects.add(0, new ArrayList<>(Arrays.asList(columnNames)));
        }
        for (int i = 0; i < rowCount && i < objects.size(); i++) {
            ArrayList<Object> row = objects.get(i);
            String[] rowData = new String[row.size()];
            for (int j = 0; j < row.size(); j++) {
                rowData[j] = row.get(j).toString();
            }
            writer.writeNext(rowData);
        }
        writer.close();
    }
}