package com.sudoku.puzzlescanner;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import static java.lang.Math.sin;
import static java.lang.Math.tan;
import static org.opencv.imgproc.Imgproc.ADAPTIVE_THRESH_MEAN_C;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;

public class PuzzleFinder {

    private static final Scalar WHITE = new Scalar(255);
    private static final Scalar BLACK = new Scalar(0);
    private static final Scalar GREY = new Scalar(64);
    private static final int GREY_INT = 64;
    private static final int THRESHOLD = 128;


    private Mat originalMat;
    private Mat greyMat;
    private Mat thresholdMat;
    private Mat largestBlobMat;
    private Mat houghLinesMat;

    public PuzzleFinder(Mat mat) {
        originalMat = mat;
    }

    public Mat getGreyMat() {
        if (greyMat == null) {
            generateGreyMat();
        }
        return greyMat;
    }

    private void generateGreyMat() {
        greyMat = originalMat.clone();
        Imgproc.cvtColor(originalMat, greyMat, Imgproc.COLOR_RGB2GRAY);
    }

    public Mat getThresholdMat() {
        if (thresholdMat == null) {
            generateThresholdMat();
        }
        return thresholdMat;
    }

    private void generateThresholdMat() {
        thresholdMat = getGreyMat().clone();
        Imgproc.adaptiveThreshold(thresholdMat, thresholdMat, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 7, 5);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ERODE, new Size(5, 5));
        Imgproc.erode(thresholdMat, thresholdMat, kernel);
        Core.bitwise_not(thresholdMat, thresholdMat);
    }

    public Mat getLargestBlob() {
        if (largestBlobMat == null) {
            generateLargestBlobMat();
        }
        return largestBlobMat;
    }

    private void generateLargestBlobMat() {


        largestBlobMat = getThresholdMat().clone();
        int height = largestBlobMat.height();
        int width = largestBlobMat.width();


        Point maxBlobOrigin = findLargestBlobOrigin(height, width);
        markLargestBlob(height, width, maxBlobOrigin);
        eraseOtherBlobs(height, width);
    }

    private Point findLargestBlobOrigin(int height, int width) {
        Point maxBlobOrigin = new Point(0, 0);

        int maxBlobSize = 0;
        Mat greyMask = new Mat(height + 2, width + 2, CvType.CV_8U, new Scalar(0, 0, 0));
        for (int y = 0; y < height; y++) {
            Mat row = largestBlobMat.row(y);
            for (int x = 0; x < width; x++) {
                double[] value = row.get(0, x);
                Point currentPoint = new Point(x, y);

                if (value[0] > THRESHOLD) {
                    int blobSize = Imgproc.floodFill(largestBlobMat, greyMask, currentPoint, GREY);
                    if (blobSize > maxBlobSize) {
                        maxBlobOrigin = currentPoint;
                        maxBlobSize = blobSize;
                    }
                }
            }
        }
        return maxBlobOrigin;
    }

    private void markLargestBlob(int height, int width, Point maxBlobOrigin) {
        Mat largeBlobMask = new Mat(height + 2, width + 2, CvType.CV_8U, BLACK);
        Imgproc.floodFill(largestBlobMat, largeBlobMask, maxBlobOrigin, WHITE);
    }

    private void eraseOtherBlobs(int height, int width) {
        Mat eraseMask = new Mat(height + 2, width + 2, CvType.CV_8U, BLACK);
        for (int y = 0; y < height; y++) {
            Mat row = largestBlobMat.row(y);
            for (int x = 0; x < width; x++) {
                double[] value = row.get(0, x);
                if (value[0] == GREY_INT) {
                    Imgproc.floodFill(largestBlobMat, eraseMask, new Point(x, y), BLACK);
                }
            }
        }
    }

    public Mat findPuzzleLocation(Mat thresholdMat) {
        Mat houghLinesMat = thresholdMat.clone();
        Mat linesMat = thresholdMat.clone();
        int width = thresholdMat.width();
        int height = thresholdMat.height();

        Imgproc.HoughLines(thresholdMat, houghLinesMat, (double) 1, Math.PI / 180, 200);

        int lines = houghLinesMat.rows();
        for (int x = 0; x < lines; x++) {
            double[] vec = houghLinesMat.get(x, 0);
            Vector vector = new Vector(vec[0], vec[1]);

            if (vector.theta != 0) {
                float m = (float) (-1 / tan(vector.theta));
                float c = (float) (vector.rho / sin(vector.theta));
                Imgproc.line(linesMat, new Point(0, c), new Point(width, m * width + c), GREY);
            } else {
                Imgproc.line(linesMat, new Point(vector.rho, 0), new Point(vector.rho, height), GREY);
            }


        }

        return linesMat;

    }
}
