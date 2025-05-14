package controller;

import model.ExcelData;
import model.ExcelReader;
import model.ExcelWriter;
import model.StatisticsCalculator;
import view.StatisticsView;

import java.io.IOException;
import java.util.Map;

public class StatisticsController {
    private final ExcelReader excelReader;
    private final StatisticsCalculator statisticsCalculator;
    private final ExcelWriter excelWriter;
    private final StatisticsView view;

    public StatisticsController(StatisticsView view) {
        this.excelReader = new ExcelReader();
        this.statisticsCalculator = new StatisticsCalculator();
        this.excelWriter = new ExcelWriter();
        this.view = view;
    }

    public void processFile(String inputFilePath, String outputFilePath) {
        try {
            // 1. Чтение данных
            ExcelData excelData = excelReader.readExcel(inputFilePath);
            
            // 2. Расчет статистики
            Map<String, Map<String, Double>> statistics = statisticsCalculator.calculateStatistics(excelData);
            excelData.setCalculatedStatistics(statistics);
            
            // 3. Запись результатов
            excelWriter.writeStatisticsToExcel(outputFilePath, excelData);
            
            view.showSuccess("Файл успешно обработан. Результаты сохранены в: " + outputFilePath);
        } catch (IOException e) {
            view.showError("Ошибка при обработке файла: " + e.getMessage());
        }
    }
}