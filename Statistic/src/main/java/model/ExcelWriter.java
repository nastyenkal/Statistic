package model;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ExcelWriter {
    
    public void writeStatisticsToExcel(String filePath, ExcelData excelData) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            createStatsSheets(workbook, excelData);
            
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
            }
        }
    }

    private void createStatsSheets(Workbook workbook, ExcelData excelData) {
        for (String sheetName : excelData.getSheetNames()) {
            Sheet sheet = workbook.createSheet(sheetName);
            int rowNum = 0;
            
            Row header = sheet.createRow(rowNum++);
            header.createCell(0).setCellValue("Показатель");
            
            Map<String, Double> stats = excelData.getCalculatedStatistics().get(sheetName);
            List<String> columnNames = new ArrayList<>();
            for (String key : stats.keySet()) {
                if (key.contains(" - ")) {
                    String colName = key.split(" - ")[0];
                    if (!columnNames.contains(colName)) {
                        columnNames.add(colName);
                    }
                }
            }
            
            for (int i = 0; i < columnNames.size(); i++) {
                header.createCell(i + 1).setCellValue(columnNames.get(i));
            }
            
            Set<String> metrics = new LinkedHashSet<>();
            for (String key : stats.keySet()) {
                if (key.contains(" - ")) {
                    metrics.add(key.split(" - ")[1]);
                }
            }
            
            for (String metric : metrics) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(metric);
                
                for (int i = 0; i < columnNames.size(); i++) {
                    String fullKey = columnNames.get(i) + " - " + metric;
                    Double value = stats.get(fullKey);
                    if (value != null) {
                        row.createCell(i + 1).setCellValue(value);
                    }
                }
            }
            
            rowNum++;
            Row covHeader = sheet.createRow(rowNum++);
            covHeader.createCell(0).setCellValue("Матрица ковариации");
            
            double[][] covMatrix = excelData.getCovarianceMatrices().get(sheetName);
            if (covMatrix != null) {
                Row matrixHeader = sheet.createRow(rowNum++);
                matrixHeader.createCell(0).setCellValue("");
                for (int i = 0; i < columnNames.size(); i++) {
                    matrixHeader.createCell(i + 1).setCellValue(columnNames.get(i));
                }
                
                for (int i = 0; i < covMatrix.length; i++) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(columnNames.get(i));
                    for (int j = 0; j < covMatrix[i].length; j++) {
                        row.createCell(j + 1).setCellValue(covMatrix[i][j]);
                    }
                }
            }
            
            for (int i = 0; i < columnNames.size() + 1; i++) {
                sheet.autoSizeColumn(i);
            }
        }
    }
}