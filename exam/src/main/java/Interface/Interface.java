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
import java.util.logging.Level;
import java.util.logging.Logger;

public class Interface {
    private static JFrame frame;
    private static JPanel panel;
    private JTextField textField1;
    private JPanel panel1;
    private JComboBox comboBox1;
    private static JTable table = new JTable();
    private static DefaultTableModel tableModel;
    private static final JFileChooser jFolderChooser = new JFileChooser();

    static ArrayList<ArrayList<Object>> objects;
    static String[] columnNames;

    public static void main(String[] args) {
        frame = new JFrame("File Chooser Example");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JButton excelButton = new JButton("Выбрать файл Excel");
        excelButton.setBounds(100, 20, 250, 25);
        panel.add(excelButton);

        JButton csvButton = new JButton("Выбрать файл CSV");
        csvButton.setBounds(450, 20, 250, 25);
        panel.add(csvButton);

        JButton exportExcelButton = new JButton("Экспорт в файл Excel");
        exportExcelButton.setBounds(100, 65, 250, 25);
        exportExcelButton.setEnabled(false);
        panel.add(exportExcelButton);

        JButton exportCsvButton = new JButton("Экспорт в файл CSV");
        exportCsvButton.setBounds(450, 65, 250, 25);
        exportCsvButton.setEnabled(false);
        panel.add(exportCsvButton);

        table.setBounds(50, 100, 700, 450);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 100, 700, 450);
        panel.add(scrollPane);

        excelButton.addActionListener(e -> {
            JFileChooser excelFileChooser = new JFileChooser(new File(".\\src\\main\\resources"));
            int returnValue = excelFileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = excelFileChooser.getSelectedFile();

                JDialog excelFrame = new JDialog(frame, "Параметры Excel файла", true);
                excelFrame.setSize(300, 200);
                JPanel excelPanel = new JPanel();
                excelFrame.add(excelPanel);

                JTextField sheetNumberField = new JTextField(10);
                JTextField startRowField = new JTextField(10);
                JTextField startColumnField = new JTextField(10);
                JTextField allRowsField = new JTextField(10);

                JLabel sheetNumberLabel = new JLabel("Номер листа:");
                JLabel startRowLabel = new JLabel("Начальная строка:");
                JLabel startColumnLabel = new JLabel("Начальный столбец:");
                JLabel allRowsLabel = new JLabel("Количество строк:");

                excelPanel.add(sheetNumberLabel);
                excelPanel.add(sheetNumberField);
                excelPanel.add(startRowLabel);
                excelPanel.add(startRowField);
                excelPanel.add(startColumnLabel);
                excelPanel.add(startColumnField);
                excelPanel.add(allRowsLabel);
                excelPanel.add(allRowsField);

JButton confirmButton = new JButton("Подтвердить");
                confirmButton.addActionListener(e1 -> {
                    try {
                        int sheetIndex = Integer.parseInt(sheetNumberField.getText()) - 1;
                        int startRow = Integer.parseInt(startRowField.getText());
                        int startColumn = Integer.parseInt(startColumnField.getText());
                        int rowCount = Integer.parseInt(allRowsField.getText());
                        objects = ExcelHandler.importFromExcel(selectedFile, sheetIndex, startRow, startColumn, rowCount);
                        showTable();
                        exportExcelButton.setEnabled(true);
                        exportCsvButton.setEnabled(true);
                        excelFrame.dispose();
                    } catch (IOException | InvalidFormatException ex) {
                        JOptionPane.showMessageDialog(excelFrame, "Некорректный файл");
                    } catch (NumberFormatException formatException) {
                        JOptionPane.showMessageDialog(excelFrame, "Неверный формат указанных данных");
                    } catch (IndexOutOfBoundsException | IllegalArgumentException argumentException) {
                        JOptionPane.showMessageDialog(excelFrame, "Введены некорректные данные");
                    }
                });
                excelPanel.add(confirmButton);
                excelFrame.setVisible(true);
            }
        });

        csvButton.addActionListener(e -> {
            JFileChooser csvFileChooser = new JFileChooser(new File(".\\src\\main\\resources"));
            int returnValue = csvFileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = csvFileChooser.getSelectedFile();

                JDialog csvFrame = new JDialog(frame, "Параметры Csv файла", true);
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
                confirmButton.addActionListener(e1 -> {
                    try {
                        int startRow = Integer.parseInt(startRowField.getText());
                        int rowCount = Integer.parseInt(startRowAmountField.getText());
                        objects = CsvHandler.csvImport(selectedFile, startRow, rowCount);
                        showTable();
                        exportExcelButton.setEnabled(true);
                        exportCsvButton.setEnabled(true);
                        csvFrame.dispose();
                    } catch (IOException | CsvException ex) {
                        JOptionPane.showMessageDialog(csvFrame, "Некорректный файл");
                    } catch (NumberFormatException formatException) {
                        JOptionPane.showMessageDialog(csvFrame, "Неверный формат указанных данных");
                    } catch (IndexOutOfBoundsException | IllegalArgumentException argumentException) {
                        JOptionPane.showMessageDialog(csvFrame, "Введены некорректные данные");
                    }
                });
                csvPanel.add(confirmButton);
                csvFrame.setVisible(true);
            }
        });


exportExcelButton.addActionListener(e -> {
            JDialog excelExportFrame = new JDialog(frame, "Параметры Excel файла", true);
            excelExportFrame.setSize(300, 200);
            JPanel excelExportPanel = new JPanel();
            excelExportFrame.add(excelExportPanel);

            JTextField firstRowField = new JTextField(10);
            JTextField allRowsField = new JTextField(10);
            JTextField statRowField = new JTextField(10);
            JTextField startColumnField = new JTextField(10);

            JLabel firstRowLabel = new JLabel("Начальная строка:");
            JLabel allRowsLabel = new JLabel("Количество строк:");
            JLabel startRowLabel = new JLabel("Точка начала (строка)");
            JLabel startColumnLabel = new JLabel("Точка начала (столбец)");

            JButton chooseColumnNames = new JButton("Указать заголовки:");

            chooseColumnNames.addActionListener(e1 -> {
                JDialog chooseColumnNamesDialog = new JDialog(excelExportFrame, "Ввод названий столбцов", true);
                chooseColumnNamesDialog.setSize(300, 200);
                JPanel chooseColumnNamesPanel = new JPanel();
                JLabel label = new JLabel("Введите названия колонок через точку с запятой:");
                chooseColumnNamesDialog.add(chooseColumnNamesPanel);
                JTextField columnNamesField = new JTextField(20);
                JButton confirmButton2 = new JButton("Подтвердить");
                chooseColumnNamesPanel.add(label);
                chooseColumnNamesPanel.add(columnNamesField);
                chooseColumnNamesPanel.add(confirmButton2);
                confirmButton2.addActionListener(e2 -> {
                    String input = columnNamesField.getText();
                    columnNames = input.split(";");
                    chooseColumnNamesDialog.dispose();
                });
                chooseColumnNamesDialog.setVisible(true);
            });

            excelExportPanel.add(firstRowLabel);
            excelExportPanel.add(firstRowField);
            excelExportPanel.add(allRowsLabel);
            excelExportPanel.add(allRowsField);
            excelExportPanel.add(startRowLabel);
            excelExportPanel.add(statRowField);
            excelExportPanel.add(startColumnLabel);
            excelExportPanel.add(startColumnField);
            excelExportPanel.add(chooseColumnNames);

            JButton confirmButton = new JButton("Подтвердить");
            confirmButton.addActionListener(e1 -> {
                try {
                    int startRow = Integer.parseInt(firstRowField.getText());
                    int rowCount = Integer.parseInt(allRowsField.getText());
                    int startRowPoint = Integer.parseInt(statRowField.getText());
                    int startColumn = Integer.parseInt(startColumnField.getText());
                    jFolderChooser.showDialog(null, "Выбрать папку сохранения");
                    File file = new File(jFolderChooser.getSelectedFile().getPath() + ".xlsx");
                    ExcelHandler.exportToExcel(objects, startRow, rowCount, startRowPoint, startColumn, columnNames, file);
                    JOptionPane.showMessageDialog(frame, "Экспорт завершен успешно");
                } catch (NumberFormatException formatException) {
                    JOptionPane.showMessageDialog(excelExportFrame, "Неверный формат указанных данных");
                } catch (IndexOutOfBoundsException | IllegalArgumentException argumentException) {
                    JOptionPane.showMessageDialog(excelExportFrame, "Введены некорректные данные");
                }
            });

            excelExportPanel.add(confirmButton);
            excelExportFrame.setVisible(true);
        });

        exportCsvButton.addActionListener(e -> {
            JDialog csvExportFrame = new JDialog(frame, "Параметры Csv файла", true);
            csvExportFrame.setSize(300, 200);
            JPanel csvExportPanel = new JPanel();
            csvExportFrame.add(csvExportPanel);


JTextField startRowField = new JTextField(10);
            JTextField allRowsField = new JTextField(10);

            JLabel startRowLabel = new JLabel("Начальная строка:");
            JLabel allRowsLabel = new JLabel("Количество строк:");

            JButton chooseColumnNames = new JButton("Указать заголовки:");

            chooseColumnNames.addActionListener(e1 -> {
                JDialog chooseColumnNamesDialog = new JDialog(csvExportFrame, "Ввод названий столбцов", true);
                chooseColumnNamesDialog.setSize(300, 200);
                JPanel chooseColumnNamesPanel = new JPanel();
                JLabel label = new JLabel("Введите названия колонок через точку с запятой:");
                chooseColumnNamesDialog.add(chooseColumnNamesPanel);
                JTextField columnNamesField = new JTextField(20);
                JButton confirmButton2 = new JButton("Подтвердить");
                chooseColumnNamesPanel.add(label);
                chooseColumnNamesPanel.add(columnNamesField);
                chooseColumnNamesPanel.add(confirmButton2);
                confirmButton2.addActionListener(e2 -> {
                    String input = columnNamesField.getText();
                    columnNames = input.split(";");
                    chooseColumnNamesDialog.dispose();
                });
                chooseColumnNamesDialog.setVisible(true);
            });

            csvExportPanel.add(startRowLabel);
            csvExportPanel.add(startRowField);
            csvExportPanel.add(allRowsLabel);
            csvExportPanel.add(allRowsField);
            csvExportPanel.add(chooseColumnNames);

            JButton confirmButton = new JButton("Подтвердить");
            confirmButton.addActionListener(e1 -> {
                try {
                    int startRow = Integer.parseInt(startRowField.getText());
                    int rowCount = Integer.parseInt(allRowsField.getText());
                    jFolderChooser.showDialog(null, "Выбрать папку сохранения");
                    File file = new File(jFolderChooser.getSelectedFile().getPath() + ".csv");
                    CsvHandler.exportToCsv(objects, startRow, rowCount, columnNames, file);
                    JOptionPane.showMessageDialog(frame, "Экспорт завершен успешно");
                } catch (NumberFormatException formatException) {
                    JOptionPane.showMessageDialog(csvExportFrame, "Неверный формат указанных данных");
                } catch (IndexOutOfBoundsException | IllegalArgumentException argumentException) {
                    JOptionPane.showMessageDialog(csvExportFrame, "Введены некорректные данные");
                } catch (IOException ex) {
                    Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            csvExportPanel.add(confirmButton);
            csvExportFrame.setVisible(true);
        });
    }

    private static void showTable() {
        Object[] columnNames = objects.get(0).toArray();
        Object[][] tableData = new Object[objects.size() - 1][];

        for (int i = 1; i < objects.size(); i++) {
            ArrayList<Object> row = objects.get(i);
            tableData[i - 1] = row.toArray(new Object[row.size()]);
        }

        tableModel = new DefaultTableModel(tableData, columnNames);
        table.setModel(tableModel);
    }
}