package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class StatisticsView {
    private JFrame frame;
    private JButton importButton;
    private JButton exportButton;
    private JButton exitButton;
    private JTextArea logArea;
    private JFileChooser fileChooser;

    public StatisticsView() {
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Excel Statistics Analyzer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        importButton = new JButton("Импорт данных");
        exportButton = new JButton("Экспорт статистики");
        exitButton = new JButton("Выход");
        
        buttonPanel.add(importButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(exitButton);

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("D:\\Документы Настя\\МИФИ\\Java"));
        
        javax.swing.filechooser.FileNameExtensionFilter filter = 
            new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel Files (*.xlsx)", "xlsx");
        fileChooser.setFileFilter(filter);

        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
    }

    public String showSaveDialog() {
        fileChooser.setDialogTitle("Сохранить результаты");
        int result = fileChooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if (!path.toLowerCase().endsWith(".xlsx")) {
                path += ".xlsx";
            }
            return path;
        }
        return null;
    }

    public String showOpenDialog() {
        fileChooser.setDialogTitle("Выберите файл для импорта");
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message, "Сообщение", JOptionPane.INFORMATION_MESSAGE);
        logArea.append("[INFO] " + message + "\n");
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
        logArea.append("[ERROR] " + message + "\n");
    }

    public void addImportListener(java.awt.event.ActionListener listener) {
        importButton.addActionListener(listener);
    }

    public void addExportListener(java.awt.event.ActionListener listener) {
        exportButton.addActionListener(listener);
    }

    public void addExitListener(java.awt.event.ActionListener listener) {
        exitButton.addActionListener(listener);
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
}