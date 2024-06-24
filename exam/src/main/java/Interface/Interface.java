
package Interface;

/**
 *
 * @author super
 */
import com.opencsv.exceptions.CsvException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import polundriks.exam.CsvHandler;
import polundriks.exam.ExcelHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.opencsv.exceptions.CsvException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class Interface {
    private static JFrame frame;
    private static JPanel panel;
    private JTextField textField1;
    private JPanel panel1;
    private JComboBox comboBox1;
    private static JTable table = new JTable();
    private static DefaultTableModel tableModel;

    static ArrayList<ArrayList<Object>> objects;

    public static void main(String[] args) {
        frame = new JFrame("File Chooser Example");
        frame.setSize(650, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JButton excelButton = new JButton("Выбрать файл Excel");
        excelButton.setBounds(50, 30, 170, 25);
        panel.add(excelButton);

        JButton csvButton = new JButton("Выбрать файл CSV");
        csvButton.setBounds(240, 30, 150, 25);
        panel.add(csvButton);

        JButton exportExcelButton = new JButton("Экспорт в файл Excel");
        exportExcelButton.setBounds(50, 70, 170, 25);
        exportExcelButton.setEnabled(false);
        panel.add(exportExcelButton);

        JButton exportCsvButton = new JButton("Экспорт в файл CSV");
        exportCsvButton.setBounds(240, 70, 150, 25);
        exportCsvButton.setEnabled(false);
        panel.add(exportCsvButton);

        table.setBounds(50, 100, 450, 350);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 100, 550, 450);
        panel.add(scrollPane);

        excelButton.addActionListener(e -> {
            int[] params = new int[3];

            JFileChooser excelFileChooser = new JFileChooser(new File(".\\src\\main\\resources"));
            int returnValue = excelFileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = excelFileChooser.getSelectedFile();
                System.out.println("Выбран файл Excel: " + selectedFile.getName());

                // Дополнительное окно для выбора параметров Excel файла
                JFrame excelFrame = new JFrame("Параметры Excel файла");
                excelFrame.setSize(300, 200);
                JPanel excelPanel = new JPanel();
                excelFrame.add(excelPanel);

                JTextField sheetNumberField = new JTextField(10);
                JTextField startRowField = new JTextField(10);
                JTextField startColumnField = new JTextField(10);

                JLabel sheetNumberLabel = new JLabel("Номер листа:");
                JLabel startRowLabel = new JLabel("Начальная строка:");
                JLabel startColumnLabel = new JLabel("Начальный столбец:");

                excelPanel.add(sheetNumberLabel);
                excelPanel.add(sheetNumberField);
                excelPanel.add(startRowLabel);
                excelPanel.add(startRowField);
                excelPanel.add(startColumnLabel);
                excelPanel.add(startColumnField);

                JButton confirmButton = new JButton("Подтвердить");
                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        params[0] = (Integer.parseInt(sheetNumberField.getText()) - 1);
                        params[1] = (Integer.parseInt(startRowField.getText()));
                        params[2] = (Integer.parseInt(startColumnField.getText()));
                        excelFrame.dispose(); // Закрыть окно после подтверждения
                        try {
                            objects = ExcelHandler.importFromExcel(selectedFile, params);
                            showTable();
                            exportExcelButton.setEnabled(true);
                            exportCsvButton.setEnabled(true);
                        } catch (IOException | InvalidFormatException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
                excelPanel.add(confirmButton);
                excelFrame.setVisible(true);
            }


        });

        csvButton.addActionListener(e -> {
            int[] params = new int[3];
            JFileChooser csvFileChooser = new JFileChooser(new File(".\\src\\main\\resources"));
            int returnValue = csvFileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = csvFileChooser.getSelectedFile();

                // Дополнительное окно для выбора параметров Excel файла
                JFrame csvFrame = new JFrame("Параметры Csv файла");
                csvFrame.setSize(300, 200);
                JPanel csvPanel = new JPanel();
                csvFrame.add(csvPanel);

                JTextField startRowField = new JTextField(10);
                JTextField startRowAmountField = new JTextField(10);

                JLabel startRowLabel = new JLabel("Начальная строка:");
                JLabel startRowAmountLabel = new JLabel("Количество строк:");

                csvPanel.add(startRowLabel);
                csvPanel.add(startRowField);
                csvPanel.add(startRowAmountLabel);
                csvPanel.add(startRowAmountField);

                JButton confirmButton = new JButton("Подтвердить");
                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        params[0] = (Integer.parseInt(startRowField.getText()));
                        params[1] = (Integer.parseInt(startRowAmountField.getText()));
                        csvFrame.dispose(); // Закрыть окно после подтверждения
                        try {
                            objects = CsvHandler.CsvImport(selectedFile, params);
                            showTable();
                            exportExcelButton.setEnabled(true);
                            exportCsvButton.setEnabled(true);
                        } catch (IOException | CsvException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
                csvPanel.add(confirmButton);
                csvFrame.setVisible(true);
            }
        });

        exportExcelButton.addActionListener(e -> {
            ExcelHandler.exportToExcel(objects, new int[] {1,1,1,1});
        });

        exportCsvButton.addActionListener(e -> {
            try {
                CsvHandler.ExportToCsv(objects);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }



    private static void showTable() {

        Object[] columnNames = objects.get(0).toArray();
        Object[][] tableData = new Object[objects.size()-1][];

        for (int i = 1; i < objects.size(); i++) {
            ArrayList<Object> row = objects.get(i);
            tableData[i-1] = row.toArray(new Object[row.size()]);
        }

        tableModel = new DefaultTableModel(tableData, columnNames);
        table.setModel(tableModel);
    }
}
