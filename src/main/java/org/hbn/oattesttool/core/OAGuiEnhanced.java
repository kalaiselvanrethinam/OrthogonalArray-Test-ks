package org.hbn.oattesttool.core;
//package org.hbn.oattesttool.core;
//
//import org.apache.poi.ss.usermodel.*;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.io.*;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;
//
//public class OAGuiEnhanced {
//
//    private JFrame frame;
//    private JTextField factorNameField;
//    private JTextField levelsField;
//    private JTextArea previewArea;
//    private DefaultListModel<String> factorListModel;
//    private List<String> headers = new ArrayList<>();
//    private List<List<String>> levelsList = new ArrayList<>();
//    private JComboBox<String> oaTypeDropdown;
//
//    public static void main(String[] args) {
//        EventQueue.invokeLater(() -> {
//            try {
//                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//                OAGuiEnhanced window = new OAGuiEnhanced();
//                window.frame.setVisible(true);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    public OAGuiEnhanced() {
//        initialize();
//    }
//
//    private void initialize() {
//        frame = new JFrame("Orthogonal Array Test Generator");
//        frame.setBounds(100, 100, 600, 600);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setLayout(new BorderLayout());
//
//        // Input Panel
//        JPanel inputPanel = new JPanel(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//        factorNameField = new JTextField(15);
//        levelsField = new JTextField(15);
//        oaTypeDropdown = new JComboBox<>(new String[]{"L4", "L8", "L16"});
//
//        factorListModel = new DefaultListModel<>();
//        JList<String> factorList = new JList<>(factorListModel);
//        JScrollPane listScroll = new JScrollPane(factorList);
//
//        JButton addButton = new JButton("Add Factor");
//        addButton.setToolTipText("Add factor name and levels to the list");
//        addButton.addActionListener(this::addFactor);
//
//        JButton uploadButton = new JButton("Upload Excel");
//        uploadButton.setToolTipText("Load factors and levels from Excel file");
//        uploadButton.addActionListener(this::uploadExcelFile);
//
//        JButton clearButton = new JButton("Clear All");
//        clearButton.setToolTipText("Clear all input fields and data");
//        clearButton.addActionListener(e -> clearAll());
//
//        gbc.insets = new Insets(5, 5, 5, 5);
//        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(new JLabel("Factor Name:"), gbc);
//        gbc.gridx = 1; inputPanel.add(factorNameField, gbc);
//        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(new JLabel("Levels (comma-separated):"), gbc);
//        gbc.gridx = 1; inputPanel.add(levelsField, gbc);
//        gbc.gridx = 0; gbc.gridy = 2; inputPanel.add(new JLabel("OA Type:"), gbc);
//        gbc.gridx = 1; inputPanel.add(oaTypeDropdown, gbc);
//        gbc.gridx = 0; gbc.gridy = 3; inputPanel.add(addButton, gbc);
//        gbc.gridx = 1; inputPanel.add(uploadButton, gbc);
//        gbc.gridx = 0; gbc.gridy = 4; inputPanel.add(clearButton, gbc);
//
//        frame.add(inputPanel, BorderLayout.NORTH);
//        frame.add(listScroll, BorderLayout.CENTER);
//
//        // Bottom Panel
//        JPanel bottomPanel = new JPanel(new BorderLayout());
//
//        JButton generateButton = new JButton("Generate OA Files");
//        generateButton.setToolTipText("Generate JSON and Postman CSV based on OA");
//        generateButton.addActionListener(e -> generateOA());
//
//        JButton openOutputButton = new JButton("Open Output Folder");
//        openOutputButton.setToolTipText("Open the output folder in file explorer");
//        openOutputButton.addActionListener(e -> openOutputFolder());
//
//        previewArea = new JTextArea();
//        previewArea.setEditable(false);
//        previewArea.setLineWrap(true);
//        previewArea.setWrapStyleWord(true);
//        JScrollPane previewScroll = new JScrollPane(previewArea);
//        previewScroll.setPreferredSize(new Dimension(200, 200));
//
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.add(generateButton);
//        buttonPanel.add(openOutputButton);
//
//        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
//        bottomPanel.add(previewScroll, BorderLayout.CENTER);
//
//        // Allure Report Generation Button
//        JButton generateReportButton = new JButton("Generate Allure Report");
//        generateReportButton.addActionListener(this::generateAllureReport);
//        bottomPanel.add(generateReportButton, BorderLayout.SOUTH);
//
//        // Allure Report Download Button
//        JButton downloadReportButton = new JButton("Download Allure Report");
//        downloadReportButton.setToolTipText("Download the generated Allure report as a zip file.");
//        downloadReportButton.addActionListener(this::downloadAllureReport);
//        bottomPanel.add(downloadReportButton, BorderLayout.SOUTH);
//
//        frame.add(bottomPanel, BorderLayout.SOUTH);
//    }
//
//    private void addFactor(ActionEvent e) {
//        String name = factorNameField.getText().trim();
//        String[] levels = levelsField.getText().split(",");
//
//        if (name.isEmpty()) {
//            JOptionPane.showMessageDialog(frame, "Factor name cannot be empty.");
//            return;
//        }
//        if (levelsField.getText().trim().isEmpty()) {
//            JOptionPane.showMessageDialog(frame, "Levels field cannot be empty.");
//            return;
//        }
//
//        headers.add(name);
//        List<String> levelsForFactor = new ArrayList<>();
//        for (String level : levels) {
//            levelsForFactor.add(level.trim());
//        }
//        levelsList.add(levelsForFactor);
//        factorListModel.addElement(name + " => " + levelsField.getText());
//        factorNameField.setText("");
//        levelsField.setText("");
//    }
//
//    private void uploadExcelFile(ActionEvent e) {
//        JFileChooser fileChooser = new JFileChooser();
//        int option = fileChooser.showOpenDialog(frame);
//        if (option == JFileChooser.APPROVE_OPTION) {
//            File file = fileChooser.getSelectedFile();
//            try (FileInputStream fis = new FileInputStream(file);
//                 Workbook workbook = WorkbookFactory.create(fis)) {
//
//                Sheet sheet = workbook.getSheetAt(0);
//
//                headers.clear();
//                levelsList.clear();
//                factorListModel.clear();
//
//                Row headerRow = sheet.getRow(0);
//                if (headerRow == null) throw new Exception("No header row in Excel!");
//
//                int numCols = headerRow.getPhysicalNumberOfCells();
//                for (int i = 0; i < numCols; i++) {
//                    String header = headerRow.getCell(i).getStringCellValue().trim();
//                    headers.add(header);
//                    levelsList.add(new ArrayList<>());
//                }
//
//                for (int rowIdx = 1; rowIdx <= sheet.getLastRowNum(); rowIdx++) {
//                    Row row = sheet.getRow(rowIdx);
//                    if (row == null) continue;
//                    for (int col = 0; col < numCols; col++) {
//                        Cell cell = row.getCell(col);
//                        if (cell != null) {
//                            String value = cell.toString().trim();
//                            if (!value.isEmpty() && !levelsList.get(col).contains(value)) {
//                                levelsList.get(col).add(value);
//                            }
//                        }
//                    }
//                }
//
//                for (int i = 0; i < headers.size(); i++) {
//                    factorListModel.addElement(headers.get(i) + " => " + String.join(", ", levelsList.get(i)));
//                }
//
//                JOptionPane.showMessageDialog(frame, "Excel file loaded successfully!");
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                JOptionPane.showMessageDialog(frame, "Error reading Excel: " + ex.getMessage());
//            }
//        }
//    }
//
//    private void generateOA() {
//        try {
//            String oaType = oaTypeDropdown.getSelectedItem().toString();
//
//            LinkedHashMap<String, List<String>> factorLevelMap = new LinkedHashMap<>();
//            for (int i = 0; i < headers.size(); i++) {
//                factorLevelMap.put(headers.get(i), levelsList.get(i));
//            }
//
//            OABatchRunner.runBatch(oaType, factorLevelMap);
//
//            previewArea.setText("✅ Files generated in:\n - output/json/\n - output/postman/\n\nCheck console for details.");
//            JOptionPane.showMessageDialog(frame, "OA-based files generated successfully!");
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(frame, "Error during OA generation: " + ex.getMessage());
//        }
//    }
//
//    private void clearAll() {
//        headers.clear();
//        levelsList.clear();
//        factorListModel.clear();
//        factorNameField.setText("");
//        levelsField.setText("");
//        previewArea.setText("");
//    }
//
//    private void openOutputFolder() {
//        try {
//            Desktop.getDesktop().open(new File("output"));
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(frame, "Output folder not found.");
//        }
//    }
//
//    private void generateAllureReport(ActionEvent e) {
//        try {
//            // Run Maven command to generate Allure report
//            ProcessBuilder processBuilder = new ProcessBuilder("mvn", "clean", "test", "allure:serve");
//            processBuilder.directory(new File(System.getProperty("user.dir"))); // Set the working directory to current directory
//            Process process = processBuilder.start();
//            process.waitFor();
//
//            // Display the report download link after report generation
//            File allureReportDir = new File("target/allure-report");
//            if (allureReportDir.exists() && allureReportDir.isDirectory()) {
//                String reportLink = allureReportDir.getAbsolutePath();
//                previewArea.setText("Allure report generated successfully! Check the report at: " + reportLink);
//                JOptionPane.showMessageDialog(frame, "Allure report generated successfully!");
//
//                // Optionally open the report folder in browser
//                Desktop.getDesktop().browse(new File(reportLink).toURI());
//            } else {
//                JOptionPane.showMessageDialog(frame, "Allure report generation failed!");
//            }
//
//        } catch (IOException | InterruptedException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(frame, "Error while generating report: " + ex.getMessage());
//        }
//    }
//
//    private void downloadAllureReport(ActionEvent e) {
//        try {
//            File reportDir = new File("C:\\Ortho\\allure-results");
//            // Check if the report directory exists
//            if (reportDir.exists() && reportDir.isDirectory()) {
//                File zipFile = new File("allure-report.zip");
//
//                // Check if the zip file already exists
//                if (!zipFile.exists()) {
//                    // Create the zip file containing the report directory
//                    try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
//                        zipDirectory(reportDir, zos, reportDir.getAbsolutePath());
//                    }
//                }
//
//                // Now, the zip file is ready to be downloaded
//                previewArea.setText("✅ Allure report downloaded as 'allure-report.zip'.");
//                JOptionPane.showMessageDialog(frame, "Allure report downloaded as 'allure-report.zip'.");
//
//                // Automatically open the file explorer to the location of the zip file
//                Desktop.getDesktop().open(zipFile);
//
//            } else {
//                JOptionPane.showMessageDialog(frame, "Allure report not found.");
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(frame, "Error while downloading report: " + ex.getMessage());
//        }
//    }
//
//    private void zipDirectory(File dir, ZipOutputStream zos, String baseDirPath) throws IOException {
//        for (File file : dir.listFiles()) {
//            if (file.isDirectory()) {
//                zipDirectory(file, zos, baseDirPath);
//            } else {
//                try (FileInputStream fis = new FileInputStream(file)) {
//                    zos.putNextEntry(new ZipEntry(file.getAbsolutePath().substring(baseDirPath.length() + 1)));
//                    byte[] buffer = new byte[1024];
//                    int length;
//                    while ((length = fis.read(buffer)) >= 0) {
//                        zos.write(buffer, 0, length);
//                    }
//                }
//            }
//        }
//
//package com.flowcheck.view;
import org.apache.poi.ss.usermodel.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
        frame.setBounds(100, 100, 600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        factorNameField = new JTextField(15);
        levelsField = new JTextField(15);
        oaTypeDropdown = new JComboBox<>(new String[]{"L4", "L8", "L16"});

        factorListModel = new DefaultListModel<>();
        JList<String> factorList = new JList<>(factorListModel);
        JScrollPane listScroll = new JScrollPane(factorList);

        JButton addButton = new JButton("Add Factor");
        addButton.addActionListener(this::addFactor);

        JButton uploadButton = new JButton("Upload Excel");
        uploadButton.addActionListener(this::uploadExcelFile);

        JButton clearButton = new JButton("Clear All");
        clearButton.addActionListener(e -> clearAll());

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Factor Name:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(factorNameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Levels (comma-separated):"), gbc);
        gbc.gridx = 1;
        inputPanel.add(levelsField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("OA Type:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(oaTypeDropdown, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(addButton, gbc);
        gbc.gridx = 1;
        inputPanel.add(uploadButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(clearButton, gbc);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(listScroll, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        JButton generateButton = new JButton("Generate OA Files");
        generateButton.addActionListener(e -> generateOA());

        JButton openOutputButton = new JButton("Open Output Folder");
        openOutputButton.addActionListener(e -> openOutputFolder());

        JButton runTestButton = new JButton("Run Tests");
        runTestButton.addActionListener(e -> runMavenTest());

        previewArea = new JTextArea();
        previewArea.setEditable(false);
        previewArea.setLineWrap(true);
        previewArea.setWrapStyleWord(true);
        JScrollPane previewScroll = new JScrollPane(previewArea);
        previewScroll.setPreferredSize(new Dimension(200, 200));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(generateButton);
        buttonPanel.add(openOutputButton);
        buttonPanel.add(runTestButton);

        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(previewScroll, BorderLayout.CENTER);

        JButton generateReportButton = new JButton("Generate Allure Report");
        generateReportButton.addActionListener(this::generateAllureReport);
        bottomPanel.add(generateReportButton, BorderLayout.SOUTH);

        JButton downloadReportButton = new JButton("Download Allure Report");
        downloadReportButton.addActionListener(this::downloadAllureReport);
        bottomPanel.add(downloadReportButton, BorderLayout.SOUTH);

        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addFactor(ActionEvent e) {
        String name = factorNameField.getText().trim();
        String[] levels = levelsField.getText().split(",");

        if (name.isEmpty() || levelsField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Factor name and levels cannot be empty.");
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
                int numCols = headerRow.getPhysicalNumberOfCells();
                for (int i = 0; i < numCols; i++) {
                    headers.add(headerRow.getCell(i).getStringCellValue().trim());
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
            previewArea.setText("✅ Files generated in:\n - output/json/\n - output/postman/");
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

    private void generateAllureReport(ActionEvent e) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("mvn", "clean", "test", "allure:serve");
            processBuilder.directory(new File(System.getProperty("user.dir")));
            Process process = processBuilder.start();
            process.waitFor();

            File allureReportDir = new File("C:/Ortho/allure-report");
            if (allureReportDir.exists() && allureReportDir.isDirectory()) {
                String reportLink = allureReportDir.getAbsolutePath();
                previewArea.setText("Allure report generated: " + reportLink);
                Desktop.getDesktop().browse(new File(reportLink).toURI());
            } else {
                JOptionPane.showMessageDialog(frame, "Allure report generation failed!");
            }

        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error generating report: " + ex.getMessage());
        }
    }

    private void downloadAllureReport(ActionEvent e) {
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "allure serve C:\\Ortho\\allure-results");
            builder.redirectErrorStream(true);
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[Allure] " + line);
            }

            int exitCode = process.waitFor();
            System.out.println("Allure exited with code: " + exitCode);

            File allureReportDir = new File("C:/Ortho/allure-report");
            if (allureReportDir.exists() && allureReportDir.isDirectory()) {
                String reportLink = allureReportDir.getAbsolutePath();
                previewArea.setText("✅ Allure report generated: " + reportLink);
                Desktop.getDesktop().browse(new File(reportLink + "/index.html").toURI());
            } else {
                JOptionPane.showMessageDialog(frame, "❌ Allure report generation failed!");
            }

        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "⚠️ Error generating report: " + ex.getMessage());
        }
    }

//    private void runMavenTest() {
//        try {
//            // Replace this with your actual Maven location
//            String mavenExecutable = "C:/Users/DELL/Downloads/apache-maven-3.9.9/bin/mvn.cmd";
//
//            // Updated command with -DskipTests to skip tests and -X for detailed output
//          //  ProcessBuilder builder = new ProcessBuilder(mavenExecutable, "install", "-DskipTests", "-X");
//            ProcessBuilder builder = new ProcessBuilder(mavenExecutable, "test");
//            // Set the working directory to the current user directory
//            builder.directory(new File(System.getProperty("user.dir")));
//            builder.redirectErrorStream(true);
//
//            // Start the Maven process
//            Process process = builder.start();
//
//            // Capture the output of the process
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//
//            StringBuilder output = new StringBuilder();
//            String line;
//
//            // Read the output line by line
//            while ((line = reader.readLine()) != null) {
//                output.append(line).append("\n");
//            }
//
//            // Wait for the process to finish and get the exit code
//            int exitCode = process.waitFor();
//
//            // Update the preview area with the exit code and output
//            previewArea.setText("Maven build finished. Exit code: " + exitCode + "\n\n" + output);
//
//        } catch (IOException | InterruptedException ex) {
//            // Print the stack trace and show error message if there's an issue
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(frame, " Error running Maven test: " + ex.getMessage());
//        }
//    }
private void runMavenTest() {
    try {
        // Replace this with your actual Maven location
        String mavenExecutable = "C:/Users/DELL/Downloads/apache-maven-3.9.9/bin/mvn.cmd";

        // Command to run tests
        ProcessBuilder builder = new ProcessBuilder(mavenExecutable, "test");

        builder.directory(new File(System.getProperty("user.dir")));
        builder.redirectErrorStream(true);

        // Start the Maven process
        Process process = builder.start();

        // Capture the output of the process
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;

        // Read the output line by line
        while ((line = reader.readLine()) != null) {
            // Remove question marks if present and look for the required lines
            if (line.contains("Testing:") || line.contains("Status Code:") || line.contains("Response:")) {
                // Remove the question mark at the beginning of the line
                line = line.replaceAll("^\\?", "").trim();

                // Add a star symbol between test cases
                if (line.contains("Testing:")) {
                    output.append("\n★ ");  // Add a star before each test case
                }

                // Append the line to output
                output.append(line).append("\n");
            }
        }

        // Wait for the process to finish and get the exit code
        int exitCode = process.waitFor();

        // Update the preview area with the exit code and filtered output
        previewArea.setText("✅ Maven test finished. Exit code: " + exitCode + "\n\n" + output);

    } catch (IOException | InterruptedException ex) {
        // Print the stack trace and show error message if there's an issue
        ex.printStackTrace();
        JOptionPane.showMessageDialog(frame, "❌ Error running Maven test: " + ex.getMessage());
    }
}

    private void zipDirectory(File folder, ZipOutputStream zos, String basePath) throws IOException {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                zipDirectory(file, zos, basePath);
            } else {
                try (FileInputStream fis = new FileInputStream(file)) {
                    String entryName = file.getAbsolutePath().substring(basePath.length() + 1);
                    zos.putNextEntry(new ZipEntry(entryName));
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) >= 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                }
            }
        }
    }
}
