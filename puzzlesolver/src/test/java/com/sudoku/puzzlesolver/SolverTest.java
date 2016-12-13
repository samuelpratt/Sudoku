package com.sudoku.puzzlesolver;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

public class SolverTest {
    @Test
    public void ValidPuzzle_Solve_NoNullValues() {

        //Arrange & Act
        Puzzle solvedPuzzle = solvePuzzle();

        //Assert
        for (AllPoints points = new AllPoints(); points.hasNext(); ) {
            Point p = points.next();
            Assert.assertNotNull(solvedPuzzle.getNumber(p));
        }
    }

    @Test
    public void ValidPuzzle_Solve_AllValuesInRange() {

        //Arrange & Act
        Puzzle solvedPuzzle = solvePuzzle();

        //Assert
        for (AllPoints points = new AllPoints(); points.hasNext(); ) {
            Point p = points.next();
            Integer value = solvedPuzzle.getNumber(p);
            Assert.assertTrue(isInRange(value));
        }
    }

    @Test
    public void ValidPuzzle_Solve_NoDuplicateValuesInColumns() {

        //Arrange & Act
        Puzzle solvedPuzzle = solvePuzzle();

        for (int x = 0; x < 9; x++) {
            AssertNoDuplicatesInLine(solvedPuzzle, new ColumnPoints(x));
        }

    }

    @Test
    public void ValidPuzzle_Solve_NoDuplicateValuesInRows() {

        //Arrange & Act
        Puzzle solvedPuzzle = solvePuzzle();

        for (int y = 0; y < 9; y++) {
            AssertNoDuplicatesInLine(solvedPuzzle, new RowPoints(y));
        }

    }

    private void AssertNoDuplicatesInLine(Puzzle solvedPuzzle, Iterator<Point> lineToTest) {
        HashMap<Integer, Boolean> colVals = new HashMap<>();
        for (; lineToTest.hasNext(); ) {
            Point p = lineToTest.next();
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

    private Puzzle solvePuzzle() {
        Integer[][] problem = getPuzzle();
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


}
