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
import static org.apache.poi.ss.usermodel.CellType.BLANK;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelHandler {

    public static ArrayList<ArrayList<Object>> importFromExcel(File file, int sheetIndex, int startRow, int startColumn, int rowCount) throws IOException, InvalidFormatException {
        XSSFWorkbook book = new XSSFWorkbook(file);
        ArrayList<ArrayList<Object>> data = new ArrayList<>();
        XSSFSheet sheet = book.getSheetAt(sheetIndex);
        for (int i = startRow - 1; i < rowCount + startRow - 1 && i <= sheet.getLastRowNum(); i++) {
            ArrayList<Object> objects = new ArrayList<>();
            XSSFRow row = sheet.getRow(i);
            if (row != null && row.getPhysicalNumberOfCells() != 0) {
                for (int j = startColumn - 1, t = 1; j < row.getLastCellNum(); j++) {
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
        return data;
    }

    public static void exportToExcel(ArrayList<ArrayList<Object>> objects, int startRow, int rowCount, int exportRow, int exportColumn, String[] columnNames, File file) {
        XSSFWorkbook book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet();
        if (columnNames != null && columnNames.length > 0) {
            XSSFRow row = sheet.createRow(exportRow - 1);
            for (int i = 0; i < columnNames.length; i++) {
                XSSFCell cell = row.createCell(i + exportColumn - 1);
                cell.setCellValue(columnNames[i]);
            }
            exportRow = exportRow + 1;
        }
        for (int i = startRow - 1, r = exportRow - 1; i < rowCount + startRow - 1 && i < objects.size(); i++, r++) {
            XSSFRow row = sheet.createRow(r);
            for (int j = 0; j < objects.get(i).size(); j++) {
                row.createCell(j + exportColumn - 1).setCellValue(objects.get(i).get(j).toString());
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