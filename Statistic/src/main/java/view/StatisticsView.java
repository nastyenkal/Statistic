package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatisticsView {
    private JFrame frame;
    private JButton importButton;
    private JButton exportButton;
    private JButton exitButton;
    private JTextArea logArea;

    public StatisticsView() {
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Статистический анализатор Excel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Панель кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout());
        importButton = new JButton("Импорт данных");
        exportButton = new JButton("Экспорт статистики");
        exitButton = new JButton("Выход");
        
        buttonPanel.add(importButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(exitButton);

        // Область лога
        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public void addImportListener(ActionListener listener) {
        importButton.addActionListener(listener);
    }

    public void addExportListener(ActionListener listener) {
        exportButton.addActionListener(listener);
    }

    public void addExitListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
        logArea.append("ОШИБКА: " + message + "\n");
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(frame, message, "Успех", JOptionPane.INFORMATION_MESSAGE);
        logArea.append("УСПЕХ: " + message + "\n");
    }

    public String showFileChooser(String title) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        fileChooser.setCurrentDirectory(new java.io.File("D:\\Документы Настя\\МИФИ\\Java"));
        
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }
}