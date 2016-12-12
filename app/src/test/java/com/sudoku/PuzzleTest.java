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

    @Test
    public void ValidNumberValidRange_SetNumber_NumberIsSet() {
        Puzzle sut = new Puzzle();
        Point validPoint = new Point(0, 0);
        Integer validNumber = 1;

        sut.SetNumber(validPoint, validNumber);

        Assert.assertEquals(validNumber, sut.GetNumber(validPoint));
    }

    @Test(expected = IllegalArgumentException.class)
    public void NumberTooBig_SetNumber_IllegalArgumentExceptionThrown() {
        Puzzle sut = new Puzzle();
        Point validPoint = new Point(0, 0);
        Integer invalidNumber = Puzzle.MaxValue + 1;

        sut.SetNumber(validPoint, invalidNumber);
    }

    @Test(expected = IllegalArgumentException.class)
    public void NumberTooSmall_SetNumber_IllegalArgumentExceptionThrown() {
        Puzzle sut = new Puzzle();
        Point validPoint = new Point(0, 0);
        Integer invalidNumber = Puzzle.MinValue - 1;

        sut.SetNumber(validPoint, invalidNumber);
    }

    @Test(expected = IllegalArgumentException.class)
    public void InvalidRow_SetNumber_IllegalArgumentExceptionThrown() {
        Puzzle sut = new Puzzle();
        Point invalidPoint = new Point(0, Puzzle.Size + 1);
        Integer validNumber = 0;

        sut.SetNumber(invalidPoint, validNumber);
    }

    @Test(expected = IllegalArgumentException.class)
    public void InvalidCol_SetNumber_IllegalArgumentExceptionThrown() {
        Puzzle sut = new Puzzle();
        Point invalidPoint = new Point(Puzzle.Size + 1, 0);
        Integer validNumber = 0;

        sut.SetNumber(invalidPoint, validNumber);
    }

    @Test(expected = IllegalArgumentException.class)
    public void InvalidCol_GetNumber_IllegalArgumentExceptionThrown() {
        Puzzle sut = new Puzzle();
        Point invalidPoint = new Point(Puzzle.Size + 1, 0);
        sut.GetNumber(invalidPoint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void InvalidRow_GetNumber_IllegalArgumentExceptionThrown() {
        Puzzle sut = new Puzzle();
        Point invalidPoint = new Point(0, Puzzle.Size + 1);
        sut.GetNumber(invalidPoint);
    }

    @Test
    public void ValidPoint_EraseNumber_NumberErased() {
        Puzzle sut = new Puzzle();
        Point validPoint = new Point(0, 0);
        Integer validNumber = 7;
        sut.SetNumber(validPoint, validNumber);

        sut.EraseNumber(validPoint);

        Assert.assertEquals(null, sut.GetNumber(validPoint));
    }

    @Test(expected = IllegalArgumentException.class)
    public void InvalidPoint_EraseNumber_IllegalArgumentExceptionThrown() {
        Puzzle sut = new Puzzle();
        Point validPoint = new Point(Puzzle.Size + 1, 0);

        sut.EraseNumber(validPoint);
    }

    @Test
    public void ValidPuzzle_PassedToCtor_AllPointsMatch() {
        Puzzle original = new Puzzle();
        original.SetNumber(new Point(1, 5), 5);
        original.SetNumber(new Point(3, 6), 7);
        original.SetNumber(new Point(2, 7), 8);

        Puzzle sut = new Puzzle(original);

        for (AllPoints points = new AllPoints(); points.hasNext(); ) {
            Point currentPoint = points.next();
            Assert.assertEquals(original.GetNumber(currentPoint), sut.GetNumber(currentPoint));
        }
    }

    @Test
    public void Valid2dArray_PassedToCtor_AllPointsMatch() {
        Integer[][] valid2dArray = new Integer[9][9];
        valid2dArray[1][5] = 5;
        valid2dArray[3][6] = 7;
        valid2dArray[2][7] = 8;

        Puzzle sut = new Puzzle(valid2dArray);

        for (AllPoints points = new AllPoints(); points.hasNext(); ) {
            Point currentPoint = points.next();
            Assert.assertEquals(valid2dArray[currentPoint.x][currentPoint.y], sut.GetNumber(currentPoint));
        }
    }
}
