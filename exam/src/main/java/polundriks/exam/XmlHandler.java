/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package polundriks.exam;

/**
 *
 * @author super
 */

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * The {@code XmlHandler} class provides methods for exporting data to XML files.
 */

public class XmlHandler {

     /**
     * Exports the provided data to an XML file.
     *
     * @param data the data to be exported as a list of lists of Objects
     * @param file the File object representing the destination XML file
     * @throws IOException   if an I/O error occurs
     * @throws JAXBException if an error occurs during XML marshalling
     */
    public void exportToXml(ArrayList<ArrayList<Object>> data, File file) throws IOException, JAXBException {
        Table table = new Table();
        for (ArrayList<Object> objectRow : data) {
            Row row = new Row();
            for (Object cellData : objectRow) {
                Cell cell = new StringCell("String", cellData.toString());
                try {
                    int i = Integer.parseInt(cellData.toString());
                    cell = new IntCell("Integer", i);
                } catch (NumberFormatException ignored){}
                try {
                    double d = Double.parseDouble(cellData.toString());
                    cell = new DoubleCell("Double", d);
                } catch (NumberFormatException ignored){}
                row.getCells().add(cell);
            }
            table.getRows().add(row);
        }
        StringWriter writer = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(Table.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(table, writer);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(writer.toString());
        fileWriter.close();
    }
}
