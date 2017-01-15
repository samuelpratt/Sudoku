package uk.co.samuelpratt.sudoku.puzzlescanner;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Point;

@RunWith(AndroidJUnit4.class)

public class LineTest {
    @Test
    public void zeroDegreeLine_getAngleFromXAxis_returns0() {
        Line sut = new Line(new Point(0, 0), new Point(10, 0));

        double angle = sut.getAngleFromXAxis();

        Assert.assertEquals(0, angle, 1);
    }

    @Test
    public void nintyDegreeLine_getAngleFromXAxis_returns90() {
        Line sut = new Line(new Point(0, 0), new Point(0, 10));

        double angle = sut.getAngleFromXAxis();

        Assert.assertEquals(90, angle, 1);
    }

    @Test
    public void fortyFiveDegreeLine_getAngleFromXAxis_returns45() {
        Line sut = new Line(new Point(0, 0), new Point(10, 10));

        double angle = sut.getAngleFromXAxis();

        Assert.assertEquals(45, angle, 1);
    }
}
