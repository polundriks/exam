package Interface;

import javax.swing.*;

import polundriks.exam.CsvHandler;
import polundriks.exam.ExcelHandler;
import polundriks.exam.XmlHandler;
import com.opencsv.exceptions.CsvException;
import jakarta.xml.bind.JAXBException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Interface {

    private static JFrame frame;
    private static JPanel panel;
//    private JTextField textField1;
//    private JPanel panel1;
//    private JComboBox comboBox1;
    private static final JTable table = new JTable();
    private static DefaultTableModel tableModel;
    private static final JFileChooser jFolderChooser = new JFileChooser();

    static ArrayList<ArrayList<Object>> objects;
    static String[] columnNames;
    static int sheetIndex;
    static int startRow;
    static int startColumn = 0;
    static int rowCount;
    static int exportRow;
    static int exportColumn;

//    public static void main(String[] args) {
//        frame = new JFrame("File Chooser Example");
//        frame.setSize(800, 700);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        panel = new JPanel();
//        frame.add(panel);
//        placeComponents(panel);
//
//        frame.setVisible(true);
//    }
    public void initInterface() {
        frame = new JFrame("File Chooser Example");
        frame.setSize(800, 700);
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

        JButton exportXmlButton = new JButton("Экспорт структуры в xml");
        exportXmlButton.setBounds(250, 600, 250, 25);
        panel.add(exportXmlButton);
        exportXmlButton.addActionListener(e -> {
            if (objects == null || objects.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Нечего экспортировать");
                return;
            }

            jFolderChooser.showDialog(frame, "Выбрать папку сохранения");
            File file = new File(jFolderChooser.getSelectedFile().getPath() + ".xml");
            try {
                new XmlHandler().exportToXml(objects, file);
                JOptionPane.showMessageDialog(frame, "Экспорт завершен успешно");
            } catch (IndexOutOfBoundsException exception) {
                JOptionPane.showMessageDialog(frame, "Ошибка экспорта");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (JAXBException ex) {
                throw new RuntimeException(ex);
            }

        });
        excelButton.addActionListener(e -> {

            JFileChooser excelFileChooser = new JFileChooser(new File(".\\src\\main\\resources"));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("", "xlsx");
            excelFileChooser.setFileFilter(filter);
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
                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            sheetIndex = (Integer.parseInt(sheetNumberField.getText()) - 1); //0
                            startRow = (Integer.parseInt(startRowField.getText())); //1
                            startColumn = (Integer.parseInt(startColumnField.getText()));  //2
                            rowCount = (Integer.parseInt(allRowsField.getText()));  //3
                            objects = ExcelHandler.importFromExcel(selectedFile, sheetIndex, startRow, startColumn, rowCount);
                            String[] columnHeaders = new String[objects.get(0).size()];
                            for (int i = 0; i < objects.get(0).size(); i++) {
                                columnHeaders[i] = "Столбец " + (startColumn + i);
                            }
                            JList<String> list = new JList<>(columnHeaders);
                            JDialog listFrame = new JDialog(excelFrame, "Параметры Excel файла", true);
                            listFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
                            listButton.addActionListener(e1 -> {
                                ArrayList<ArrayList<Object>> newObjects = new ArrayList<>();
                                int[] chosenHeaders = new int[finalListModel.getSize()];
                                for (int i = 0; i < finalListModel.getSize(); i++) {
                                    chosenHeaders[i] = (Integer.parseInt(finalListModel.getElementAt(i).substring(8)) - 1);
                                }
                                for (int j = 0; j < objects.size(); j++) {
                                    ArrayList<Object> newRow = new ArrayList<>();
                                    for (int i : chosenHeaders) {
                                        newRow.add(objects.get(j).get(i - startColumn + 1));
                                    }
                                    newObjects.add(newRow);
                                }
                                objects = newObjects;
                                showTable();
                                exportExcelButton.setEnabled(true);
                                exportCsvButton.setEnabled(true);
                                excelFrame.dispose();
                                listFrame.dispose();
                            }
                            );
                            listFrame.setVisible(true);
                        } catch (IOException | InvalidFormatException ex) {
                            JOptionPane.showMessageDialog(excelFrame, "Некорректный файл");
                        } catch (NumberFormatException formatException) {
                            JOptionPane.showMessageDialog(excelFrame, "Неверный формат указанных данных");
                        } catch (IndexOutOfBoundsException | IllegalArgumentException argumentException) {
                            JOptionPane.showMessageDialog(excelFrame, "Введены некорректные данные");
                        }
                        excelFrame.dispose();
                    }
                });
                excelPanel.add(confirmButton);
                excelFrame.setVisible(true);
            }

        });

        csvButton.addActionListener(e -> {
            JFileChooser csvFileChooser = new JFileChooser(new File(".\\src\\main\\resources"));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("", "csv");
            csvFileChooser.setFileFilter(filter);
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
                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            startRow = (Integer.parseInt(startRowField.getText()));
                            rowCount = (Integer.parseInt(startRowAmountField.getText()));
                            objects = CsvHandler.CsvImport(selectedFile, startRow, rowCount);
                            int headersRow = 0;
                            if (objects.get(headersRow).get(0).toString().isBlank()) {
                                headersRow++;
                            }
                            String[] columnHeaders = new String[objects.get(headersRow).size()];
                            for (int i = 0; i < objects.get(headersRow).size(); i++) {
                                columnHeaders[i] = "Столбец " + (i + 1);
                            }
                            JList<String> list = new JList<>(columnHeaders);
                            JDialog listFrame = new JDialog(csvFrame, "Параметры Excel файла", true);
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
                            listButton.addActionListener(e12 -> {
                                ArrayList<ArrayList<Object>> newObjects = new ArrayList<>();
                                int[] chosenHeaders = new int[finalListModel.getSize()];
                                for (int i = 0; i < finalListModel.getSize(); i++) {
                                    chosenHeaders[i] = (Integer.parseInt(finalListModel.getElementAt(i).substring(8)) - 1);
                                }
                                for (int j = 0; j < objects.size(); j++) {
                                    ArrayList<Object> newRow = new ArrayList<>();
                                    for (int i : chosenHeaders) {
                                        newRow.add(objects.get(j).get(i));
                                    }
                                    newObjects.add(newRow);
                                }
                                objects = newObjects;
                                showTable();
                                listFrame.dispose();
                                csvFrame.dispose();
                                exportExcelButton.setEnabled(true);
                                exportCsvButton.setEnabled(true);
                            }
                            );
                            listFrame.setVisible(true);
                        } catch (IOException | CsvException ex) {
                            JOptionPane.showMessageDialog(csvFrame, "Некорректный файл");
                        } catch (NumberFormatException formatException) {
                            JOptionPane.showMessageDialog(csvFrame, "Неверный формат указанных данных");
                        } catch (IndexOutOfBoundsException | IllegalArgumentException argumentException) {
                            JOptionPane.showMessageDialog(csvFrame, "Введены некорректные данные");
                        }
                    }
                });
                csvPanel.add(confirmButton);
                csvFrame.setVisible(true);
            }
        });

        exportExcelButton.addActionListener(e -> {
            int[] params = new int[4];

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

            chooseColumnNames.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JDialog chooseColumnNames = new JDialog(excelExportFrame, "Ввод названий столбцов", true);
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
            excelExportPanel.add(confirmButton);
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        startRow = (Integer.parseInt(firstRowField.getText()));
                        rowCount = (Integer.parseInt(allRowsField.getText()));
                        exportRow = (Integer.parseInt(statRowField.getText()));
                        exportColumn = (Integer.parseInt(startColumnField.getText()));
                        String[] columnHeaders = new String[objects.get(0).size()];
                        for (int i = 0; i < objects.get(0).size(); i++) {
                            columnHeaders[i] = "Столбец " + (startColumn + i);
                        }
                        JList<String> list = new JList<>(columnHeaders);
                        JDialog listFrame = new JDialog(excelExportFrame, "Параметры Excel файла", true);
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
                        listButton.addActionListener(e13 -> {
                            ArrayList<ArrayList<Object>> newObjects = new ArrayList<>();
                            int[] chosenHeaders = new int[finalListModel.getSize()];
                            for (int i = 0; i < finalListModel.getSize(); i++) {
                                chosenHeaders[i] = (Integer.parseInt(finalListModel.getElementAt(i).substring(8)) - 1);
                            }
                            for (int j = 0; j < objects.size(); j++) {
                                ArrayList<Object> newRow = new ArrayList<>();
                                for (int i : chosenHeaders) {
                                    newRow.add(objects.get(j).get(i - startColumn + 1));
                                }
                                newObjects.add(newRow);
                            }
                            objects = newObjects;
                            listFrame.dispose();
                            jFolderChooser.showDialog(null, "Выбрать папку сохранения");
                            File file = new File(jFolderChooser.getSelectedFile().getPath() + ".xlsx");
                            ExcelHandler.exportToExcel(objects, startRow, rowCount, exportRow, exportColumn, columnNames, file);
                            JOptionPane.showMessageDialog(frame, "Экспорт завершен успешно");
                        }
                        );
                        excelExportPanel.add(confirmButton);
                        listFrame.setVisible(true);
                        excelExportFrame.dispose();
                    } catch (NumberFormatException formatException) {
                        JOptionPane.showMessageDialog(excelExportFrame, "Неверный формат указанных данных");
                    } catch (IndexOutOfBoundsException | IllegalArgumentException argumentException) {
                        JOptionPane.showMessageDialog(excelExportFrame, "Введены некорректные данные");
                    }
                }

            });

            excelExportFrame.setVisible(true);
        });

        exportCsvButton.addActionListener(e -> {
            int[] params = new int[2];

            JDialog csvExportFrame = new JDialog(frame, "Параметры Csv файла", true);
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
                    JDialog chooseColumnNames = new JDialog(csvExportFrame, "Ввод названий столбцов", true);
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
                    try {
                        startRow = Integer.parseInt(startRowField.getText());
                        rowCount = Integer.parseInt(allRowsField.getText());

                        if (startRow <= 0 || rowCount <= 0) {
                            JOptionPane.showMessageDialog(csvExportFrame, "Введены некорректные данные, введите положительные числа");
                        } else {
                            String[] columnHeaders = new String[objects.get(0).size()];
                            for (int i = 0; i < objects.get(0).size(); i++) {
                                columnHeaders[i] = "Столбец " + (i + 1);
                            }

                            JList<String> list = new JList<>(columnHeaders);
                            JDialog listFrame = new JDialog(csvExportFrame, "Параметры Csv файла", true);
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
                            listButton.addActionListener(e14 -> {
                                ArrayList<ArrayList<Object>> newObjects = new ArrayList<>();
                                int[] chosenHeaders = new int[finalListModel.getSize()];
                                for (int i = 0; i < finalListModel.getSize(); i++) {
                                    chosenHeaders[i] = (Integer.parseInt(finalListModel.getElementAt(i).substring(8)) - 1);
                                }
                                for (int j = 0; j < objects.size(); j++) {
                                    ArrayList<Object> newRow = new ArrayList<>();
                                    for (int i : chosenHeaders) {
                                        newRow.add(objects.get(j).get(i));
                                    }
                                    newObjects.add(newRow);
                                }
                                objects = newObjects;
                                listFrame.dispose();
                                jFolderChooser.showDialog(null, "Выбрать папку сохранения");
                                File file = new File(jFolderChooser.getSelectedFile().getPath() + ".csv");
                                try {
                                    CsvHandler.ExportToCsv(objects, startRow, rowCount, columnNames, file);
                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(csvExportFrame, "Некорректный файл");
                                } catch (NumberFormatException formatException) {
                                    JOptionPane.showMessageDialog(csvExportFrame, "Неверный формат указанных данных");
                                } catch (IndexOutOfBoundsException | IllegalArgumentException argumentException) {
                                    JOptionPane.showMessageDialog(csvExportFrame, "Введены некорректные данные");
                                }
                                JOptionPane.showMessageDialog(frame, "Экспорт завершен успешно");
                            }
                            );
                            listFrame.setVisible(true);
                            csvExportFrame.dispose();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(csvExportFrame, "Некорректный формат ввода чисел.");
                    }
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
