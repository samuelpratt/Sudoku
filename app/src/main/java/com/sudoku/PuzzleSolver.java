package com.sudoku;

/**
 * Based on https://see.stanford.edu/materials/icspacs106b/Lecture11.pdf
 */
class PuzzleSolver {
    private Puzzle originalPuzzle;


    PuzzleSolver(Puzzle puzzle) {
        this.originalPuzzle = puzzle;
    }

    Puzzle SolvePuzzle() {
        Puzzle workingPuzzle = new Puzzle(originalPuzzle);
        Solve(workingPuzzle);
        return workingPuzzle;
    }

    private Boolean Solve(Puzzle workingPuzzle) {
        Point workingPoint = workingPuzzle.FindUnassignedLocation();
        if (workingPoint == null)
            return true;
        for (Integer num = workingPuzzle.MinValue; num <= workingPuzzle.MaxValue; num++) {
            if (workingPuzzle.NoConflicts(workingPoint, num)) {
                workingPuzzle.SetNumber(workingPoint, num);
                if (Solve(workingPuzzle))
                    return true;
                else
                    workingPuzzle.EraseNumber(workingPoint); // undo & try again
            }
        }
        return false; // this triggers backtracking from early decisions
    }


}
