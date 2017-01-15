package uk.co.samuelpratt.sudoku.puzzlescanner;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.util.ArrayList;
import java.util.List;

import static uk.co.samuelpratt.sudoku.puzzlescanner.Constants.BLACK;

class PuzzleExtractor {

    private Mat thresholdMat;
    private Mat largestBlobMat;
    private PuzzleOutLine puzzleOutline;

    private Mat extractedPuzzleMat;

    PuzzleExtractor(Mat thresholdMat, Mat largestBlobMat, PuzzleOutLine puzzleOutLine) {
        this.thresholdMat = thresholdMat;
        this.largestBlobMat = largestBlobMat;
        this.puzzleOutline = puzzleOutLine;
    }

    Mat getExtractedPuzzleMat() {
        if (extractedPuzzleMat == null)
            generateExtractedPuzzleMat();

        return extractedPuzzleMat;
    }

    private void generateExtractedPuzzleMat() {
        extractedPuzzleMat = thresholdMat.clone();
        RemovePuzzleOutline();
        CorrectPerspective();
    }

    private void CorrectPerspective() {
        double size = puzzleOutline.getSize();

        Mat outputMat = new Mat((int) size, (int) size, CvType.CV_8U);

        List<Point> source = new ArrayList<Point>();
        source.add(puzzleOutline.bottomLeft);
        source.add(puzzleOutline.topLeft);
        source.add(puzzleOutline.topRight);
        source.add(puzzleOutline.bottomRight);
        Mat startM = Converters.vector_Point2f_to_Mat(source);

        Point bottomLeft = new Point(0, 0);
        Point topLeft = new Point(0, size);
        Point topRight = new Point(size, size);
        Point bottomRight = new Point(size, 0);
        List<Point> dest = new ArrayList<Point>();
        dest.add(bottomLeft);
        dest.add(topLeft);
        dest.add(topRight);
        dest.add(bottomRight);
        Mat endM = Converters.vector_Point2f_to_Mat(dest);

        Mat perspectiveTransform = Imgproc.getPerspectiveTransform(startM, endM);

        Imgproc.warpPerspective(extractedPuzzleMat,
                outputMat,
                perspectiveTransform,
                new Size(size, size),
                Imgproc.INTER_CUBIC);
        extractedPuzzleMat = outputMat;
    }


    private void RemovePuzzleOutline() {
        int height = thresholdMat.height();
        int width = thresholdMat.width();
        for (int y = 0; y < height; y++) {
            Mat row = largestBlobMat.row(y);
            for (int x = 0; x < width; x++) {
                double[] value = row.get(0, x);
                Point currentPoint = new Point(x, y);
                if (value[0] > Constants.THRESHOLD) {
                    Mat blackMask = new Mat(height + 2, width + 2, CvType.CV_8U, new Scalar(0, 0, 0));
                    Imgproc.floodFill(extractedPuzzleMat, blackMask, currentPoint, BLACK);
                    return;
                }
            }
        }
    }
}
