package uk.co.samuelpratt.sudoku.puzzlesolver;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class SolverTest {
    @Test
    public void ValidPuzzle_Solve_NoNullValues() {

        //Arrange & Act
        Puzzle solvedPuzzle = solvePuzzle(getPuzzle());

        //Assert
        for (int x = 0; x < Puzzle.SIZE; x++) {
            for (int y = 0; y < Puzzle.SIZE; y++) {
                Point currentPoint = new Point(x, y);
                Assert.assertNotNull(solvedPuzzle.getNumber(currentPoint));
            }
        }
    }

    @Test
    public void ValidPuzzle_Solve_AllValuesInRange() {

        //Arrange & Act
        Puzzle solvedPuzzle = solvePuzzle(getPuzzle());

        //Assert
        for (int x = 0; x < Puzzle.SIZE; x++) {
            for (int y = 0; y < Puzzle.SIZE; y++) {
                Point currentPoint = new Point(x, y);
                Integer value = solvedPuzzle.getNumber(currentPoint);
                Assert.assertTrue(isInRange(value));
            }
        }
    }

    @Test
    public void ValidPuzzle2_Solve_AllValuesInRange() {

        //Arrange & Act
        Puzzle solvedPuzzle = solvePuzzle(getPuzzle2());

        //Assert
        for (int x = 0; x < Puzzle.SIZE; x++) {
            for (int y = 0; y < Puzzle.SIZE; y++) {
                Point currentPoint = new Point(x, y);
                Integer value = solvedPuzzle.getNumber(currentPoint);
                Assert.assertTrue(isInRange(value));
            }
        }
    }

    @Test
    public void ValidPuzzle_Solve_NoDuplicateValuesInColumns() {

        //Arrange & Act
        Puzzle solvedPuzzle = solvePuzzle(getPuzzle());

        for (int x = 0; x < 9; x++) {
            List<Point> colPoints = new ArrayList<>();
            for (int y = 0; y < Puzzle.SIZE; y++) {
                colPoints.add(new Point(x, y));
            }
            AssertNoDuplicatesInLine(solvedPuzzle, colPoints);
        }

    }

    @Test
    public void ValidPuzzle_Solve_NoDuplicateValuesInRows() {

        //Arrange & Act
        Puzzle solvedPuzzle = solvePuzzle(getPuzzle());

        for (int y = 0; y < 9; y++) {
            List<Point> colPoints = new ArrayList<>();
            for (int x = 0; x < Puzzle.SIZE; x++) {
                colPoints.add(new Point(x, y));
            }
            AssertNoDuplicatesInLine(solvedPuzzle, colPoints);
        }

    }

    private void AssertNoDuplicatesInLine(Puzzle solvedPuzzle, List<Point> lineToTest) {
        HashMap<Integer, Boolean> colVals = new HashMap<>();
        Iterator<Point> points = lineToTest.iterator();
        for (; points.hasNext(); ) {
            Point p = points.next();
            Integer value = solvedPuzzle.getNumber(p);

            if (colVals.containsKey(value))
                Assert.fail(String.format(Locale.ENGLISH, "Line already contains value %1d ", value));
            else
                colVals.put(value, true);
        }
    }


    private boolean isInRange(Integer value) {
        return (value > 0 && value < 10);
    }

    private Puzzle solvePuzzle(Integer[][] problem) {
        Puzzle puzzle = new Puzzle(problem);

        Solver solver = new Solver(puzzle);

        //Act
        return solver.solvePuzzle();
    }

    private Integer[][] getPuzzle() {
        return new Integer[][]{
                {null, null, 4, null, null, null, null, 6, 7},
                {3, null, null, 4, 7, null, null, null, 5},
                {1, 5, null, 8, 2, null, null, null, 3},

                {null, null, 6, null, null, null, null, 3, 1},
                {8, null, 2, 1, null, 5, 6, null, 4},
                {4, 1, null, null, null, null, 9, null, null},

                {7, null, null, null, 8, null, null, 4, 6},
                {6, null, null, null, 1, 2, null, null, null},
                {9, 3, null, null, null, null, 7, 1, null}};
    }

    private Integer[][] getPuzzle2() {
        return new Integer[][]{
                {8, null, null, null, null, null, null, null, null},
                {null, null, 7, 5, null, null, null, null, 9},
                {null, 3, null, null, null, null, 1, 8, null},

                {null, 6, null, null, null, 1, null, 5, null},
                {null, null, 9, null, 4, null, null, null, null},
                {null, null, null, 7, 5, null, null, null, null},

                {null, 2, null, null, 7, null, null, null, 4},
                {null, null, null, null, null, 3, 6, 1, null},
                {null, null, null, null, null, null, 8, null, null},
        };
    }


}
