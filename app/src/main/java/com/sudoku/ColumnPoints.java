package com.sudoku;

class ColumnPoints extends Points {

    ColumnPoints(int column) {
        for (int y = Puzzle.Size - 1; y >= 0; y--) {
            points.push(new Point(column, y));
        }
    }
}
