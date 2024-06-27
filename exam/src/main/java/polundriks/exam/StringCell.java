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

@Data
public class StringCell extends Cell {
    String content;

    public StringCell(String contentType, Object object) {
        super(contentType);
        this.content = (String) object;
    }
}



