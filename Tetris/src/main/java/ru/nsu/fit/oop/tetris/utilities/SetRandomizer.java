package ru.nsu.fit.oop.tetris.utilities;

import java.util.Random;
import java.util.Set;

public class SetRandomizer {
    public static <T> T getRandomElement(Set<T> set) {
        int size = set.size();
        int item = new Random().nextInt(size);
        int i = 0;
        for (T element : set) {
            if (i == item) return element;
            i++;
        }
        return null;
    }
}
