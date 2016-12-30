package com.sudoku.puzzlescanner;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Mat;

@RunWith(AndroidJUnit4.class)
public class PuzzleParserTests {

    @Test
    public void validPuzzle_getMatForPosition_CorrectMatReturned() throws PuzzleNotFoundException {
        Mat extractedPuzzle = getExtractedPuzzle();

        PuzzleParser sut = new PuzzleParser(extractedPuzzle);

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                Mat digit = sut.getMatForPosition(x, y);
                String imageName = new Object() {
                }.getClass().getEnclosingMethod().getName();
                imageName = imageName + "y=" + y + "x=" + x;

                BitmapFixture.writePngForMat(digit, imageName);
            }
        }
    }

    private Mat getExtractedPuzzle() throws PuzzleNotFoundException {
        Mat mat = BitmapFixture.readBitMapFromResouce(R.drawable.sudoku);
        PuzzleFinder finder = new PuzzleFinder(mat);
        Mat thresholdMat = finder.getThresholdMat();
        Mat largestBlobMat = finder.getLargestBlobMat();
        PuzzleOutLine puzzleOutline = finder.findOutLine();

        PuzzleExtractor extractor = new PuzzleExtractor(thresholdMat, largestBlobMat, puzzleOutline);

        return extractor.getExtractedPuzzleMat();
    }


}
