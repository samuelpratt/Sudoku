package com.sudoku.solver;

import com.flextrade.jfixture.JFixture;

import org.junit.Assert;
import org.junit.Test;

public class ColumnPointsTest {

    @Test
    public void ValidColumn_Next_ColumnMatches() {
        int column = 2;
        ColumnPoints sut = new ColumnPoints(column);

        Point p = sut.next();

        Assert.assertEquals(column, p.x);
    }

    @Test
    public void NewInstance_Next_RowIsZero() {
        JFixture fixture = new JFixture();
        int column = fixture.create(int.class);

        ColumnPoints sut = new ColumnPoints(column);

        Point p = sut.next();

        Assert.assertEquals(0, p.y);
    }

    @Test
    public void FirstPointRetrived_Next_RowIsOne() {
        JFixture fixture = new JFixture();
        int column = fixture.create(int.class);

        ColumnPoints sut = new ColumnPoints(column);

        sut.next();
        Point p = sut.next();

        Assert.assertEquals(1, p.y);
    }

    @Test
    public void NinePointsRetrieved_hasNext_IsFalse() {
        JFixture fixture = new JFixture();
        int column = fixture.create(int.class);

        ColumnPoints sut = new ColumnPoints(column);

        for (int i = 0; i < 9; i++)
            sut.next();

        Assert.assertFalse(sut.hasNext());
    }
}

