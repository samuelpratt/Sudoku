package uk.co.samuelpratt.sudoku.puzzlescanner;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class PuzzleParserTest {

    @Test
    public void validPuzzle_getMatForPosition_CorrectMatReturned() throws Exception {
        Mat extractedPuzzle = BitmapFixture.readBitMapFromResouce(R.drawable.extracted);
        Imgproc.cvtColor(extractedPuzzle, extractedPuzzle, Imgproc.COLOR_RGB2GRAY);

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
    public void validPuzzle_getSingleLetterForPositionX2Y0_returns2() throws Exception {
        Mat extractedPuzzle = BitmapFixture.readBitMapFromResouce(R.drawable.extracted);
        Imgproc.cvtColor(extractedPuzzle, extractedPuzzle, Imgproc.COLOR_RGB2GRAY);

        PuzzleParser sut = new PuzzleParser(extractedPuzzle, InstrumentationRegistry.getContext());

        Integer number = sut.getNumberForPosition(2, 0);

        Assert.assertEquals(Integer.valueOf(2), number);

    }

    @Test
    public void validPuzzle_getSingleLetterForPositionX0Y0_returnsEmptyString() throws Exception {
        Mat extractedPuzzle = BitmapFixture.readBitMapFromResouce(R.drawable.extracted);
        Imgproc.cvtColor(extractedPuzzle, extractedPuzzle, Imgproc.COLOR_RGB2GRAY);

        PuzzleParser sut = new PuzzleParser(extractedPuzzle, InstrumentationRegistry.getContext());

        Integer number = sut.getNumberForPosition(0, 0);

        Assert.assertNull(number);
    }

    @Test
    public void validExtractedPuzzle_getPuzzle_correctDigitsReturned() throws Exception {
        Integer[][] expectedPuzzle = new Integer[][]{
                {null, null, null, 9, null, 5, 2, null, 7},
                {null, null, 1, null, null, 4, 6, 8, null},
                {2, null, null, null, null, null, null, null, 1},
                {null, null, 3, null, 5, null, null, null, null},
                {null, null, 8, 3, null, 2, null, 4, null},
                {5, null, null, null, 7, null, null, null, null},
                {9, null, null, null, null, 3, null, 7, null},
                {null, null, 6, null, null, null, null, 5, null},
                {3, null, null, null, 8, null, null, null, null},
        };
        int puzzle = R.drawable.extracted;

        parsePuzzle(expectedPuzzle, puzzle);
    }

    @Test
    public void validExtractedPuzzle4_getPuzzle_correctDigitsReturned() throws Exception {
        Integer[][] expectedPuzzle = new Integer[][]{
                {8, null, null, null, null, null, null, null, null},
                {null, null, 7, 5, null, null, null, null, 9},
                {null, 3, null, null, null, null, 1, 8, null},
                {null, 6, null, null, null, 1, null, 5, null},
                {null, null, 9, null, 4, null, null, null, null},
                {null, null, null, 7, 5, null, null, null, null},
                {null, null, 2, null, 7, null, null, null, 4},
                {null, null, null, null, null, 3, 6, 1, null},
                {null, null, null, null, null, null, 8, null, null},
        };
        int puzzle = R.drawable.extracted4;

        parsePuzzle(expectedPuzzle, puzzle);
    }

    private void parsePuzzle(Integer[][] expectedPuzzle, int puzzle) throws IOException, PuzzleNotFoundException {
        Mat extractedPuzzle = BitmapFixture.readBitMapFromResouce(puzzle);
        Imgproc.cvtColor(extractedPuzzle, extractedPuzzle, Imgproc.COLOR_RGB2GRAY);

        PuzzleParser sut = new PuzzleParser(extractedPuzzle, InstrumentationRegistry.getContext());

        Integer[][] foundPuzzle = sut.getPuzzle();

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if (expectedPuzzle[x][y] != foundPuzzle[x][y])
                    Assert.fail(String.format("Values at X=%d, Y=%d do not match. Expected %d, got %d", x, y, expectedPuzzle[x][y], foundPuzzle[x][y]));
            }
        }
    }
}
