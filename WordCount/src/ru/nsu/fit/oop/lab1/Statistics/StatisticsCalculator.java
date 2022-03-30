package ru.nsu.fit.oop.lab1.Statistics;

import javafx.util.Pair;
import ru.nsu.fit.oop.lab1.Word;

import java.util.Set;
import java.util.TreeSet;

public class StatisticsCalculator {
    private Statistics statistics;

    public Statistics getStatistics() {
        return statistics;
    }

    public void calculateStatistics(Set<Pair<Word, Integer>> wordDict, int totalWords) throws IllegalArgumentException {
        if (totalWords == 0) {
            throw new IllegalArgumentException();
        }
        this.statistics = new Statistics(totalWords);
        for (Pair<Word, Integer> currentElem : wordDict) {
            statistics.add(currentElem);
        }
    }
}
