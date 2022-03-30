package ru.nsu.fit.oop.lab1.Statistics;

public class Percent implements Comparable<Percent> {
    private final Double value;

    public Percent(double value) throws IllegalArgumentException {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException();
        }

        this.value = value;
    }

    public Percent(int count, int totalCount) throws IllegalArgumentException {
        if (count < 0 || count > totalCount || totalCount == 0) {
            throw new IllegalArgumentException();
        }

        this.value = (double) count / totalCount * 100;
    }

    public double getValue() {
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

        Percent percent = (Percent) o;

        return value.equals(percent.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int compareTo(Percent o) {
        if (this == o) {
            return 0;
        }
        return value.compareTo(o.getValue());
    }
}
