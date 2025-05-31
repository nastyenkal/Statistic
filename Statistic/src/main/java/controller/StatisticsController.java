package controller;

import java.io.IOException;
import model.*;
import view.StatisticsView;

public class StatisticsController {
    private final ExcelReader excelReader;
    private final StatisticsCalculator statisticsCalculator;
    private final ExcelWriter excelWriter;
    private final StatisticsView view;
    private ExcelData currentData;

    public StatisticsController() {
        this.excelReader = new ExcelReader();
        this.statisticsCalculator = new StatisticsCalculator();
        this.excelWriter = new ExcelWriter();
        this.view = new StatisticsView();
        setupListeners();
    }

    private void setupListeners() {
        view.addImportListener(e -> importData());
        view.addExportListener(e -> exportData());
        view.addExitListener(e -> System.exit(0));
    }

    public void start() {
        view.setVisible(true);
        System.out.println("Application started");
    }

    private void importData() {
        try {
            String path = view.showOpenDialog();
            if (path != null) {
                System.out.println("Importing data from: " + path);
                currentData = excelReader.readExcel(path);
                view.showMessage("Данные успешно загружены. Листов: " + currentData.getSheetNames().size());
            }
        } catch (IOException e) {
            view.showError("Ошибка загрузки: " + e.getMessage());
        }
    }

    private void exportData() {
        if (currentData == null) {
            view.showError("Нет данных для экспорта. Сначала загрузите файл.");
            return;
        }

        try {
            System.out.println("Calculating statistics...");
            // Теперь метод возвращает ExcelData вместо Map
            ExcelData resultData = statisticsCalculator.calculateStatistics(currentData);
            // Обновляем текущие данные с рассчитанной статистикой
            currentData = resultData;
            System.out.println("Statistics calculated successfully");

            String path = view.showSaveDialog();
            if (path != null) {
                System.out.println("Exporting to: " + path);
                excelWriter.writeStatisticsToExcel(path, currentData);
                view.showMessage("Данные успешно экспортированы в: " + path);
                System.out.println("Export completed");
            }
        } catch (IOException e) {
            view.showError("Ошибка экспорта: " + e.getMessage());
        }
    }
}