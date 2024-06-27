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
public class DoubleCell extends Cell {
    double content;

    public DoubleCell(String contentType, Object content) {
        super(contentType);
        this.content = (double) content;
    }
}
