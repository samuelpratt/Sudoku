package com.sudoku;

public class Puzzle {

    private Integer[][] Numbers;

    public Puzzle(int size) throws IllegalArgumentException {
        AssertValidSize(size);
        Numbers = new Integer[size][size];
    }

    private void AssertValidSize(int size) throws IllegalArgumentException {
        if (size < 3)
            throw new IllegalArgumentException("Puzzle Size must be greater than 3");
    }

    private void AssertValidIndexes(int x, int y) throws IllegalArgumentException {
        if (x < 0 || x > Numbers.length - 1)
            throw new IllegalArgumentException("X is out of range.");
        if (y < 0 || y > Numbers[0].length - 1)
            throw new IllegalArgumentException("Y is out of range.");
    }

    public void SetNumber(int x, int y, int value) throws IllegalArgumentException {
        AssertValidIndexes(x, y);
        AssertValidValue(value);
        Numbers[x][y] = value;
    }

    private void AssertValidValue(int value) {
        if (value < 1 || value > Numbers.length)
            throw new IllegalArgumentException("Value must be greater than 0 and less than the size of the grid.");
    }

    public int GetNumber(int x, int y) throws IllegalArgumentException {
        AssertValidIndexes(x, y);
        return Numbers[x][y];
    }
}
