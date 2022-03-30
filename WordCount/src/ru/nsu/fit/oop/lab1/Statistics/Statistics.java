package ru.nsu.fit.oop.lab1.Statistics;

import javafx.util.Pair;
import ru.nsu.fit.oop.lab1.Word;

import java.util.Comparator;
import java.util.TreeSet;

public class Statistics {
    private TreeSet<StatisticsItem> statistics = new TreeSet<>(new StatisticsItemComparator());
    private final int totalWords;

    Statistics(int totalWords) throws IllegalArgumentException{
        if (totalWords <= 0) {
            throw new IllegalArgumentException();
        }

        this.totalWords = totalWords;
    }

    public TreeSet<StatisticsItem> getStatistics() {
        return statistics;
    }

    public void add(Pair<Word, Integer> wordCount) {
        statistics.add(new StatisticsItem(wordCount.getKey(), wordCount.getValue()));
    }

    public class StatisticsItem {
        private final Word word;
        private final int count;
        private final Percent percent;

        public StatisticsItem(Word word, int count) {
            this.word = word;
            this.count = count;
            this.percent = new Percent(count, totalWords);
        }

        public Word getWord() {
            return word;
        }

        public int getCount() {
            return count;
        }

        public Percent getPercent() {
            return percent;
        }

    }

    private class StatisticsItemComparator implements Comparator<StatisticsItem> {
        @Override
        public int compare(StatisticsItem o1, StatisticsItem o2) {
            int o1Count = o1.getCount();
            int o2Count = o2.getCount();
            if (o1Count > o2Count) {
                return -1;
            } else if (o1Count < o2Count) {
                return 1;
            }
            return o1.getWord().compareTo(o2.getWord());
        }
    }

}
