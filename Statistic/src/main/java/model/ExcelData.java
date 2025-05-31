package model;

import java.util.List;
import java.util.Map;

public class ExcelData {
    private List<String> sheetNames;
    // Изменено: для каждого листа храним map: название столбца -> данные столбца
    private Map<String, Map<String, List<Double>>> sheetData;
    private Map<String, Map<String, Double>> calculatedStatistics;
    private Map<String, double[][]> covarianceMatrices;

    public ExcelData(List<String> sheetNames,
                   Map<String, Map<String, List<Double>>> sheetData,
                   Map<String, Map<String, Double>> calculatedStatistics,
                   Map<String, double[][]> covarianceMatrices) {
        this.sheetNames = sheetNames;
        this.sheetData = sheetData;
        this.calculatedStatistics = calculatedStatistics;
        this.covarianceMatrices = covarianceMatrices;
    }

    // Геттеры
    public List<String> getSheetNames() {
        return sheetNames;
    }

    public Map<String, Map<String, List<Double>>> getSheetData() {
        return sheetData;
    }

    public Map<String, Map<String, Double>> getCalculatedStatistics() {
        return calculatedStatistics;
    }

    public Map<String, double[][]> getCovarianceMatrices() {
        return covarianceMatrices;
    }

    // Сеттеры
    public void setSheetNames(List<String> sheetNames) {
        this.sheetNames = sheetNames;
    }

    public void setSheetData(Map<String, Map<String, List<Double>>> sheetData) {
        this.sheetData = sheetData;
    }

    public void setCalculatedStatistics(Map<String, Map<String, Double>> calculatedStatistics) {
        this.calculatedStatistics = calculatedStatistics;
    }

    public void setCovarianceMatrices(Map<String, double[][]> covarianceMatrices) {
        this.covarianceMatrices = covarianceMatrices;
    }
}