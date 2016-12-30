package com.sudoku.puzzlescanner;


import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Mat;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class EndToEndTest {
    @Test
    public void validPuzzle_getLetterForPositionX2Y0_returns2() throws PuzzleNotFoundException, IOException {

        //Read in the test Puzzle
        Mat mat = BitmapFixture.readBitMapFromResouce(R.drawable.sudoku);

        //Find the Puzzle
        PuzzleFinder finder = new PuzzleFinder(mat);
        Mat thresholdMat = finder.getThresholdMat();
        Mat largestBlobMat = finder.getLargestBlobMat();
        PuzzleOutLine puzzleOutline = finder.findOutLine();

        //Extract the Puzzle
        PuzzleExtractor extractor = new PuzzleExtractor(thresholdMat, largestBlobMat, puzzleOutline);
        Mat extractedPuzzle = extractor.getExtractedPuzzleMat();

        //Parse the digit at x=2, y=0
        PuzzleParser sut = new PuzzleParser(extractedPuzzle, InstrumentationRegistry.getContext());
        Integer number;
        number = sut.getNumberForPosition(2, 0);

        //Assert
        Assert.assertEquals(Integer.valueOf(2), number);

    }
}
