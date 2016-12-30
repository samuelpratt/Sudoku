package com.sudoku.puzzlescanner;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Mat;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class PuzzleParserTest {

    @Test
    public void validPuzzle_getMatForPosition_CorrectMatReturned() throws PuzzleNotFoundException, IOException {
        Mat extractedPuzzle = BitmapFixture.readBitMapFromResouce(R.drawable.extracted);

        PuzzleParser sut = new PuzzleParser(extractedPuzzle, InstrumentationRegistry.getContext());

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

    @Test
    public void validPuzzle_getSingleLetterForPositionX2Y0_returns2() throws PuzzleNotFoundException, IOException {
        Mat extractedPuzzle = BitmapFixture.readBitMapFromResouce(R.drawable.extracted);

        PuzzleParser sut = new PuzzleParser(extractedPuzzle, InstrumentationRegistry.getContext());

        Integer number = sut.getLetterForPosition(2, 0);

        Assert.assertEquals(Integer.valueOf(2), number);

    }

    @Test
    public void validPuzzle_getSingleLetterForPositionX0Y0_returnsEmptyString() throws PuzzleNotFoundException, IOException {
        Mat extractedPuzzle = BitmapFixture.readBitMapFromResouce(R.drawable.extracted);

        PuzzleParser sut = new PuzzleParser(extractedPuzzle, InstrumentationRegistry.getContext());

        Integer number = sut.getLetterForPosition(0, 0);

        Assert.assertNull(number);
    }
}
