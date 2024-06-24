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
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelImporter {

    public static ArrayList<Object> importFromExcel(int sheetNum, File file, int firstColumn, int firstString) throws IOException, InvalidFormatException {
        //создание объекта книги
        XSSFWorkbook book = new XSSFWorkbook(file);
        ArrayList<Object> groups = new ArrayList<>();
        XSSFSheet sheet = book.getSheetAt(sheetNum);
        //цикл по обработке строк страницы
        for (int i = firstString; i <= sheet.getLastRowNum(); i++) {
            ArrayList<Object> objects = new ArrayList<>();
            //строка эксель файла
            XSSFRow row = sheet.getRow(i);
            for (int j = firstColumn; j < row.getLastCellNum(); j++) {
                Object object;
                object = row.getCell(j);
                objects.add(object);
            }
            groups.add(objects);
        }
        return groups;
    }

    public static void exportToExcel(ArrayList<ArrayList<Object>> objects, int firstRow, int allRows, int leftRow, int leftColumn) {
        XSSFWorkbook book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet();
        for (int i = 0; i < objects.size(); i++){
            XSSFRow row = sheet.createRow(i);
            for (int j = 0; j < objects.get(i).size(); j++){
                row.createCell(j).setCellValue(objects.get(i).get(j).toString());
            }
        }
        File file = new File("excelExport.xlsx");
        try {
            book.write(new FileOutputStream(file));
            book.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}

