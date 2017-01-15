package uk.co.samuelpratt.sudoku.puzzlescanner;

import android.content.Context;
import android.graphics.Bitmap;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class PuzzleScanner {
    private final Mat originalMat;
    private final Context context;

    private PuzzleFinder puzzleFinder;
    private PuzzleExtractor puzzleExtractor;
    private PuzzleParser puzzleParser;

    public PuzzleScanner(Bitmap bitmap, Context context) throws Exception {
        initOpencv();

        this.context = context;
        this.originalMat = convertAndResizeBitmap(bitmap);
    }

    private PuzzleFinder getPuzzleFinder() {
        if (puzzleFinder == null) {
            puzzleFinder = new PuzzleFinder(originalMat);
        }
        return puzzleFinder;
    }

    private PuzzleExtractor getPuzzleExtractor() throws PuzzleNotFoundException {
        if (puzzleExtractor == null) {
            PuzzleFinder finder = getPuzzleFinder();
            puzzleExtractor = new PuzzleExtractor(finder.getThresholdMat(), finder.getLargestBlobMat(), finder.findOutLine());
        }
        return puzzleExtractor;
    }

    private PuzzleParser getPuzzleParser() throws IOException, PuzzleNotFoundException {
        if (puzzleParser == null) {
            PuzzleExtractor extractor = getPuzzleExtractor();
            puzzleParser = new PuzzleParser(extractor.getExtractedPuzzleMat(), context);
        }
        return puzzleParser;
    }

    private void initOpencv() throws Exception {
        if (!OpenCVLoader.initDebug()) {
            throw new Exception("OpenCv did not init properly");
        }
    }

    public Bitmap getThreshold() {
        Mat thresholdMat = getPuzzleFinder().getThresholdMat();
        return convertMatToBitMap(thresholdMat);
    }

    public Bitmap getLargestBlob() {
        Mat largestBlobMat = getPuzzleFinder().getLargestBlobMat();
        return convertMatToBitMap(largestBlobMat);
    }

    public Bitmap getHoughLines() {
        Mat houghLinesMat = getPuzzleFinder().getHoughLinesMat();
        return convertMatToBitMap(houghLinesMat);
    }

    public Bitmap getOutLine() throws PuzzleNotFoundException {
        Mat outLineMat = getPuzzleFinder().getOutLineMat();
        return convertMatToBitMap(outLineMat);
    }

    public Bitmap extractPuzzle() throws PuzzleNotFoundException {
        Mat extractedPuzzleMat = getPuzzleExtractor().getExtractedPuzzleMat();
        return convertMatToBitMap(extractedPuzzleMat);
    }

    public Integer[][] getPuzzle() throws IOException, PuzzleNotFoundException {
        return getPuzzleParser().getPuzzle();
    }

    private Mat convertAndResizeBitmap(Bitmap bitmap) {
        Mat mat = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8SC1);
        Utils.bitmapToMat(bitmap, mat);
        Imgproc.resize(mat, mat, new Size(1080, 1440));
        return mat;
    }

    private Bitmap convertMatToBitMap(Mat matToConvert) {
        Bitmap bitmap = Bitmap.createBitmap(matToConvert.cols(), matToConvert.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(matToConvert, bitmap);
        return bitmap;
    }
}
