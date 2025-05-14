package model;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class ExcelWriter {
    public void writeStatisticsToExcel(String filePath, ExcelData excelData) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Statistics");

            // Создание стиля для заголовков
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            int rowNum = 0;
            for (String sheetName : excelData.getCalculatedStatistics().keySet()) {
                Map<String, Double> stats = excelData.getCalculatedStatistics().get(sheetName);

                // Заголовок с именем листа
                Row sheetNameRow = sheet.createRow(rowNum++);
                Cell sheetNameCell = sheetNameRow.createCell(0);
                sheetNameCell.setCellValue("Sheet: " + sheetName);
                sheetNameCell.setCellStyle(headerStyle);

                // Заголовки столбцов
                Row headerRow = sheet.createRow(rowNum++);
                Cell statNameHeader = headerRow.createCell(0);
                statNameHeader.setCellValue("Statistic Name");
                statNameHeader.setCellStyle(headerStyle);

                Cell statValueHeader = headerRow.createCell(1);
                statValueHeader.setCellValue("Value");
                statValueHeader.setCellStyle(headerStyle);

                // Данные статистики
                for (Map.Entry<String, Double> entry : stats.entrySet()) {
                    Row dataRow = sheet.createRow(rowNum++);
                    dataRow.createCell(0).setCellValue(entry.getKey());
                    dataRow.createCell(1).setCellValue(entry.getValue());
                }

                // Пустая строка между листами
                rowNum++;
            }

            // Автоподбор ширины столбцов
            for (int i = 0; i < 2; i++) {
                sheet.autoSizeColumn(i);
            }

            // Запись в файл
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }
    }
}