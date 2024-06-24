
package polundriks.exam;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author super
 */

public class CsvHandler {

    public static ArrayList<Object> CsvImport(String fileName) throws IOException, CsvValidationException {
        CSVReader csvReader = new CSVReaderBuilder(new FileReader(fileName)).build();
        ArrayList<Object> rows = new ArrayList<>();
        String[] nextLine;
        while ((nextLine = csvReader.readNext()) != null) {
            rows.addAll(Arrays.asList(nextLine));
        }
        return rows;
    }

    public static void ExportToCsv(ArrayList<ArrayList<Object>> objects){
        String csvFilePath = "csvExport.csv";
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
            for (ArrayList<Object> row : objects) {
                String[] rowData = new String[row.size()];
                for (int i = 0; i < row.size(); i++) {
                    rowData[i] = row.get(i).toString();
                }
                writer.writeNext(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


