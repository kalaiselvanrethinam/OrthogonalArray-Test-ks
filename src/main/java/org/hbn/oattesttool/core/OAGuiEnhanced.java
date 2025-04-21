package org.hbn.oattesttool.core;

import org.json.JSONException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        frame.setBounds(100, 100, 700, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        factorNameField = new JTextField();
        levelsField = new JTextField();

        JButton addButton = new JButton("Add Factor");
        addButton.addActionListener(this::addFactor);

        factorListModel = new DefaultListModel<>();
        JList<String> factorList = new JList<>(factorListModel);
        JScrollPane listScroll = new JScrollPane(factorList);

        oaTypeDropdown = new JComboBox<>(new String[]{"L4", "L8", "L16"});

        inputPanel.add(new JLabel("Factor Name:"));
        inputPanel.add(factorNameField);
        inputPanel.add(new JLabel("Levels (comma-separated):"));
        inputPanel.add(levelsField);
        inputPanel.add(addButton);
        inputPanel.add(new JLabel("OA Type:"));
        inputPanel.add(oaTypeDropdown);

        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);
        frame.getContentPane().add(listScroll, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        JButton generateButton = new JButton("Generate OA Files");
        generateButton.addActionListener(e -> generateOA());

        previewArea = new JTextArea();
        previewArea.setLineWrap(true);
        previewArea.setWrapStyleWord(true);
        JScrollPane previewScroll = new JScrollPane(previewArea);
        previewScroll.setPreferredSize(new Dimension(600, 200));

        bottomPanel.add(generateButton, BorderLayout.NORTH);
        bottomPanel.add(previewScroll, BorderLayout.CENTER);

        frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);


    }

    private void addFactor(ActionEvent e) {
        String name = factorNameField.getText().trim();
        String[] levels = levelsField.getText().split(",");

        if (!name.isEmpty() && levels.length > 0) {
            headers.add(name);
            List<String> levelsForFactor = new ArrayList<>();
            for (String level : levels) levelsForFactor.add(level.trim());
            levelsList.add(levelsForFactor);
            factorListModel.addElement(name + " => " + levelsField.getText());
            factorNameField.setText("");
            levelsField.setText("");
        }
    }

    private void generateOA() {
        try {
            String oaType = oaTypeDropdown.getSelectedItem().toString();

            // Prepare factor-to-level map
            LinkedHashMap<String, List<String>> factorLevelMap = new LinkedHashMap<>();
            for (int i = 0; i < headers.size(); i++) {
                factorLevelMap.put(headers.get(i), levelsList.get(i));
            }

            // Trigger batch runner
            OABatchRunner.runBatch(oaType, factorLevelMap);

            // Show preview text
            previewArea.setText("Files generated in:\n - output/json/\n - output/postman/\n\nCheck console for detailed logs.");
            JOptionPane.showMessageDialog(frame, "OA-based JSON & Postman CSV files generated successfully!");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error during OA generation: " + ex.getMessage());
        }
    }
}
