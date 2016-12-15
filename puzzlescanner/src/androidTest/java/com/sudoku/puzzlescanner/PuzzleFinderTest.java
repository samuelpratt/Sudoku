package com.sudoku.puzzlescanner;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

@RunWith(AndroidJUnit4.class)
public class PuzzleFinderTest {

    @Test
    public void ValidImage_getGreyMat_ImageIsGreyScale() {
        Mat mat = BitmapFixture.readBitMapFromResouce(R.drawable.sudoku);
        PuzzleFinder sut = new PuzzleFinder(mat);
        Mat greyMat = sut.getGreyMat();
        BitmapFixture.writePngForMat(greyMat, new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    @Test
    public void validImage_getThresholdMat_ImageIsThresholded() {
        Mat mat = BitmapFixture.readBitMapFromResouce(R.drawable.sudoku);
        PuzzleFinder sut = new PuzzleFinder(mat);
        Mat thresholdMat = sut.getThresholdMat();
        BitmapFixture.writePngForMat(thresholdMat, new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    @Test
    public void FloodFillTest() {
        Mat mat = BitmapFixture.readBitMapFromResouce(R.drawable.sudoku);
        PuzzleFinder sut = new PuzzleFinder(mat);
        Mat thresholdMat = sut.getThresholdMat();

        Mat mask = new Mat(thresholdMat.height() + 2, thresholdMat.width() + 2, CvType.CV_8U); //
        for (int x = 0; x < mask.width(); x++) {
            for (int y = 0; y < mask.height(); y++) {
                mask.put(y, x, 0);
            }
        }
        Imgproc.floodFill(thresholdMat, mask, new Point(0, 0), new Scalar(0));

        BitmapFixture.writePngForMat(thresholdMat, new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

}
