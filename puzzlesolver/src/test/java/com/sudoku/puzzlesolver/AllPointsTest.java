package com.sudoku.puzzlesolver;

import org.junit.Assert;
import org.junit.Test;


public class AllPointsTest {

    @Test
    public void NewInstance_GetElement_ReturnsTopLeft() {
        AllPoints sut = new AllPoints();

        Point firstElement = sut.next();

        Assert.assertEquals(0, firstElement.x);
        Assert.assertEquals(0, firstElement.y);
    }

    @Test
    public void InstanceWithOneElementEnumerated_GetElement_ReturnsTopRowSecondCol() {
        AllPoints sut = new AllPoints();
        sut.next();

        Point secondElement = sut.next();

        Assert.assertEquals(1, secondElement.x);
        Assert.assertEquals(0, secondElement.y);
    }

    @Test
    public void NewInstance_hasMoreElements_ReturnsTrue() {
        AllPoints sut = new AllPoints();

        Assert.assertTrue(sut.hasNext());
    }

    @Test
    public void EmptyInstance_hasMoreElements_ReturnsFalse() {
        AllPoints sut = new AllPoints();
        GetAllPoints(sut);

        Assert.assertFalse(sut.hasNext());

    }

    private void GetAllPoints(AllPoints sut) {
        for (int i = 0; i < 9 * 9; i++)
            sut.next();
    }

    @Test
    public void FirstRowRetrived_NextElement_ElementIsFirstColOfSecondRow() {
        AllPoints sut = new AllPoints();
        GetFirstRow(sut);

        Point element = sut.next();
        Assert.assertEquals(0, element.x);
        Assert.assertEquals(1, element.y);
    }

    private void GetFirstRow(AllPoints sut) {
        for (int i = 0; i < 9; i++)
            sut.next();
    }
}
