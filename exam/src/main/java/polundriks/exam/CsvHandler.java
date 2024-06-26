package polundriks.exam;

import com.opencsv.*;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CsvHandler {

    public static ArrayList<ArrayList<Object>> csvImport(File file, int startRow, int rowCount) throws IOException, CsvException {
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(file)).withCSVParser(parser).build()) {
            ArrayList<ArrayList<Object>> data = new ArrayList<>();
            List<String[]> rows = csvReader.readAll();
            rows.removeIf(row -> Objects.equals(row[0], ""));

            int endRow = Math.min(startRow + rowCount - 1, rows.size());
            for (int i = startRow - 1; i < endRow; i++) {
                ArrayList<Object> objectRow = new ArrayList<>();
                int t = 1;

                for (int j = 0; j < rows.get(i).length; j++) {
                    if (rows.get(i)[j].isBlank()) {
                        rows.get(i)[j] = rows.get(i)[j - t] + t;
                        t++;
                    } else {
                        t = 1;
                    }
                    objectRow.add(rows.get(i)[j]);
                }
                data.add(objectRow);
            }
            return data;
        }
    }

    public static void exportToCsv(ArrayList<ArrayList<Object>> objects, int startRow, int rowCount, String[] columnNames, File file) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
            if (columnNames != null && columnNames.length > 0) {
                writer.writeNext(columnNames);
                startRow++;
            }

            int endRow = Math.min(startRow + rowCount - 1, objects.size());
            for (int i = startRow - 1; i < endRow; i++) {
                ArrayList<Object> row = objects.get(i);
                String[] rowData = row.stream().map(Object::toString).toArray(String[]::new);
                writer.writeNext(rowData);
            }
        }
    }
}