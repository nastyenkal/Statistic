package model;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelReader {
    
    public ExcelData readExcel(String filePath) throws IOException {
        Map<String, Map<String, List<Double>>> sheetData = new HashMap<>();
        List<String> sheetNames = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            for (Sheet sheet : workbook) {
                String sheetName = sheet.getSheetName();
                sheetNames.add(sheetName);
                Map<String, List<Double>> columns = new LinkedHashMap<>();
                
                Row headerRow = sheet.getRow(0);
                if (headerRow == null) continue;
                
                for (Cell cell : headerRow) {
                    String colName = getCellValueAsString(cell);
                    columns.put(colName, new ArrayList<>());
                }
                
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;
                    
                    int colIndex = 0;
                    for (Cell cell : row) {
                        String colName = getHeaderForColumn(headerRow, colIndex);
                        if (colName != null && !colName.isEmpty()) {
                            Double value = getNumericCellValue(cell);
                            if (value != null) {
                                columns.get(colName).add(value);
                            }
                        }
                        colIndex++;
                    }
                }
                
                sheetData.put(sheetName, columns);
            }
        }
        
        return new ExcelData(
            sheetNames, 
            sheetData, 
            new HashMap<>(),  
            new HashMap<>()   
        );
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case FORMULA: 
                switch (cell.getCachedFormulaResultType()) {
                    case STRING: return cell.getStringCellValue();
                    case NUMERIC: return String.valueOf(cell.getNumericCellValue());
                    case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
                    default: return "";
                }
            default: return "";
        }
    }
    
    private Double getNumericCellValue(Cell cell) {
        if (cell == null) return null;
        try {
            switch (cell.getCellType()) {
                case NUMERIC: return cell.getNumericCellValue();
                case FORMULA: 
                    if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                        return cell.getNumericCellValue();
                    }
                    return null;
                case STRING:
                    try {
                        return Double.parseDouble(cell.getStringCellValue());
                    } catch (NumberFormatException e) {
                        return null;
                    }
                default: return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    private String getHeaderForColumn(Row headerRow, int columnIndex) {
        if (headerRow == null || columnIndex >= headerRow.getLastCellNum()) {
            return null;
        }
        Cell cell = headerRow.getCell(columnIndex);
        return cell != null ? getCellValueAsString(cell) : null;
    }
}