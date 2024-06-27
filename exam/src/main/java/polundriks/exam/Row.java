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
@XmlAccessorType(XmlAccessType.FIELD)
public class Row {
    @XmlElementWrapper(name = "allCells")
    @XmlElement(name = "cell")
    ArrayList<Cell> cells = new ArrayList<>();
}
