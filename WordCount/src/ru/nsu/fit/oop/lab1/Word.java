package ru.nsu.fit.oop.lab1;

import static java.lang.Character.isLetterOrDigit;

public class Word implements Comparable<Word> {
    private final String value;

    public Word(String value) throws IllegalArgumentException {
        for (int i = 0; i < value.length(); ++i) {
            if (!isLetterOrDigit(value.charAt(i))) {
                throw new IllegalArgumentException();
            }
        }

        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Word word = (Word) o;

        return value.equals(word.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int compareTo(Word o) {
        if (this == o) {
            return 0;
        }
        return value.compareTo(o.getValue());
    }
}
