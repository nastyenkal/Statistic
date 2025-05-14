package mephi.b23902.statistic.statistic;

import controller.StatisticsController;
import view.StatisticsView;

public class MainApp {
    public static void main(String[] args) {
        StatisticsView view = new StatisticsView();
        StatisticsController controller = new StatisticsController(view);

        view.addImportListener(e -> {
            String inputPath = view.showFileChooser("Выберите файл для импорта");
            if (inputPath != null) {
                String outputPath = inputPath.replace(".xlsx", "_statistics.xlsx");
                controller.processFile(inputPath, outputPath);
            }
        });

        view.addExportListener(e -> {
            String outputPath = view.showFileChooser("Выберите файл для экспорта");
            if (outputPath != null) {
                // В этом примере просто сообщаем, что экспорт выполнен
                view.showSuccess("Статистика экспортирована в: " + outputPath);
            }
        });

        view.addExitListener(e -> System.exit(0));
    }
}