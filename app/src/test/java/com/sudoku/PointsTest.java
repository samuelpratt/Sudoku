package com.sudoku;

import org.junit.Assert;
import org.junit.Test;


public class PointsTest {

    @Test
    public void NewInstance_GetElement_ReturnsTopLeft() {
        Points sut = new Points();

        Point firstElement = sut.next();

        Assert.assertEquals(0, firstElement.x);
        Assert.assertEquals(0, firstElement.y);
    }

    @Test
    public void InstanceWithOneElementEnumerated_GetElement_ReturnsTopRowSecondCol() {
        Points sut = new Points();
        sut.next();

        Point secondElement = sut.next();

        Assert.assertEquals(1, secondElement.x);
        Assert.assertEquals(0, secondElement.y);
    }

    @Test
    public void NewInstance_hasMoreElements_ReturnsTrue() {
        Points sut = new Points();

        Assert.assertTrue(sut.hasNext());
    }

    @Test
    public void EmptyInstance_hasMoreElements_ReturnsFalse() {
        Points sut = new Points();
        GetAllPoints(sut);

        Assert.assertFalse(sut.hasNext());

    }

    private void GetAllPoints(Points sut) {
        for (int i = 0; i < 9 * 9; i++)
            sut.next();
    }

    @Test
    public void FirstRowRetrived_NextElement_ElementIsFirstColOfSecondRow() {
        Points sut = new Points();
        GetFirstRow(sut);

        Point element = sut.next();
        Assert.assertEquals(0, element.x);
        Assert.assertEquals(1, element.y);
    }

    private void GetFirstRow(Points sut) {
        for (int i = 0; i < 9; i++)
            sut.next();
    }
}
