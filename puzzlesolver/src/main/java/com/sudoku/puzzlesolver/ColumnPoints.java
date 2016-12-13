package com.sudoku.puzzlesolver;

class ColumnPoints extends Points {

    ColumnPoints(int column) {
        super();
        for (int y = Puzzle.SIZE - 1; y >= 0; y--) {
            points.push(new Point(column, y));
        }
    }
}
