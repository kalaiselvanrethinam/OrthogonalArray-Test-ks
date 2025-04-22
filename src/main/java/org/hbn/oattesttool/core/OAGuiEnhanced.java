
package org.hbn.oattesttool.core;

import org.apache.poi.ss.usermodel.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class OAGuiEnhanced {

    private JFrame frame;
    private JTextField factorNameField;
    private JTextField levelsField;
    private JTextArea previewArea;
    private DefaultListModel<String> factorListModel;
    private List<String> headers = new ArrayList<>();
    private List<List<String>> levelsList = new ArrayList<>();
    private JComboBox<String> oaTypeDropdown;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                OAGuiEnhanced window = new OAGuiEnhanced();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public OAGuiEnhanced() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Orthogonal Array Test Generator");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        factorNameField = new JTextField(15);
        levelsField = new JTextField(15);
        oaTypeDropdown = new JComboBox<>(new String[]{"L4", "L8", "L16"});

        factorListModel = new DefaultListModel<>();
        JList<String> factorList = new JList<>(factorListModel);
        JScrollPane listScroll = new JScrollPane(factorList);

        JButton addButton = new JButton("Add Factor");
        addButton.setToolTipText("Add factor name and levels to the list");
        addButton.addActionListener(this::addFactor);

        JButton uploadButton = new JButton("Upload Excel");
        uploadButton.setToolTipText("Load factors and levels from Excel file");
        uploadButton.addActionListener(this::uploadExcelFile);

        JButton clearButton = new JButton("Clear All");
        clearButton.setToolTipText("Clear all input fields and data");
        clearButton.addActionListener(e -> clearAll());

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(new JLabel("Factor Name:"), gbc);
        gbc.gridx = 1; inputPanel.add(factorNameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(new JLabel("Levels (comma-separated):"), gbc);
        gbc.gridx = 1; inputPanel.add(levelsField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; inputPanel.add(new JLabel("OA Type:"), gbc);
        gbc.gridx = 1; inputPanel.add(oaTypeDropdown, gbc);
        gbc.gridx = 0; gbc.gridy = 3; inputPanel.add(addButton, gbc);
        gbc.gridx = 1; inputPanel.add(uploadButton, gbc);
        gbc.gridx = 0; gbc.gridy = 4; inputPanel.add(clearButton, gbc);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(listScroll, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JButton generateButton = new JButton("Generate OA Files");
        generateButton.setToolTipText("Generate JSON and Postman CSV based on OA");
        generateButton.addActionListener(e -> generateOA());

        JButton openOutputButton = new JButton("Open Output Folder");
        openOutputButton.setToolTipText("Open the output folder in file explorer");
        openOutputButton.addActionListener(e -> openOutputFolder());

        previewArea = new JTextArea();
        previewArea.setEditable(false);
        previewArea.setLineWrap(true);
        previewArea.setWrapStyleWord(true);
        JScrollPane previewScroll = new JScrollPane(previewArea);
        previewScroll.setPreferredSize(new Dimension(600, 200));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(generateButton);
        buttonPanel.add(openOutputButton);

        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(previewScroll, BorderLayout.CENTER);

        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addFactor(ActionEvent e) {
        String name = factorNameField.getText().trim();
        String[] levels = levelsField.getText().split(",");

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Factor name cannot be empty.");
            return;
        }
        if (levelsField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Levels field cannot be empty.");
            return;
        }

        headers.add(name);
        List<String> levelsForFactor = new ArrayList<>();
        for (String level : levels) {
            levelsForFactor.add(level.trim());
        }
        levelsList.add(levelsForFactor);
        factorListModel.addElement(name + " => " + levelsField.getText());
        factorNameField.setText("");
        levelsField.setText("");
    }

    private void uploadExcelFile(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileInputStream fis = new FileInputStream(file);
                 Workbook workbook = WorkbookFactory.create(fis)) {

                Sheet sheet = workbook.getSheetAt(0);

                headers.clear();
                levelsList.clear();
                factorListModel.clear();

                Row headerRow = sheet.getRow(0);
                if (headerRow == null) throw new Exception("No header row in Excel!");

                int numCols = headerRow.getPhysicalNumberOfCells();
                for (int i = 0; i < numCols; i++) {
                    String header = headerRow.getCell(i).getStringCellValue().trim();
                    headers.add(header);
                    levelsList.add(new ArrayList<>());
                }

                for (int rowIdx = 1; rowIdx <= sheet.getLastRowNum(); rowIdx++) {
                    Row row = sheet.getRow(rowIdx);
                    if (row == null) continue;
                    for (int col = 0; col < numCols; col++) {
                        Cell cell = row.getCell(col);
                        if (cell != null) {
                            String value = cell.toString().trim();
                            if (!value.isEmpty() && !levelsList.get(col).contains(value)) {
                                levelsList.get(col).add(value);
                            }
                        }
                    }
                }

                for (int i = 0; i < headers.size(); i++) {
                    factorListModel.addElement(headers.get(i) + " => " + String.join(", ", levelsList.get(i)));
                }

                JOptionPane.showMessageDialog(frame, "Excel file loaded successfully!");

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error reading Excel: " + ex.getMessage());
            }
        }
    }

    private void generateOA() {
        try {
            String oaType = oaTypeDropdown.getSelectedItem().toString();

            LinkedHashMap<String, List<String>> factorLevelMap = new LinkedHashMap<>();
            for (int i = 0; i < headers.size(); i++) {
                factorLevelMap.put(headers.get(i), levelsList.get(i));
            }

            OABatchRunner.runBatch(oaType, factorLevelMap);

            previewArea.setText("✅ Files generated in:\n - output/json/\n - output/postman/\n\nCheck console for details.");
            JOptionPane.showMessageDialog(frame, "OA-based files generated successfully!");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error during OA generation: " + ex.getMessage());
        }
    }

    private void clearAll() {
        headers.clear();
        levelsList.clear();
        factorListModel.clear();
        factorNameField.setText("");
        levelsField.setText("");
        previewArea.setText("");
    }

    private void openOutputFolder() {
        try {
            Desktop.getDesktop().open(new File("output"));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Output folder not found.");
        }
    }
}
