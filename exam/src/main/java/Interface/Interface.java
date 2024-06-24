
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
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
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
            int[] params = new int[4];

            JFileChooser excelFileChooser = new JFileChooser(new File(".\\src\\main\\resources"));
            int returnValue = excelFileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = excelFileChooser.getSelectedFile();

                JFrame excelFrame = new JFrame("Параметры Excel файла");
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
                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        params[0] = (Integer.parseInt(sheetNumberField.getText()) - 1);
                        params[1] = (Integer.parseInt(startRowField.getText()));
                        params[2] = (Integer.parseInt(startColumnField.getText()));
                        params[3] = (Integer.parseInt(allRowsField.getText()));
                        try {
                            objects = ExcelHandler.importFromExcel(selectedFile, params);
                            String[] columnHeaders = new String[objects.get(0).size()];
                            for (int i = 0; i < objects.get(0).size(); i++) {
                                columnHeaders[i] = objects.get(0).get(i).toString();
                            }
                            JList<String> list = new JList<>(columnHeaders);
                            JFrame listFrame = new JFrame("Параметры Excel файла");
                            listFrame.setSize(400, 300);
                            JPanel listPanel = new JPanel();
                            listFrame.add(listPanel);
                            JScrollPane listScrollPane = new JScrollPane(list);
                            listScrollPane.setBounds(50, 30, 200, 250);
                            listPanel.add(listScrollPane);
                            DefaultListModel<String> finalListModel = new DefaultListModel<>();
                            JList<String> finalList = new JList<>(finalListModel);
                            JScrollPane finalListScrollPane = new JScrollPane(finalList);
                            finalListScrollPane.setBounds(150, 30, 200, 250);
                            listPanel.add(finalListScrollPane);
                            JButton listButton = new JButton("Подтвердить");
                            listButton.setBounds(100, 250, 100, 25);
                            JButton addButton = new JButton("Добавить");
                            listButton.setBounds(250, 250, 100, 25);
                            listPanel.add(listButton);
                            listPanel.add(addButton);
                            addButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String selectedValue = list.getSelectedValue();
                                    finalListModel.addElement(selectedValue);
                                }
                            });
                            listFrame.setVisible(true);
                            listButton.addActionListener(new ActionListener() {
                                                             @Override
                                                             public void actionPerformed(ActionEvent e) {
                                                                 ArrayList<ArrayList<Object>> newObjects = new ArrayList<>();
                                                                 Object[] chosenHeaders = new Object[finalListModel.getSize()];
                                                                 for (int i = 0; i < finalListModel.getSize(); i++) {
                                                                     chosenHeaders[i] = finalListModel.getElementAt(i);
                                                                 }
                                                                 int[] chosenHeaderIndexes = new int[chosenHeaders.length];
                                                                 for (int i = 0; i < chosenHeaders.length; i++) {
                                                                     chosenHeaderIndexes[i] = objects.get(0).indexOf(chosenHeaders[i]);
                                                                 }
                                                                 System.out.println(chosenHeaderIndexes);
                                                                 for (int j = 0; j < objects.size(); j++) {
                                                                     ArrayList<Object> newRow = new ArrayList<>();
                                                                     for (int i : chosenHeaderIndexes) {
                                                                         newRow.add(objects.get(j).get(i));
                                                                     }
                                                                     newObjects.add(newRow);
                                                                 }
                                                                 objects = newObjects;
                                                                 showTable();
                                                                 exportExcelButton.setEnabled(true);
                                                                 exportCsvButton.setEnabled(true);
                                                                 listFrame.dispose();
                                                             }
                                                         }
                            );
                        } catch (IOException | InvalidFormatException ex) {
                            throw new RuntimeException(ex);
                        }
                        excelFrame.dispose();
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
                            int headersRow = 0;
                            if (objects.get(headersRow).get(0).toString().isBlank()) {
                                headersRow++;
                            }
                            String[] columnHeaders = new String[objects.get(headersRow).size()];
                            for (int i = 0; i < objects.get(headersRow).size(); i++) {
                                columnHeaders[i] = objects.get(headersRow).get(i).toString();
                            }
                            JList<String> list = new JList<>(columnHeaders);
                            JFrame listFrame = new JFrame("Параметры Excel файла");
                            listFrame.setSize(400, 300);
                            JPanel listPanel = new JPanel();
                            listFrame.add(listPanel);
                            JScrollPane listScrollPane = new JScrollPane(list);
                            listScrollPane.setBounds(50, 30, 200, 250);
                            listPanel.add(listScrollPane);
                            DefaultListModel<String> finalListModel = new DefaultListModel<>();
                            JList<String> finalList = new JList<>(finalListModel);
                            JScrollPane finalListScrollPane = new JScrollPane(finalList);
                            finalListScrollPane.setBounds(150, 30, 200, 250);
                            listPanel.add(finalListScrollPane);
                            JButton listButton = new JButton("Подтвердить");
                            listButton.setBounds(100, 250, 100, 25);
                            JButton addButton = new JButton("Добавить");
                            listButton.setBounds(250, 250, 100, 25);
                            listPanel.add(listButton);
                            listPanel.add(addButton);
                            addButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String selectedValue = list.getSelectedValue();
                                    finalListModel.addElement(selectedValue);
                                }
                            });
                            listFrame.setVisible(true);
                            listButton.addActionListener(new ActionListener() {
                                                             @Override
                                                             public void actionPerformed(ActionEvent e) {
                                                                 ArrayList<ArrayList<Object>> newObjects = new ArrayList<>();
                                                                 Object[] chosenHeaders = new Object[finalListModel.getSize()];
                                                                 for (int i = 0; i < finalListModel.getSize(); i++) {
                                                                     chosenHeaders[i] = finalListModel.getElementAt(i);
                                                                 }
                                                                 int[] chosenHeaderIndexes = new int[chosenHeaders.length];
                                                                 for (int i = 0; i < chosenHeaders.length; i++) {
                                                                     chosenHeaderIndexes[i] = objects.get(0).indexOf(chosenHeaders[i]);
                                                                 }
                                                                 System.out.println(chosenHeaderIndexes);
                                                                 for (int j = 0; j < objects.size(); j++) {
                                                                     ArrayList<Object> newRow = new ArrayList<>();
                                                                     for (int i : chosenHeaderIndexes) {
                                                                         newRow.add(objects.get(j).get(i));
                                                                     }
                                                                     newObjects.add(newRow);
                                                                 }
                                                                 objects = newObjects;
                                                                 showTable();
                                                                 listFrame.dispose();
                                                                 exportExcelButton.setEnabled(true);
                                                                 exportCsvButton.setEnabled(true);
                                                             }
                                                         }
                            );
                        } catch (IOException |
                                 CsvException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
                csvPanel.add(confirmButton);
                csvFrame.setVisible(true);
            }
        });

        exportExcelButton.addActionListener(e -> {
            int[] params = new int[4];

            JFrame excelExportFrame = new JFrame("Параметры Excel файла");
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

            chooseColumnNames.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame chooseColumnNames = new JFrame("Ввод названий столбцов");
                    chooseColumnNames.setSize(300, 200);
                    JPanel panel = new JPanel();
                    JLabel label = new JLabel("Введите названия колонок через точку с запятой:");
                    chooseColumnNames.add(panel);
                    JTextField columnNamesField = new JTextField(20);
                    JButton confirmButton = new JButton("Подтвердить");
                    panel.add(label);
                    panel.add(columnNamesField);
                    panel.add(confirmButton);
                    confirmButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String input = columnNamesField.getText();
                            columnNames = input.split(";");
                            chooseColumnNames.dispose();
                        }
                    });

                    chooseColumnNames.setVisible(true);
                }
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
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    params[0] = (Integer.parseInt(firstRowField.getText()));
                    params[1] = (Integer.parseInt(allRowsField.getText()));
                    params[2] = (Integer.parseInt(statRowField.getText()));
                    params[3] = (Integer.parseInt(startColumnField.getText()));
                    String[] columnHeaders = new String[objects.get(0).size()];
                    for (int i = 0; i < objects.get(0).size(); i++) {
                        columnHeaders[i] = objects.get(0).get(i).toString();
                    }
                    JList<String> list = new JList<>(columnHeaders);
                    JFrame listFrame = new JFrame("Параметры Excel файла");
                    listFrame.setSize(400, 300);
                    JPanel listPanel = new JPanel();
                    listFrame.add(listPanel);
                    JScrollPane listScrollPane = new JScrollPane(list);
                    listScrollPane.setBounds(50, 30, 200, 250);
                    listPanel.add(listScrollPane);
                    DefaultListModel<String> finalListModel = new DefaultListModel<>();
                    JList<String> finalList = new JList<>(finalListModel);
                    JScrollPane finalListScrollPane = new JScrollPane(finalList);
                    finalListScrollPane.setBounds(150, 30, 200, 250);
                    listPanel.add(finalListScrollPane);
                    JButton listButton = new JButton("Подтвердить");
                    listButton.setBounds(100, 250, 100, 25);
                    JButton addButton = new JButton("Добавить");
                    listButton.setBounds(250, 250, 100, 25);
                    listPanel.add(listButton);
                    listPanel.add(addButton);
                    addButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String selectedValue = list.getSelectedValue();
                            finalListModel.addElement(selectedValue);
                        }
                    });
                    listFrame.setVisible(true);
                    listButton.addActionListener(new ActionListener() {
                                                     @Override
                                                     public void actionPerformed(ActionEvent e) {
                                                         ArrayList<ArrayList<Object>> newObjects = new ArrayList<>();
                                                         Object[] chosenHeaders = new Object[finalListModel.getSize()];
                                                         for (int i = 0; i < finalListModel.getSize(); i++) {
                                                             chosenHeaders[i] = finalListModel.getElementAt(i);
                                                         }
                                                         int[] chosenHeaderIndexes = new int[chosenHeaders.length];
                                                         for (int i = 0; i < chosenHeaders.length; i++) {
                                                             chosenHeaderIndexes[i] = objects.get(0).indexOf(chosenHeaders[i]);
                                                         }
                                                         System.out.println(chosenHeaderIndexes);
                                                         for (int j = 0; j < objects.size(); j++) {
                                                             ArrayList<Object> newRow = new ArrayList<>();
                                                             for (int i : chosenHeaderIndexes) {
                                                                 newRow.add(objects.get(j).get(i));
                                                             }
                                                             newObjects.add(newRow);
                                                         }
                                                         objects = newObjects;
                                                         listFrame.dispose();
                                                         jFolderChooser.showDialog(null, "Выбрать папку сохранения");
                                                         File file = new File(jFolderChooser.getSelectedFile().getPath() + ".xlsx");
                                                         ExcelHandler.exportToExcel(objects, params, columnNames, file);
                                                         JOptionPane.showMessageDialog(frame, "Экспорт завершен успешно");
                                                     }
                                                 }
                    );
                    excelExportFrame.dispose();
                }
            });
            excelExportPanel.add(confirmButton);
            excelExportFrame.setVisible(true);
        });

        exportCsvButton.addActionListener(e -> {
            int[] params = new int[2];

            JFrame csvExportFrame = new JFrame("Параметры Csv файла");
            csvExportFrame.setSize(300, 200);
            JPanel csvExportPanel = new JPanel();
            csvExportFrame.add(csvExportPanel);

            JTextField startRowField = new JTextField(10);
            JTextField allRowsField = new JTextField(10);

            JLabel startRowLabel = new JLabel("Начальная строка:");
            JLabel allRowsLabel = new JLabel("Количество строк:");

            JButton chooseColumnNames = new JButton("Указать заголовки:");

            chooseColumnNames.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame chooseColumnNames = new JFrame("Ввод названий столбцов");
                    chooseColumnNames.setSize(300, 200);
                    JPanel panel = new JPanel();
                    JLabel label = new JLabel("Введите названия колонок через точку с запятой:");
                    chooseColumnNames.add(panel);
                    JTextField columnNamesField = new JTextField(20);
                    JButton confirmButton = new JButton("Подтвердить");
                    panel.add(label);
                    panel.add(columnNamesField);
                    panel.add(confirmButton);
                    confirmButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String input = columnNamesField.getText();
                            columnNames = input.split(";");
                            chooseColumnNames.dispose();
                        }
                    });
                    chooseColumnNames.setVisible(true);
                }
            });

            csvExportPanel.add(startRowLabel);
            csvExportPanel.add(startRowField);
            csvExportPanel.add(allRowsLabel);
            csvExportPanel.add(allRowsField);
            csvExportPanel.add(chooseColumnNames);

            JButton confirmButton = new JButton("Подтвердить");
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    params[0] = (Integer.parseInt(startRowField.getText()));
                    params[1] = (Integer.parseInt(allRowsField.getText()));
                    String[] columnHeaders = new String[objects.get(0).size()];
                    for (int i = 0; i < objects.get(0).size(); i++) {
                        columnHeaders[i] = objects.get(0).get(i).toString();
                    }
                    JList<String> list = new JList<>(columnHeaders);
                    JFrame listFrame = new JFrame("Параметры Excel файла");
                    listFrame.setSize(400, 300);
                    JPanel listPanel = new JPanel();
                    listFrame.add(listPanel);
                    JScrollPane listScrollPane = new JScrollPane(list);
                    listScrollPane.setBounds(50, 30, 200, 250);
                    listPanel.add(listScrollPane);
                    DefaultListModel<String> finalListModel = new DefaultListModel<>();
                    JList<String> finalList = new JList<>(finalListModel);
                    JScrollPane finalListScrollPane = new JScrollPane(finalList);
                    finalListScrollPane.setBounds(150, 30, 200, 250);
                    listPanel.add(finalListScrollPane);
                    JButton listButton = new JButton("Подтвердить");
                    listButton.setBounds(100, 250, 100, 25);
                    JButton addButton = new JButton("Добавить");
                    listButton.setBounds(250, 250, 100, 25);
                    listPanel.add(listButton);
                    listPanel.add(addButton);
                    addButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String selectedValue = list.getSelectedValue();
                            finalListModel.addElement(selectedValue);
                        }
                    });
                    listFrame.setVisible(true);
                    listButton.addActionListener(new ActionListener() {
                                                     @Override
                                                     public void actionPerformed(ActionEvent e) {
                                                         ArrayList<ArrayList<Object>> newObjects = new ArrayList<>();
                                                         Object[] chosenHeaders = new Object[finalListModel.getSize()];
                                                         for (int i = 0; i < finalListModel.getSize(); i++) {
                                                             chosenHeaders[i] = finalListModel.getElementAt(i);
                                                         }
                                                         int[] chosenHeaderIndexes = new int[chosenHeaders.length];
                                                         for (int i = 0; i < chosenHeaders.length; i++) {
                                                             chosenHeaderIndexes[i] = objects.get(0).indexOf(chosenHeaders[i]);
                                                         }
                                                         System.out.println(chosenHeaderIndexes);
                                                         for (int j = 0; j < objects.size(); j++) {
                                                             ArrayList<Object> newRow = new ArrayList<>();
                                                             for (int i : chosenHeaderIndexes) {
                                                                 newRow.add(objects.get(j).get(i));
                                                             }
                                                             newObjects.add(newRow);
                                                         }
                                                         objects = newObjects;
                                                         listFrame.dispose();
                                                         jFolderChooser.showDialog(null, "Выбрать папку сохранения");
                                                         File file = new File(jFolderChooser.getSelectedFile().getPath() + ".csv");
                                                         try {
                                                             CsvHandler.ExportToCsv(objects, params, columnNames, file);
                                                         } catch (IOException ex) {
                                                             throw new RuntimeException(ex);
                                                         }
                                                         JOptionPane.showMessageDialog(frame, "Экспорт завершен успешно");
                                                     }
                                                 }
                    );
                    csvExportFrame.dispose();
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