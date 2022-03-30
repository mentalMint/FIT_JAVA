package ru.nsu.fit.oop.lab1;

import javafx.util.Pair;

import java.util.Comparator;

public class PairByWordComparator implements Comparator<Pair<Word, Integer>> {
    @Override
    public int compare(Pair<Word, Integer> o1, Pair<Word, Integer> o2) {
            return o1.getKey().getValue().compareTo(o2.getKey().getValue());
    }
}
