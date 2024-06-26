/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package polundriks.exam;
/**
 *
 * @author super
 */

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import static org.apache.poi.ss.usermodel.CellType.BLANK;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

public class ExcelHandler {

    public static ArrayList<ArrayList<Object>> importFromExcel(File file, int[] params) throws IOException, InvalidFormatException {
        //создание объекта книги
        XSSFWorkbook book = new XSSFWorkbook(file);
        ArrayList<ArrayList<Object>> data = new ArrayList<>();
        XSSFSheet sheet = book.getSheetAt(params[0]);
        for (int i = params[1] - 1; i < params[3] + params[1] - 1 && i <= sheet.getLastRowNum(); i++) {
            ArrayList<Object> objects = new ArrayList<>();
            XSSFRow row = sheet.getRow(i);
            if (row != null && row.getPhysicalNumberOfCells() != 0) {
                for (int j = params[2] - 1, t = 1; j < row.getLastCellNum(); j++) {
                    Object object = new Object();
                    switch (row.getCell(j).getCellType()) {
                        case STRING -> {
                            object = row.getCell(j).getStringCellValue();
                            t = 1;
                        }
                        case NUMERIC -> {
                            object = row.getCell(j).getNumericCellValue();
                            t = 1;
                        }
                        case BLANK -> {
                            object = objects.get(j - t).toString() + t;
                            t++;
                        }
                    }
                    objects.add(object);
                }
                data.add(objects);
            }
        }
        System.out.println(data);
        return data;
    }

    public static void exportToExcel(ArrayList<ArrayList<Object>> objects, int[] params, String[] columnNames, File file) {
        XSSFWorkbook book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet();
        if (columnNames != null && columnNames.length > 0) {
            XSSFRow row = sheet.createRow(params[2] - 1);
            for (int i = 0; i < columnNames.length; i++) {
                XSSFCell cell = row.createCell(i + params[3] - 1);
                cell.setCellValue(columnNames[i]);
            }
            params[2] = params[2] + 1;
        }
        for (int i = params[0] - 1, r = params[2] - 1; i < params[1] + params[0] - 1 && i < objects.size(); i++, r++) {
            XSSFRow row = sheet.createRow(r);
            for (int j = 0; j < objects.get(i).size(); j++) {
                row.createCell(j + params[3] - 1).setCellValue(objects.get(i).get(j).toString());
            }
        }
        try {
            book.write(new FileOutputStream(file));
            book.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
