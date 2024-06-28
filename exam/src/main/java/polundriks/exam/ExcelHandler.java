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

/**
 * The {@code ExcelHandler} class provides methods for importing data from Excel files and exporting data to Excel files.
 */

public class ExcelHandler {
    
    /**
     * Imports data from the specified Excel file.
     *
     * @param file       the Excel file to import data from
     * @param sheetIndex the index of the sheet to import data from
     * @param startRow   the starting row index from which to begin importing
     * @param startColumn the starting column index from which to begin importing
     * @param rowCount   the number of rows to import
     * @return an ArrayList containing the imported data as a list of lists of Objects
     * @throws IOException            if an I/O error occurs
     * @throws InvalidFormatException if the format of the Excel file is invalid
     */

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

     /**
     * Exports data to the specified Excel file.
     *
     * @param objects     the data to export as a list of lists of Objects
     * @param startRow    the starting row index for exporting
     * @param rowCount    the number of rows to export
     * @param exportRow   the row index to start exporting data in the Excel file
     * @param exportColumn the column index to start exporting data in the Excel file
     * @param columnNames an array of column names to include in the Excel file
     * @param file        the Excel file to export data to
     */
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