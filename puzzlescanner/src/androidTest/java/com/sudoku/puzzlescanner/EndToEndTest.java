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

        Mat mat = BitmapFixture.readBitMapFromResouce(R.drawable.sudoku);

        //Find The Puzzle
        PuzzleFinder finder = new PuzzleFinder(mat);
        Mat thresholdMat = finder.getThresholdMat();
        Mat largestBlobMat = finder.getLargestBlobMat();
        PuzzleOutLine puzzleOutline = finder.findOutLine();

        PuzzleExtractor extractor = new PuzzleExtractor(thresholdMat, largestBlobMat, puzzleOutline);

        Mat extractedPuzzle = extractor.getExtractedPuzzleMat();

        PuzzleParser sut = new PuzzleParser(extractedPuzzle, InstrumentationRegistry.getContext());

        Integer number;
        number = sut.getLetterForPosition(2, 0);

        Assert.assertEquals(Integer.valueOf(2), number);

    }
}
