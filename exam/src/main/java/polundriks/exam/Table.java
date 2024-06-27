/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package polundriks.exam;

/**
 *
 * @author super
 */

import jakarta.xml.bind.annotation.*;
import lombok.Data;

import java.util.ArrayList;
@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Table {
    @XmlElementWrapper(name = "allRows")
    @XmlElement(name = "row")
    ArrayList<Row> rows = new ArrayList<>();
}
