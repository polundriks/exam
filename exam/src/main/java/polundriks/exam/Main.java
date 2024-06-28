/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package polundriks.exam;

/**
 *
 * @author super
 */
import Interface.Interface;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException, InvalidFormatException, CsvValidationException {
        Interface interfaceInstance = new Interface();
        interfaceInstance.initInterface();
    }
}

