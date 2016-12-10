package com.sudoku;

/**
 * Based on https://see.stanford.edu/materials/icspacs106b/Lecture11.pdf
 */
public class PuzzleSolver {
    private Puzzle originalPuzzle;


    public PuzzleSolver(Puzzle puzzle) {
        this.originalPuzzle = puzzle;
    }

    public Puzzle SolvePuzzle() {
        Puzzle workingPuzzle = new Puzzle(originalPuzzle);
        Solve(workingPuzzle);
        return workingPuzzle;
    }

    private Boolean Solve(Puzzle workingPuzzle) {
        Point workingPoint = FindUnassignedLocation(workingPuzzle);
        if (workingPoint == null)
            return true;
        for (Integer num = 1; num <= 9; num++) {
            if (NoConflicts(workingPuzzle, workingPoint, num)) {
                workingPuzzle.SetNumber(workingPoint, num);
                if (Solve(workingPuzzle)) return true;
                workingPuzzle.EraseNumber(workingPoint); // undo & try again
            }
        }
        return false; // this triggers backtracking from early decisions
    }

    private Point FindUnassignedLocation(Puzzle workingPuzzle) {
        for (int x = 0; x < workingPuzzle.Size; x++) {
            for (int y = 0; y < workingPuzzle.Size; y++) {
                Point currentPoint = new Point(x, y);
                if (workingPuzzle.GetNumber(currentPoint) == null) {
                    return currentPoint;
                }
            }
        }
        return null;
    }

    private Boolean NoConflicts(Puzzle workingPuzzle, Point point, Integer number) {
        for (int x = 0; x < workingPuzzle.Size; x++) {
            if (workingPuzzle.GetNumber(new Point(x, point.y)) == number)
                return false;
        }
        for (int y = 0; y < workingPuzzle.Size; y++) {
            if (workingPuzzle.GetNumber(new Point(point.x, y)) == number)
                return false;
        }
        return true;
    }
}
