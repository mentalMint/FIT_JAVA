package ru.nsu.fit.oop.lab1;

import ru.nsu.fit.oop.lab1.Statistics.StatisticsCalculator;
import ru.nsu.fit.oop.lab1.Statistics.StatisticsCounter;
import ru.nsu.fit.oop.lab1.Statistics.StatisticsWriter;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        FileParser fileParser = new FileParser("in.txt");
        List<String> text = fileParser.getText();
        TextParser textParser = new TextParser();
        textParser.parseText(text);

        StatisticsCounter counter = new StatisticsCounter();
        counter.countWords(textParser.getWordList());
        StatisticsCalculator calculator = new StatisticsCalculator();
        calculator.calculateStatistics(counter.getWordDict(), counter.getTotalWords());

        StatisticsWriter writer = new StatisticsWriter(calculator.getStatistics());
        writer.writeOutputFile("out.csv", ';');
    }
}
