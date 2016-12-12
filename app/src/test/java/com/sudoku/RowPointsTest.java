package com.sudoku;

import com.flextrade.jfixture.JFixture;

import org.junit.Assert;
import org.junit.Test;

public class RowPointsTest {
    @Test
    public void ValidRow_Next_RowMatches() {
        int row = 2;
        RowPoints sut = new RowPoints(row);

        Point p = sut.next();

        Assert.assertEquals(row, p.y);
    }

    @Test
    public void NewInstance_Next_ColumnIsZero() {
        JFixture fixture = new JFixture();
        int row = fixture.create(int.class);

        RowPoints sut = new RowPoints(row);

        Point p = sut.next();

        Assert.assertEquals(0, p.x);
    }

    @Test
    public void FirstPointRetrived_Next_ColumnIsOne() {
        JFixture fixture = new JFixture();
        int row = fixture.create(int.class);

        RowPoints sut = new RowPoints(row);

        sut.next();
        Point p = sut.next();

        Assert.assertEquals(1, p.x);
    }

    @Test
    public void NinePointsRetrieved_hasNext_ReturnsFalse() {
        JFixture fixture = new JFixture();
        int row = fixture.create(int.class);

        RowPoints sut = new RowPoints(row);

        for (int i = 0; i < 9; i++)
            sut.next();

        Assert.assertFalse(sut.hasNext());
    }
}
