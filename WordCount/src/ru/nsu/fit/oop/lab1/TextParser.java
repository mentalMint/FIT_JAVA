package ru.nsu.fit.oop.lab1;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.isLetterOrDigit;

public class TextParser {
    private final ArrayList<Word> wordList = new ArrayList<>();

    public TextParser() {
    }

    public void parseText(List<String> text) {
        for (String line : text) {
            int charactersNumber = line.length();
            for (int i = 0; i < charactersNumber; ) {
                while (i < charactersNumber && !isLetterOrDigit(line.charAt(i))) {
                    i++;
                }

                StringBuilder wordBuilder = new StringBuilder();
                while (i < charactersNumber && isLetterOrDigit(line.charAt(i))) {
                    if (line.charAt(i) >= 'A' && line.charAt(i) <= 'Z') {
                        wordBuilder.append((char) (line.charAt(i) + 'a' - 'A'));
                    } else {
                        wordBuilder.append(line.charAt(i));
                    }
                    i++;
                }

                try {
                    Word word = new Word(wordBuilder.toString());
                    wordList.add(word);
                } catch (Exception e) {
                    System.err.println("Error while word init");
                }
            }
        }
    }

    public List<Word> getWordList() {
        return this.wordList;
    }
}
