package com.sudoku;

import org.junit.Test;

public class PuzzleSolverTest {
    @Test
    public void ValidPuzzle_Solve_SolvedPuzzleReturned() {
        Integer[][] problem = {
                {null, null, 4, null, null, null, null, 6, 7},
                {3, null, null, 4, 7, null, null, null, 5},
                {1, 5, null, 8, 2, null, null, null, 3},

                {null, null, 6, null, null, null, null, 3, 1},
                {8, null, 2, 1, null, 5, 6, null, 4},
                {4, 1, null, null, null, null, 9, null, null},

                {7, null, null, null, 8, null, null, 4, 6},
                {6, null, null, null, 1, 2, null, null, null},
                {9, 3, null, null, null, null, 7, 1, null}};
        Puzzle puzzle = new Puzzle(problem);
        Solver solver = new Solver(puzzle);
        Puzzle solvedPuzzle = solver.solvePuzzle();
        System.out.println(solvedPuzzle);


    }


}
