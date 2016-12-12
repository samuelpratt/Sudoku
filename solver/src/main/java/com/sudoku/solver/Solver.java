package com.sudoku.solver;

/**
 * Based on https://see.stanford.edu/materials/icspacs106b/Lecture11.pdf
 */
class Solver {
    private Puzzle originalPuzzle;


    Solver(Puzzle puzzle) {
        this.originalPuzzle = puzzle;
    }

    Puzzle solvePuzzle() {
        Puzzle workingPuzzle = new Puzzle(originalPuzzle);
        solve(workingPuzzle);
        return workingPuzzle;
    }

    private Boolean solve(Puzzle workingPuzzle) {
        Point workingPoint = workingPuzzle.findNextUnassignedLocation();
        if (workingPoint == null)
            return true;
        for (Integer num = Puzzle.MIN_VALUE; num <= Puzzle.MAX_VALUE; num++) {
            if (workingPuzzle.noConflicts(workingPoint, num)) {
                workingPuzzle.setNumber(workingPoint, num);
                if (solve(workingPuzzle))
                    return true;
                else
                    workingPuzzle.eraseNumber(workingPoint); // undo & try again
            }
        }
        return false; // this triggers backtracking from early decisions
    }


}
