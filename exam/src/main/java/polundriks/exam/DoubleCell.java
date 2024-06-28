/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package polundriks.exam;

/**
 *
 * @author super
 */
import lombok.Data;
/**
 * The {@code DoubleCell} class represents a cell in a table that contains double type data.
 */
@Data
public class DoubleCell extends Cell {
    double content;
/**
     * Constructs a DoubleCell object with the specified content type and double value.
     *
     * @param contentType the content type of the cell
     * @param content     the double value to be stored in the cell
     */
    public DoubleCell(String contentType, Object content) {
        super(contentType);
        this.content = (double) content;
    }
}
