package com.sudoku;

public class Puzzle {

    public int Size = 9;
    private Integer[][] Numbers;

    public Puzzle(Puzzle toCopy) {
        InitNumbers();
        for (int x = 0; x < Size; x++) {
            for (int y = 0; y < Size; y++) {
                Numbers[x][y] = toCopy.GetNumber(new Point(x, y));
            }
        }
    }

    public Puzzle(Integer[][] problem) {
        InitNumbers();
        for (int x = 0; x < Size; x++) {
            for (int y = 0; y < Size; y++) {
                SetNumber(new Point(x, y), problem[x][y]);
            }
        }
    }

    private void InitNumbers() {
        this.Numbers = new Integer[Size][Size];
    }

    private void AssertValidIndexes(Point point) throws IllegalArgumentException {
        if (point.x < 0 || point.x > Size - 1)
            throw new IllegalArgumentException("X is out of range.");
        if (point.y < 0 || point.y > Size - 1)
            throw new IllegalArgumentException("Y is out of range.");
    }

    public void SetNumber(Point point, Integer value) throws IllegalArgumentException {
        AssertValidIndexes(point);
        AssertValidValue(value);
        Numbers[point.x][point.y] = value;
    }

    public void EraseNumber(Point point) throws IllegalArgumentException {
        AssertValidIndexes(point);
        SetNumber(point, null);
    }

    private void AssertValidValue(Integer value) {
        if (value == null)
            return;
        if (value < 1 || value > Size)
            throw new IllegalArgumentException("Value must be greater than 0 and less than the Size of the grid.");
    }

    public Integer GetNumber(Point point) throws IllegalArgumentException {
        AssertValidIndexes(point);
        return Numbers[point.x][point.y];
    }
}
