/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package polundriks.exam;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author super
 */

public class CsvImporter {

    public static ArrayList<Object> CsvImport(String fileName, int ) throws IOException, CsvValidationException {
        CSVReader csvReader = new CSVReaderBuilder(new FileReader(fileName)).build();
        ArrayList<Object> rows = new ArrayList<>();
        String[] nextLine;
        while ((nextLine = csvReader.readNext()) != null) {
            rows.addAll(Arrays.asList(nextLine));
        }
        return rows;
        

        
    }

}

