package com.sudoku;

class AllPoints extends Points {

    AllPoints() {
        super();
        for (int y = Puzzle.Size - 1; y >= 0; y--) {
            for (int x = Puzzle.Size - 1; x >= 0; x--) {
                points.push(new Point(x, y));
            }
        }
    }

}
