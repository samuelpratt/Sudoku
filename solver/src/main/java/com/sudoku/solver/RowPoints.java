package com.sudoku.solver;

class RowPoints extends Points {
    RowPoints(int row) {
        super();
        for (int x = Puzzle.SIZE - 1; x >= 0; x--) {
            points.push(new Point(x, row));
        }
    }
}
