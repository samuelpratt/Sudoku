package com.sudoku.puzzlescanner;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Mat;

@RunWith(AndroidJUnit4.class)
public class PuzzleFinderTest {

    @Test
    public void validImage_getGreyMat_ImageIsGreyScale() {
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
    public void validImage_getBlobMat_LargestBlobFound() {
        Mat mat = BitmapFixture.readBitMapFromResouce(R.drawable.sudoku);

        PuzzleFinder sut = new PuzzleFinder(mat);
        Mat blobMat = sut.getLargestBlobMat();
        BitmapFixture.writePngForMat(blobMat, new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    @Test
    public void validImage_getHoughLinesMat_LinesInCorrectPlace() {
        Mat mat = BitmapFixture.readBitMapFromResouce(R.drawable.sudoku);

        PuzzleFinder sut = new PuzzleFinder(mat);
        Mat linesMat = sut.getHoughLinesMat();
        BitmapFixture.writePngForMat(linesMat, new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

}
