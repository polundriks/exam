
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

    public static ArrayList<ArrayList<Object>> CsvImport(File file, int[] params) throws IOException, CsvException {

        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        CSVReader csvReader = new CSVReaderBuilder(new FileReader(file)).withCSVParser(parser).build();
        ArrayList<ArrayList<Object>> data = new ArrayList<>();
        List<String[]> rows = csvReader.readAll();
        while (Objects.equals(rows.get(0)[0], "")) {
            rows.remove(0);
        }
        for (int i = params[0] - 1, t = 1; i < params[1] + params[0] - 1 && i < rows.size(); i++) {
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

    public static void ExportToCsv(ArrayList<ArrayList<Object>> objects, int[] params, String[] columnNames, File file) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(file));
        for (int i = 0; i < params[0] - 1; i++) {
            objects.remove(0);
        }
        if (columnNames != null && columnNames.length > 0) {
            objects.add(0, new ArrayList<>(Arrays.asList(columnNames)));
        }
        for (int i = 0; i < params[1] && i < objects.size(); i++) {
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

