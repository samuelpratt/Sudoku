package com.sudoku;

import org.junit.Assert;
import org.junit.Test;

public class PuzzleTest {

    @Test
    public void RowHasDuplicateNumber_NoConflicts_ReturnsFalse() {
        Point FirstRowFirstCol = new Point(0, 0);
        Point FirstRowSecondCol = new Point(1, 0);
        Integer value = 3;

        Puzzle sut = new Puzzle();

        sut.SetNumber(FirstRowFirstCol, value);

        Assert.assertFalse(sut.NoConflicts(FirstRowSecondCol, value));
    }

    @Test
    public void RowDoesNotHaveDuplicateNumber_NoConflicts_ReturnsTrue() {
        Point FirstRowFirstCol = new Point(0, 0);
        Point FirstRowSecondCol = new Point(1, 0);
        Integer value = 3;
        Integer otherValue = 4;

        Puzzle sut = new Puzzle();

        sut.SetNumber(FirstRowFirstCol, value);

        Assert.assertTrue(sut.NoConflicts(FirstRowSecondCol, otherValue));
    }

    @Test
    public void ColumnHasDuplicateNumber_NoConflicts_ReturnsFalse() {
        Point FirstRowFirstCol = new Point(0, 0);
        Point SecondRowFirstCol = new Point(0, 1);
        Integer value = 3;

        Puzzle sut = new Puzzle();

        sut.SetNumber(FirstRowFirstCol, value);

        Assert.assertFalse(sut.NoConflicts(SecondRowFirstCol, value));
    }

    @Test
    public void ColumnDoesNotHaveDuplicateNumber_NoConflicts_ReturnsTrue() {
        Point FirstRowFirstCol = new Point(0, 0);
        Point SecondRowFirstCol = new Point(0, 1);
        Integer value = 3;
        Integer otherValue = 4;

        Puzzle sut = new Puzzle();

        sut.SetNumber(FirstRowFirstCol, value);

        Assert.assertTrue(sut.NoConflicts(SecondRowFirstCol, otherValue));
    }


}
