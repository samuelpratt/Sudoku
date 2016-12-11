package com.sudoku;

class Puzzle {

    int MaxValue = 9;
    int MinValue = 1;
    private int Size = 9;
    private Integer[][] Numbers;

    Puzzle(Puzzle toCopy) {
        InitNumbers();
        for (int x = 0; x < Size; x++) {
            for (int y = 0; y < Size; y++) {
                Numbers[x][y] = toCopy.GetNumber(new Point(x, y));
            }
        }
    }

    Puzzle(Integer[][] problem) {
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

    void SetNumber(Point point, Integer value) throws IllegalArgumentException {
        AssertValidIndexes(point);
        AssertValidValue(value);
        Numbers[point.x][point.y] = value;
    }

    void EraseNumber(Point point) throws IllegalArgumentException {
        AssertValidIndexes(point);
        SetNumber(point, null);
    }

    private void AssertValidValue(Integer value) {
        if (value == null)
            return;
        if (value < MinValue || value > MaxValue)
            throw new IllegalArgumentException("Value must be greater than 0 and less than the Size of the grid.");
    }

    private Integer GetNumber(Point point) throws IllegalArgumentException {
        AssertValidIndexes(point);
        return Numbers[point.x][point.y];
    }

    Point FindUnassignedLocation() {
        for (int x = 0; x < Size; x++) {
            for (int y = 0; y < Size; y++) {
                Point currentPoint = new Point(x, y);
                if (GetNumber(currentPoint) == null) {
                    return currentPoint;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("NumberEquality")
    Boolean NoConflicts(Point point, Integer number) {
        for (int x = 0; x < Size; x++) {
            if (GetNumber(new Point(x, point.y)) == number)
                return false;
        }
        for (int y = 0; y < Size; y++) {
            if (GetNumber(new Point(point.x, y)) == number)
                return false;
        }
        return true;
    }
}
