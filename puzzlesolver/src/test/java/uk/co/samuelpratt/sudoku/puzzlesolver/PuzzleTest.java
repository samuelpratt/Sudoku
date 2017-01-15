package uk.co.samuelpratt.sudoku.puzzlesolver;

import org.junit.Assert;
import org.junit.Test;

public class PuzzleTest {

    @Test
    public void RowHasDuplicateNumber_NoConflicts_ReturnsFalse() {
        Point FirstRowFirstCol = new Point(0, 0);
        Point FirstRowSecondCol = new Point(1, 0);
        Integer value = 3;

        Puzzle sut = new Puzzle();

        sut.setNumber(FirstRowFirstCol, value);

        Assert.assertFalse(sut.noConflicts(FirstRowSecondCol, value));
    }

    @Test
    public void RowDoesNotHaveDuplicateNumber_NoConflicts_ReturnsTrue() {
        Point FirstRowFirstCol = new Point(0, 0);
        Point FirstRowSecondCol = new Point(1, 0);
        Integer value = 3;
        Integer otherValue = 4;

        Puzzle sut = new Puzzle();

        sut.setNumber(FirstRowFirstCol, value);

        Assert.assertTrue(sut.noConflicts(FirstRowSecondCol, otherValue));
    }

    @Test
    public void ColumnHasDuplicateNumber_NoConflicts_ReturnsFalse() {
        Point FirstRowFirstCol = new Point(0, 0);
        Point SecondRowFirstCol = new Point(0, 1);
        Integer value = 3;

        Puzzle sut = new Puzzle();

        sut.setNumber(FirstRowFirstCol, value);

        Assert.assertFalse(sut.noConflicts(SecondRowFirstCol, value));
    }

    @Test
    public void ColumnDoesNotHaveDuplicateNumber_NoConflicts_ReturnsTrue() {
        Point FirstRowFirstCol = new Point(0, 0);
        Point SecondRowFirstCol = new Point(0, 1);
        Integer value = 3;
        Integer otherValue = 4;

        Puzzle sut = new Puzzle();

        sut.setNumber(FirstRowFirstCol, value);

        Assert.assertTrue(sut.noConflicts(SecondRowFirstCol, otherValue));
    }

    @Test
    public void ValidNumberValidRange_SetNumber_NumberIsSet() {
        Puzzle sut = new Puzzle();
        Point validPoint = new Point(0, 0);
        Integer validNumber = 1;

        sut.setNumber(validPoint, validNumber);

        Assert.assertEquals(validNumber, sut.getNumber(validPoint));
    }

    @Test(expected = IllegalArgumentException.class)
    public void NumberTooBig_SetNumber_IllegalArgumentExceptionThrown() {
        Puzzle sut = new Puzzle();
        Point validPoint = new Point(0, 0);
        Integer invalidNumber = Puzzle.MAX_VALUE + 1;

        sut.setNumber(validPoint, invalidNumber);
    }

    @Test(expected = IllegalArgumentException.class)
    public void NumberTooSmall_SetNumber_IllegalArgumentExceptionThrown() {
        Puzzle sut = new Puzzle();
        Point validPoint = new Point(0, 0);
        Integer invalidNumber = Puzzle.MIN_VALUE - 1;

        sut.setNumber(validPoint, invalidNumber);
    }

    @Test(expected = IllegalArgumentException.class)
    public void InvalidRow_SetNumber_IllegalArgumentExceptionThrown() {
        Puzzle sut = new Puzzle();
        Point invalidPoint = new Point(0, Puzzle.SIZE + 1);
        Integer validNumber = 0;

        sut.setNumber(invalidPoint, validNumber);
    }

    @Test(expected = IllegalArgumentException.class)
    public void InvalidCol_SetNumber_IllegalArgumentExceptionThrown() {
        Puzzle sut = new Puzzle();
        Point invalidPoint = new Point(Puzzle.SIZE + 1, 0);
        Integer validNumber = 0;

        sut.setNumber(invalidPoint, validNumber);
    }

    @Test(expected = IllegalArgumentException.class)
    public void InvalidCol_GetNumber_IllegalArgumentExceptionThrown() {
        Puzzle sut = new Puzzle();
        Point invalidPoint = new Point(Puzzle.SIZE + 1, 0);
        sut.getNumber(invalidPoint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void InvalidRow_GetNumber_IllegalArgumentExceptionThrown() {
        Puzzle sut = new Puzzle();
        Point invalidPoint = new Point(0, Puzzle.SIZE + 1);
        sut.getNumber(invalidPoint);
    }

    @Test
    public void ValidPoint_EraseNumber_NumberErased() {
        Puzzle sut = new Puzzle();
        Point validPoint = new Point(0, 0);
        Integer validNumber = 7;
        sut.setNumber(validPoint, validNumber);

        sut.eraseNumber(validPoint);

        Assert.assertEquals(null, sut.getNumber(validPoint));
    }

    @Test(expected = IllegalArgumentException.class)
    public void InvalidPoint_EraseNumber_IllegalArgumentExceptionThrown() {
        Puzzle sut = new Puzzle();
        Point validPoint = new Point(Puzzle.SIZE + 1, 0);

        sut.eraseNumber(validPoint);
    }

    @Test
    public void ValidPuzzle_PassedToCtor_AllPointsMatch() {
        Puzzle original = new Puzzle();
        original.setNumber(new Point(1, 5), 5);
        original.setNumber(new Point(3, 6), 7);
        original.setNumber(new Point(2, 7), 8);

        Puzzle sut = new Puzzle(original);

        for (int x = 0; x < Puzzle.SIZE; x++) {
            for (int y = 0; y < Puzzle.SIZE; y++) {
                Point currentPoint = new Point(x, y);
                Assert.assertEquals(original.getNumber(currentPoint), sut.getNumber(currentPoint));
            }
        }
    }

    @Test
    public void Valid2dArray_PassedToCtor_AllPointsMatch() {
        Integer[][] valid2dArray = new Integer[9][9];
        valid2dArray[1][5] = 5;
        valid2dArray[3][6] = 7;
        valid2dArray[2][7] = 8;

        Puzzle sut = new Puzzle(valid2dArray);

        for (int x = 0; x < Puzzle.SIZE; x++) {
            for (int y = 0; y < Puzzle.SIZE; y++) {
                Point currentPoint = new Point(x, y);
                Assert.assertEquals(valid2dArray[currentPoint.x][currentPoint.y], sut.getNumber(currentPoint));
            }
        }
    }

}
