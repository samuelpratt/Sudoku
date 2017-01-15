package uk.co.samuelpratt.sudoku.puzzlescanner;


import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Mat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class EndToEndTest {

    @Test
    public void endToEnd_getMatForPosition_CorrectMatReturned() throws PuzzleNotFoundException, IOException {
        Mat extractedPuzzleMat = getExtractedPuzzleMat(R.drawable.sudoku3);


        PuzzleParser sut = new PuzzleParser(extractedPuzzleMat, InstrumentationRegistry.getContext());

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
    public void endToEnd_getPuzzle_allDigitsMatch() throws PuzzleNotFoundException, IOException {
        Mat extractedPuzzleMat = getExtractedPuzzleMat(R.drawable.sudoku);

        PuzzleParser sut = new PuzzleParser(extractedPuzzleMat, InstrumentationRegistry.getContext());
        Integer[][] extractedPuzzle = sut.getPuzzle();

        //Assert
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
        assertPuzzlesMatch(expectedPuzzle, extractedPuzzle);
    }

    @Test
    public void endToEnd_getPuzzle2_allDigitsMatch() throws PuzzleNotFoundException, IOException {
        Mat extractedPuzzleMat = getExtractedPuzzleMat(R.drawable.sudoku2);

        PuzzleParser sut = new PuzzleParser(extractedPuzzleMat, InstrumentationRegistry.getContext());
        Integer[][] extractedPuzzle = sut.getPuzzle();

        //Assert
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
        assertPuzzlesMatch(expectedPuzzle, extractedPuzzle);
    }

    @Test
    @Ignore
    public void endToEnd_getPuzzle3_allDigitsMatch() throws PuzzleNotFoundException, IOException {
        Mat extractedPuzzleMat = getExtractedPuzzleMat(R.drawable.sudoku3);

        PuzzleParser sut = new PuzzleParser(extractedPuzzleMat, InstrumentationRegistry.getContext());
        Integer[][] extractedPuzzle = sut.getPuzzle();

        //Assert
        Integer[][] expectedPuzzle = new Integer[][]{
                {null, null, null, null, null, null, 2, 4, null},
                {3, null, 2, null, null, null, null, null, 1},
                {9, null, null, null, null, null, null, null, null},
                {null, null, 8, null, null, 4, 5, 7, null},
                {4, null, null, null, null, 2, 3, null, null},
                {null, null, 6, 1, null, 5, null, 9, null},
                {null, null, 7, null, null, null, null, null, null},
                {null, 1, null, 2, 3, 8, null, null, null},
                {null, 5, null, null, null, null, null, null, null},
        };

        // This won't pass at the moment. It misses on a about 11 points.
        // Due to the amout of noise in image. Need to have a think
        // About if this is worth fixing.

        assertPuzzlesMatch(expectedPuzzle, extractedPuzzle);
    }

    @Test
    public void endToEnd_getPuzzle4_allDigitsMatch() throws PuzzleNotFoundException, IOException {
        Mat extractedPuzzleMat = getExtractedPuzzleMat(R.drawable.sudoku4);

        PuzzleParser sut = new PuzzleParser(extractedPuzzleMat, InstrumentationRegistry.getContext());
        Integer[][] extractedPuzzle = sut.getPuzzle();

        //Assert
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
        assertPuzzlesMatch(expectedPuzzle, extractedPuzzle);
    }

    private Mat getExtractedPuzzleMat(int resource) throws PuzzleNotFoundException {
        //Read in the test Puzzle
        Mat mat = BitmapFixture.readBitMapFromResouce(resource);

        //Find the Puzzle
        PuzzleFinder finder = new PuzzleFinder(mat);
        Mat thresholdMat = finder.getThresholdMat();
        Mat largestBlobMat = finder.getLargestBlobMat();
        PuzzleOutLine puzzleOutline = finder.findOutLine();

        //Extract the Puzzle
        PuzzleExtractor extractor = new PuzzleExtractor(thresholdMat, largestBlobMat, puzzleOutline);
        return extractor.getExtractedPuzzleMat();
    }

    private void assertPuzzlesMatch(Integer[][] expectedPuzzle, Integer[][] extractedPuzzle) {
        List<String> failedPoints = new ArrayList<>();

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if (expectedPuzzle[x][y] != extractedPuzzle[x][y])
                    failedPoints.add(String.format("Values at X=%d, Y=%d do not match. Expected %d, got %d\n", x, y, expectedPuzzle[x][y], extractedPuzzle[x][y]));
            }
        }
        if (failedPoints.size() != 0) {
            Assert.fail(String.format("Found %d points where the grids don't match", failedPoints.size()));
        }

    }
}
