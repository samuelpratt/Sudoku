package uk.co.samuelpratt.sudoku.puzzlescanner;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Mat;

@RunWith(AndroidJUnit4.class)
public class PuzzleExtractorTest {

    @Test
    public void validOutline_generateExtractedPuzzleMat_PuzzleIsExtracted() throws Exception {
        setupValidExtractor();

        PuzzleExtractor sut = setupValidExtractor();

        Mat puzzleMat = sut.getExtractedPuzzleMat();

        BitmapFixture.writePngForMat(puzzleMat, new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    private PuzzleExtractor setupValidExtractor() throws PuzzleNotFoundException {
        Mat mat = BitmapFixture.readBitMapFromResouce(R.drawable.sudoku);
        PuzzleFinder finder = new PuzzleFinder(mat);
        Mat thresholdMat = finder.getThresholdMat();
        Mat largestBlobMat = finder.getLargestBlobMat();
        PuzzleOutLine puzzleOutline = finder.findOutLine();

        return new PuzzleExtractor(thresholdMat, largestBlobMat, puzzleOutline);
    }

}
