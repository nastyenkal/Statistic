package model;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.distribution.TDistribution;
import java.util.*;
import org.apache.commons.math3.exception.MathIllegalArgumentException;

public class StatisticsCalculator {
    private static final double CONFIDENCE_LEVEL = 0.95;

    public ExcelData calculateStatistics(ExcelData excelData) {
        Map<String, Map<String, Double>> stats = new LinkedHashMap<>();
        Map<String, double[][]> covariances = new LinkedHashMap<>();

        for (String sheetName : excelData.getSheetNames()) {
            Map<String, List<Double>> columns = excelData.getSheetData().get(sheetName);
            Map<String, Double> sheetStats = new LinkedHashMap<>();
            
            // Рассчитываем статистики для каждого столбца
            for (Map.Entry<String, List<Double>> columnEntry : columns.entrySet()) {
                String columnName = columnEntry.getKey();
                List<Double> sampleList = columnEntry.getValue();
                double[] sample = sampleList.stream().mapToDouble(Double::doubleValue).toArray();
                DescriptiveStatistics ds = new DescriptiveStatistics(sample);

                // 1. Количество элементов
                sheetStats.put(columnName + " - Количество элементов", (double) ds.getN());

                // 2. Среднее арифметическое
                sheetStats.put(columnName + " - Среднее арифметическое", ds.getMean());

                // 3. Среднее геометрическое
                sheetStats.put(columnName + " - Среднее геометрическое", calculateGeometricMean(sample));

                // 4. Стандартное отклонение
                sheetStats.put(columnName + " - Стандартное отклонение", ds.getStandardDeviation());

                // 5. Размах
                sheetStats.put(columnName + " - Размах", ds.getMax() - ds.getMin());

                // 6. Коэффициент вариации
                sheetStats.put(columnName + " - Коэффициент вариации", 
                    ds.getMean() != 0 ? ds.getStandardDeviation()/ds.getMean() : Double.NaN);

                // 7. Дисперсия
                sheetStats.put(columnName + " - Дисперсия", ds.getVariance());

                // 8. Доверительный интервал
                double[] ci = calculateConfidenceInterval(ds);
                sheetStats.put(columnName + " - ДИ нижняя граница", ci[0]);
                sheetStats.put(columnName + " - ДИ верхняя граница", ci[1]);

                // 9. Минимум и максимум
                sheetStats.put(columnName + " - Минимум", ds.getMin());
                sheetStats.put(columnName + " - Максимум", ds.getMax());
            }
            
            // 10. Ковариация между столбцами
            double[][] covMatrix = calculateCovarianceMatrix(columns);
            covariances.put(sheetName, covMatrix);

            stats.put(sheetName, sheetStats);
        }

        return new ExcelData(
            excelData.getSheetNames(),
            excelData.getSheetData(),
            stats,
            covariances
        );
    }

    private double calculateGeometricMean(double[] data) {
        if (data == null || data.length == 0) {
            return Double.NaN;
        }
        
        // Фильтрация положительных значений
        double[] positiveValues = Arrays.stream(data)
                                       .filter(value -> value > 0)
                                       .toArray();
        
        if (positiveValues.length == 0) {
            return Double.NaN;
        }
        
        try {
            return StatUtils.geometricMean(positiveValues);
        } catch (MathIllegalArgumentException e) {
            return Double.NaN;
        }
    }

    private double[] calculateConfidenceInterval(DescriptiveStatistics ds) {
        if (ds.getN() < 2) return new double[]{Double.NaN, Double.NaN};
        
        TDistribution tDist = new TDistribution(ds.getN() - 1);
        double critVal = tDist.inverseCumulativeProbability(1 - (1 - CONFIDENCE_LEVEL)/2);
        double margin = critVal * ds.getStandardDeviation() / Math.sqrt(ds.getN());
        
        return new double[]{
            ds.getMean() - margin, 
            ds.getMean() + margin
        };
    }

    private double[][] calculateCovarianceMatrix(Map<String, List<Double>> columns) {
        List<List<Double>> columnData = new ArrayList<>(columns.values());
        int numColumns = columnData.size();
        double[][] matrix = new double[numColumns][numColumns];
        
        // Преобразуем данные в массив
        double[][] data = new double[numColumns][];
        int index = 0;
        for (List<Double> col : columnData) {
            data[index++] = col.stream().mapToDouble(Double::doubleValue).toArray();
        }
        
        // Рассчитываем ковариацию
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numColumns; j++) {
                matrix[i][j] = calculateCovariance(data[i], data[j]);
            }
        }
        
        return matrix;
    }
    
    private double calculateCovariance(double[] x, double[] y) {
        if (x.length != y.length || x.length < 2) return Double.NaN;
        
        double sumX = 0.0;
        double sumY = 0.0;
        double sumXY = 0.0;
        
        for (int i = 0; i < x.length; i++) {
            sumX += x[i];
            sumY += y[i];
            sumXY += x[i] * y[i];
        }
        
        double n = x.length;
        return (sumXY - (sumX * sumY) / n) / (n - 1);
    }
}