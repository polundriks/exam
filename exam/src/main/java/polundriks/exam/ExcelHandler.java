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
import static org.apache.poi.ss.usermodel.CellType.*;

public class ExcelHandler {

    public static ArrayList<ArrayList<Object>> importFromExcel(File file, int sheetIndex, int startRow, int startColumn, int rowCount) throws IOException, InvalidFormatException {
        // Создание объекта книги
        try (XSSFWorkbook book = new XSSFWorkbook(file)) {
            ArrayList<ArrayList<Object>> data = new ArrayList<>();
            XSSFSheet sheet = book.getSheetAt(sheetIndex);

            for (int i = startRow - 1; i < rowCount + startRow - 1 && i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (row == null || row.getPhysicalNumberOfCells() == 0) continue;

                ArrayList<Object> objects = new ArrayList<>();
                int t = 1;

                for (int j = startColumn - 1; j < row.getLastCellNum(); j++) {
                    XSSFCell cell = row.getCell(j);
                    Object value = switch (cell.getCellType()) {
                        case STRING -> cell.getStringCellValue();
                        case NUMERIC -> cell.getNumericCellValue();
                        case BLANK -> objects.get(j - t).toString() + t++;
                        default -> null;
                    };
                    objects.add(value);
                }
                data.add(objects);
            }
            return data;
        }
    }

    public static void exportToExcel(ArrayList<ArrayList<Object>> objects, int startRow, int rowCount, int startColumn, int columnNameRow, String[] columnNames, File file) {
        try (XSSFWorkbook book = new XSSFWorkbook()) {
            XSSFSheet sheet = book.createSheet();

            if (columnNames != null && columnNames.length > 0) {
                XSSFRow row = sheet.createRow(columnNameRow - 1);
                for (int i = 0; i < columnNames.length; i++) {
                    XSSFCell cell = row.createCell(i + startColumn - 1);
                    cell.setCellValue(columnNames[i]);
                }
                startRow++;
            }

            for (int i = startRow - 1, r = startRow - 1; i < rowCount + startRow - 1 && i < objects.size(); i++, r++) {
                XSSFRow row = sheet.createRow(r);
                for (int j = 0; j < objects.get(i).size(); j++) {
                    row.createCell(j + startColumn - 1).setCellValue(objects.get(i).get(j).toString());
                }
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                book.write(fos);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}