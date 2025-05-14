package model;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelReader {
    public ExcelData readExcel(String filePath) throws IOException {
        List<String> sheetNames = new ArrayList<>();
        Map<String, List<List<Double>>> sheetData = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            for (Sheet sheet : workbook) {
                String sheetName = sheet.getSheetName();
                sheetNames.add(sheetName);
                List<List<Double>> data = new ArrayList<>();

                for (Row row : sheet) {
                    List<Double> rowData = new ArrayList<>();
                    for (Cell cell : row) {
                        if (cell.getCellType() == CellType.NUMERIC) {
                            rowData.add(cell.getNumericCellValue());
                        }
                    }
                    if (!rowData.isEmpty()) {
                        data.add(rowData);
                    }
                }
                sheetData.put(sheetName, data);
            }
        }
        return new ExcelData(sheetNames, sheetData, null);
    }
}