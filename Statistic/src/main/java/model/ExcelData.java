package model;

import java.util.List;
import java.util.Map;

public class ExcelData {
    private List<String> sheetNames;
    private Map<String, List<List<Double>>> sheetData;
    private Map<String, Map<String, Double>> calculatedStatistics;

    public ExcelData(List<String> sheetNames, 
                   Map<String, List<List<Double>>> sheetData,
                   Map<String, Map<String, Double>> calculatedStatistics) {
        this.sheetNames = sheetNames;
        this.sheetData = sheetData;
        this.calculatedStatistics = calculatedStatistics;
    }

    // Геттеры и сеттеры
    public List<String> getSheetNames() {
        return sheetNames;
    }

    public Map<String, List<List<Double>>> getSheetData() {
        return sheetData;
    }

    public Map<String, Map<String, Double>> getCalculatedStatistics() {
        return calculatedStatistics;
    }

    public void setCalculatedStatistics(Map<String, Map<String, Double>> calculatedStatistics) {
        this.calculatedStatistics = calculatedStatistics;
    }
}