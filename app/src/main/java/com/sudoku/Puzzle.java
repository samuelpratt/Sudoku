package com.sudoku;

class Puzzle {

    static int MaxValue = 9;
    static int MinValue = 1;
    static int Size = 9;

    private Integer[][] Numbers;

    Puzzle() {
        InitNumbers();
    }

    Puzzle(Puzzle toCopy) {
        InitNumbers();
        for (AllPoints allPoints = new AllPoints(); allPoints.hasNext(); ) {
            Point p = allPoints.next();
            Numbers[p.x][p.y] = toCopy.GetNumber(p);

        }
    }

    Puzzle(Integer[][] problem) {
        InitNumbers();
        for (AllPoints allPoints = new AllPoints(); allPoints.hasNext(); ) {
            Point p = allPoints.next();
            SetNumber(p, problem[p.x][p.y]);
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

    Point FindNextUnassignedLocation() {
        for (AllPoints allPoints = new AllPoints(); allPoints.hasNext(); ) {
            Point currentPoint = allPoints.next();
            if (GetNumber(currentPoint) == null) {
                return currentPoint;
            }
        }

        return null;
    }

    Boolean NoConflicts(Point point, Integer number) {
        if (IsRowConflict(point.y, number)) return false;
        return !IsColumnConflict(point.x, number);
    }

    @SuppressWarnings("NumberEquality")
    private boolean IsRowConflict(int y, Integer number) {
        for (RowPoints rowPoints = new RowPoints(y); rowPoints.hasNext(); ) {
            Point rowPoint = rowPoints.next();
            if (GetNumber(rowPoint) == number)
                return true;
        }
        return false;
    }

    @SuppressWarnings("NumberEquality")
    private boolean IsColumnConflict(int x, Integer number) {
        for (ColumnPoints colPoints = new ColumnPoints(x); colPoints.hasNext(); ) {
            Point colPoint = colPoints.next();
            if (GetNumber(colPoint) == number)
                return true;
        }
        return false;
    }
}
