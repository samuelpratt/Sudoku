package uk.co.samuelpratt.sudoku.puzzlescanner;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Mat;
import org.opencv.core.Point;

@RunWith(AndroidJUnit4.class)
public class PuzzleFinderTest {

    @Test
    public void validImage_getGreyMat_ImageIsGreyScale() {
        Mat mat = BitmapFixture.readBitMapFromResouce(R.drawable.sudoku3);
        PuzzleFinder sut = new PuzzleFinder(mat);
        Mat greyMat = sut.getGreyMat();
        BitmapFixture.writePngForMat(greyMat, new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    @Test
    public void validImage_getThresholdMat_ImageIsThresholded() {
        Mat mat = BitmapFixture.readBitMapFromResouce(R.drawable.sudoku3);
        PuzzleFinder sut = new PuzzleFinder(mat);
        Mat thresholdMat = sut.getThresholdMat();
        BitmapFixture.writePngForMat(thresholdMat, new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    @Test
    public void validImage_getBlobMat_LargestBlobFound() {
        Mat mat = BitmapFixture.readBitMapFromResouce(R.drawable.sudoku3);

        PuzzleFinder sut = new PuzzleFinder(mat);
        Mat blobMat = sut.getLargestBlobMat();
        BitmapFixture.writePngForMat(blobMat, new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    @Test
    public void validImage_getHoughLinesMat_LinesInCorrectPlace() {
        Mat mat = BitmapFixture.readBitMapFromResouce(R.drawable.sudoku3);

        PuzzleFinder sut = new PuzzleFinder(mat);
        Mat linesMat = sut.getHoughLinesMat();
        BitmapFixture.writePngForMat(linesMat, new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    @Test
    public void validImage_getOutlineMat_CornersInCorrectPlace() throws PuzzleNotFoundException {
        Mat mat = BitmapFixture.readBitMapFromResouce(R.drawable.sudoku3);

        PuzzleFinder sut = new PuzzleFinder(mat);
        Mat outlineMat = sut.getOutLineMat();
        BitmapFixture.writePngForMat(outlineMat, new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    @Test
    public void linesAreParallel_findIntersection_returnsNull() {
        Line line1 = new Line(new Point(0, 0), new Point(10, 0));
        Line line2 = new Line(new Point(0, 10), new Point(10, 10));

        Point intersection = line1.findIntersection(line2);

        Assert.assertNull(intersection);
    }

    @Test
    public void linesAreCollinear_findIntersection_returnsNull() {
        Line line1 = new Line(new Point(0, 0), new Point(10, 0));
        Line line2 = new Line(new Point(0, 0), new Point(20, 0));

        Point intersection = line1.findIntersection(line2);

        Assert.assertNull(intersection);
    }

    @Test
    public void linesIntersect_findIntersection_returnsIntersection() {
        Line line1 = new Line(new Point(0, 5), new Point(10, 5));
        Line line2 = new Line(new Point(5, 0), new Point(5, 10));

        Point intersection = line1.findIntersection(line2);

        Assert.assertEquals(new Point(5, 5), intersection);
    }

    @Test
    public void negativeOriginlinesIntersect_findIntersection_returnsIntersection() {
        Line line1 = new Line(new Point(-10, 5), new Point(10, 5));
        Line line2 = new Line(new Point(5, -10), new Point(5, 10));

        Point intersection = line1.findIntersection(line2);

        Assert.assertEquals(new Point(5, 5), intersection);
    }

    @Test
    public void backwardslinesIntersect_findIntersection_returnsIntersection() {
        Line line1 = new Line(new Point(10, 5), new Point(-10, 5));
        Line line2 = new Line(new Point(5, 10), new Point(5, -10));

        Point intersection = line1.findIntersection(line2);

        Assert.assertEquals(new Point(5, 5), intersection);
    }

}
