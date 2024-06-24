/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package polundriks.exam;

/**
 *
 * @author super
 */
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException, InvalidFormatException, CsvValidationException {
        System.out.println(ExcelImporter.importFromExcel(0, new File("C:\\var3\\main\\resources\\excel1.xlsx"), 1, 1));
        System.out.println(ExcelImporter.importFromExcel(0, new File("C:\\var3\\main\\resources\\excel2.xlsx"), 1, 1));
        System.out.println(CsvImporter.CsvImport("C:\\var3\\main\\resources\\csv1.csv"));
        System.out.println(CsvImporter.CsvImport("C:\\var3\\main\\resources\\csv2.csv"));
        ArrayList<ArrayList<Object>> data = new ArrayList<>();
        ArrayList<Object> innerList1 = new ArrayList<>();
        innerList1.add("data1");
        innerList1.add("data2");
        data.add(innerList1);
        ArrayList<Object> innerList2 = new ArrayList<>();
        innerList2.add(42);
        innerList2.add(true);
        data.add(innerList2);
        ExcelImporter.exportToExcel(data, 1,1 ,1, 1);
        CsvImporter.ExportToCsv(data);

    }
}
