package com.sudoku;

class RowPoints extends Points {
    RowPoints(int row) {
        for (int x = Puzzle.Size - 1; x >= 0; x--) {
            points.push(new Point(x, row));
        }
    }
}
