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
 * The {@code IntCell} class represents a cell in a table that contains integer type data.
 */
@Data
public class IntCell extends Cell {
    /**
     * The integer value stored in the cell.
     */
    int content;
/**
     * Constructs an IntCell object with the specified content type and integer value.
     *
     * @param contentType the content type of the cell
     * @param content     the integer value to be stored in the cell
     */
    
    public IntCell(String contentType, Object content) {
        super(contentType);
        this.content = (int) content;
    }
}
