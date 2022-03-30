package ru.nsu.fit.oop.lab1.Statistics;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class StatisticsWriter {
    public Statistics statistics;

    public StatisticsWriter(Statistics statistics) {
        this.statistics = statistics;
    }

    public void writeOutputFile(String outputFile, char delimiter) {
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(outputFile));
            writer.write("Word;Amount;Frequency, %\n");
            for (Statistics.StatisticsItem statisticsItem : statistics.getStatistics()) {
                writer.write(statisticsItem.getWord().getValue() + delimiter +
                        statisticsItem.getCount() + delimiter + statisticsItem.getPercent().getValue() + '\n');
            }

        } catch (IOException e) {
            System.err.println("Error while reading file: " + e.getLocalizedMessage());
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }
}
