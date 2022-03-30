package ru.nsu.fit.oop.lab1.Statistics;

import javafx.util.Pair;
import ru.nsu.fit.oop.lab1.Word;
import ru.nsu.fit.oop.lab1.PairByWordComparator;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class StatisticsCounter {
    private final TreeSet<Pair<Word, Integer>> wordDict = new TreeSet<>(new PairByWordComparator());

    private int totalWords;

    public int getTotalWords() {
        return totalWords;
    }

    public Set<Pair<Word, Integer>> getWordDict() {
        return this.wordDict;
    }

    public void countWords(List<Word> wordList) {
        this.totalWords = wordList.size();
        for (Word word : wordList) {
            updateWordDict(word);
        }
    }

    private void updateWordDict(Word word) {
        Pair<Word, Integer> pairByWord = getPairByWord(word);
        if (pairByWord != null) {
            int a = pairByWord.getValue() + 1;
            wordDict.remove(pairByWord);
            wordDict.add(new Pair<>(word, a));
        } else {
            wordDict.add(new Pair<>(word, 1));
        }
    }

    private Pair<Word, Integer> getPairByWord(Word word) {
        Pair<Word, Integer> pairToFind = new Pair<>(word, 0);
        Pair<Word, Integer> ceil = wordDict.ceiling(pairToFind);
        if (ceil != null) {
            return ceil.getKey().equals(word) ? ceil : null;
        }
        return null;
    }

}
