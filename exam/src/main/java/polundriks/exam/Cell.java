/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package polundriks.exam;

/**
 *
 * @author super
 */
import jakarta.xml.bind.annotation.XmlElement;

public abstract class Cell {
    @XmlElement(name = "type")
    String contentType;

    public Cell(String contentType) {
        this.contentType = contentType;
    }

    public Cell() {
    }
}