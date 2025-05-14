package model;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsCalculator {
    public Map<String, Map<String, Double>> calculateStatistics(ExcelData excelData) {
        Map<String, Map<String, Double>> statistics = new HashMap<>();
        Map<String, List<List<Double>>> sheetData = excelData.getSheetData();

        for (String sheetName : sheetData.keySet()) {
            List<List<Double>> data = sheetData.get(sheetName);
            Map<String, Double> sheetStats = new HashMap<>();

            for (int i = 0; i < data.size(); i++) {
                double[] sample = data.get(i).stream().mapToDouble(Double::doubleValue).toArray();
                DescriptiveStatistics stats = new DescriptiveStatistics(sample);

                // Расчет показателей для каждой выборки
                sheetStats.put("Sample_" + i + "_Mean", stats.getMean());
                sheetStats.put("Sample_" + i + "_GeometricMean", StatUtils.geometricMean(sample));
                sheetStats.put("Sample_" + i + "_StandardDeviation", stats.getStandardDeviation());
                sheetStats.put("Sample_" + i + "_Range", stats.getMax() - stats.getMin());
                sheetStats.put("Sample_" + i + "_Count", (double) stats.getN());
                sheetStats.put("Sample_" + i + "_Variance", stats.getVariance());
                sheetStats.put("Sample_" + i + "_Max", stats.getMax());
                sheetStats.put("Sample_" + i + "_Min", stats.getMin());
                sheetStats.put("Sample_" + i + "_CoefficientOfVariation", 
                    stats.getStandardDeviation() / stats.getMean());
            }

            // Расчет ковариации между выборками
            if (data.size() > 1) {
                Covariance covariance = new Covariance();
                for (int i = 0; i < data.size(); i++) {
                    for (int j = i + 1; j < data.size(); j++) {
                        double[] sample1 = data.get(i).stream().mapToDouble(Double::doubleValue).toArray();
                        double[] sample2 = data.get(j).stream().mapToDouble(Double::doubleValue).toArray();
                        double cov = covariance.covariance(sample1, sample2);
                        sheetStats.put("Covariance_Sample_" + i + "_and_Sample_" + j, cov);
                    }
                }
            }

            statistics.put(sheetName, sheetStats);
        }

        return statistics;
    }
}