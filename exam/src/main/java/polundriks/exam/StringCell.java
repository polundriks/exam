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
 * The {@code StringCell} class represents a cell in a table that contains string type data.
 */
@Data
public class StringCell extends Cell {
    /**
     * The string value stored in the cell.
     */
    String content;
    /**
     * Constructs a StringCell object with the specified content type and string value.
     *
     * @param contentType the content type of the cell
     * @param object      the string value to be stored in the cell
     */

    public StringCell(String contentType, Object object) {
        super(contentType);
        this.content = (String) object;
    }
}



