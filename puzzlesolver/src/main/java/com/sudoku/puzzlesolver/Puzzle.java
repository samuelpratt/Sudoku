package com.sudoku.puzzlesolver;

public class Puzzle {

    public static final int SIZE = 9;
    static final int MAX_VALUE = 9;
    static final int MIN_VALUE = 1;
    private Integer[][] Numbers;

    public Puzzle() {
        initNumbers();
    }

    Puzzle(Puzzle toCopy) {
        initNumbers();
        for (AllPoints allPoints = new AllPoints(); allPoints.hasNext(); ) {
            Point p = allPoints.next();
            Numbers[p.x][p.y] = toCopy.getNumber(p);

        }
    }

    Puzzle(Integer[][] problem) {
        initNumbers();
        for (AllPoints allPoints = new AllPoints(); allPoints.hasNext(); ) {
            Point p = allPoints.next();
            setNumber(p, problem[p.x][p.y]);
        }
    }

    private void initNumbers() {
        this.Numbers = new Integer[SIZE][SIZE];
    }

    private void AssertValidIndexes(Point point) throws IllegalArgumentException {
        if (point.x < 0 || point.x > SIZE - 1)
            throw new IllegalArgumentException("X is out of range.");
        if (point.y < 0 || point.y > SIZE - 1)
            throw new IllegalArgumentException("Y is out of range.");
    }

    public void setNumber(Point point, Integer value) throws IllegalArgumentException {
        AssertValidIndexes(point);
        assertValidValue(value);
        Numbers[point.x][point.y] = value;
    }

    void eraseNumber(Point point) throws IllegalArgumentException {
        AssertValidIndexes(point);
        setNumber(point, null);
    }

    private void assertValidValue(Integer value) {
        if (value == null)
            return;
        if (value < MIN_VALUE || value > MAX_VALUE)
            throw new IllegalArgumentException("Value must be greater than 0 and less than the SIZE of the grid.");
    }

    public Integer getNumber(Point point) throws IllegalArgumentException {
        AssertValidIndexes(point);
        return Numbers[point.x][point.y];
    }

    Point findNextUnassignedLocation() {
        for (AllPoints allPoints = new AllPoints(); allPoints.hasNext(); ) {
            Point currentPoint = allPoints.next();
            if (getNumber(currentPoint) == null) {
                return currentPoint;
            }
        }

        return null;
    }

    Boolean noConflicts(Point point, Integer number) {
        if (isRowConflict(point.y, number)) return false;
        return !isColumnConflict(point.x, number);
    }

    @SuppressWarnings("NumberEquality")
    private boolean isRowConflict(int y, Integer number) {
        for (RowPoints rowPoints = new RowPoints(y); rowPoints.hasNext(); ) {
            Point rowPoint = rowPoints.next();
            if (getNumber(rowPoint) == number)
                return true;
        }
        return false;
    }

    @SuppressWarnings("NumberEquality")
    private boolean isColumnConflict(int x, Integer number) {
        for (ColumnPoints colPoints = new ColumnPoints(x); colPoints.hasNext(); ) {
            Point colPoint = colPoints.next();
            if (getNumber(colPoint) == number)
                return true;
        }
        return false;
    }
}
