package com.sudoku.puzzlescanner;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.opencv.imgproc.Imgproc.ADAPTIVE_THRESH_MEAN_C;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;

class PuzzleFinder {

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

    PuzzleFinder(Mat mat) {
        originalMat = mat;
    }

    Mat getGreyMat() {
        if (greyMat == null) {
            generateGreyMat();
        }
        return greyMat;
    }

    private void generateGreyMat() {
        greyMat = originalMat.clone();
        Imgproc.cvtColor(originalMat, greyMat, Imgproc.COLOR_RGB2GRAY);
    }

    Mat getThresholdMat() {
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

    Mat getLargestBlob() {
        if (largestBlobMat == null) {
            generateLargestBlobMat();
        }
        return largestBlobMat;
    }

    private void generateLargestBlobMat() {
        largestBlobMat = getThresholdMat().clone();
        int height = largestBlobMat.height();
        int width = largestBlobMat.width();


        Point maxBlobOrigin = new Point(0, 0);

        int maxBlobSize = 0;
        Mat greyMask = new Mat(height + 2, width + 2, CvType.CV_8U, new Scalar(0, 0, 0));
        Mat blackMask = new Mat(height + 2, width + 2, CvType.CV_8U, new Scalar(0, 0, 0));
        for (int y = 0; y < height; y++) {
            Mat row = largestBlobMat.row(y);
            for (int x = 0; x < width; x++) {
                double[] value = row.get(0, x);
                Point currentPoint = new Point(x, y);

                if (value[0] > THRESHOLD) {
                    int blobSize = Imgproc.floodFill(largestBlobMat, greyMask, currentPoint, GREY);
                    if (blobSize > maxBlobSize) {
                        Imgproc.floodFill(largestBlobMat, blackMask, maxBlobOrigin, BLACK);
                        maxBlobOrigin = currentPoint;
                        maxBlobSize = blobSize;
                    } else {
                        Imgproc.floodFill(largestBlobMat, blackMask, currentPoint, BLACK);
                    }
                }
            }
        }
        Mat largeBlobMask = new Mat(height + 2, width + 2, CvType.CV_8U, BLACK);
        Imgproc.floodFill(largestBlobMat, largeBlobMask, maxBlobOrigin, WHITE);

    }

    Mat findPuzzleLocation(Mat thresholdMat) {
        Mat houghLinesMat = thresholdMat.clone();
        Mat linesMat = thresholdMat.clone();
        int width = thresholdMat.width();
        int height = thresholdMat.height();

        Imgproc.HoughLines(thresholdMat, houghLinesMat, (double) 1, Math.PI / 180, 500);

        //The Hough transform returns a series of lines in Polar format this is returned in the
        //form of a Mat where each row is a vector where row[0] is rho and row[1] is theta
        //See http://docs.opencv.org/2.4/doc/tutorials/imgproc/imgtrans/hough_lines/hough_lines.html
        //and http://stackoverflow.com/questions/7925698/android-opencv-drawing-hough-lines/7975315#7975315
        int lines = houghLinesMat.rows();
        for (int x = 0; x < lines; x++) {
            double[] vec = houghLinesMat.get(x, 0);
            Vector vector = new Vector(vec[0], vec[1]);


            Point origin = new Point();
            Point destination = new Point();
            double a = cos(vector.theta), b = sin(vector.theta);
            double x0 = a * vector.rho, y0 = b * vector.rho;
            origin.x = (x0 + width * (-b));
            origin.y = (y0 + height * (a));
            destination.x = (x0 - width * (-b));
            destination.y = (y0 - height * (a));

            Imgproc.line(linesMat, origin, destination, GREY);
        }

        return linesMat;

    }
}
