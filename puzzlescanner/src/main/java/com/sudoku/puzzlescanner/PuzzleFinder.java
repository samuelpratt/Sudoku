package com.sudoku.puzzlescanner;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.opencv.imgproc.Imgproc.ADAPTIVE_THRESH_MEAN_C;
import static org.opencv.imgproc.Imgproc.MARKER_TILTED_CROSS;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;

class PuzzleFinder {

    private static final Scalar WHITE = new Scalar(255);
    private static final Scalar BLACK = new Scalar(0);
    private static final Scalar GREY = new Scalar(64);
    private static final Scalar DARK_GREY = new Scalar(127);
    private static final int THRESHOLD = 128;


    private Mat originalMat;
    private Mat greyMat;
    private Mat thresholdMat;
    private Mat largestBlobMat;
    private Mat houghLinesMat;
    private Mat outLineMat;

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

    Mat getLargestBlobMat() {
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

    Mat getHoughLinesMat() {
        if (houghLinesMat == null)
            generateHoughLinesMat();
        return houghLinesMat;
    }

    private void generateHoughLinesMat() {

        houghLinesMat = getLargestBlobMat().clone();

        List<Line> houghLines = getHoughLines();
        for (Line line : houghLines) {
            Imgproc.line(houghLinesMat, line.origin, line.destination, GREY);
        }
    }

    private List<Line> getHoughLines() {
        Mat linesMat = getLargestBlobMat().clone();
        int width = thresholdMat.width();
        int height = thresholdMat.height();

        //Need to think about the threshold as getting this correct is very important!
        Imgproc.HoughLines(thresholdMat, linesMat, (double) 1, Math.PI / 180, 600);

        //The Hough transform returns a series of lines in Polar format this is returned in the
        //form of a Mat where each row is a vector where row[0] is rho and row[1] is theta
        //See http://docs.opencv.org/2.4/doc/tutorials/imgproc/imgtrans/hough_lines/hough_lines.html
        //and http://stackoverflow.com/questions/7925698/android-opencv-drawing-hough-lines/7975315#7975315
        List<Line> houghLines = new ArrayList<>();
        int lines = linesMat.rows();
        for (int x = 0; x < lines; x++) {
            double[] vec = linesMat.get(x, 0);
            Vector vector = new Vector(vec[0], vec[1]);


            Point origin = new Point();
            Point destination = new Point();
            double a = cos(vector.theta), b = sin(vector.theta);
            double x0 = a * vector.rho, y0 = b * vector.rho;
            origin.x = (x0 + width * (-b));
            origin.y = (y0 + height * (a));
            destination.x = (x0 - width * (-b));
            destination.y = (y0 - height * (a));

            Line line = new Line(origin, destination);


            houghLines.add(line);
        }
        return houghLines;
    }


    Point findIntersection(Line line1, Line line2) {
        //See http://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect
        double line1DeltaX = line1.destination.x - line1.origin.x;
        double line1DeltaY = line1.destination.y - line1.origin.y;
        double line2DeltaX = line2.destination.x - line2.origin.x;
        double line2DeltaY = line2.destination.y - line2.origin.y;

        double linesDeltaOriginX = line1.origin.x - line2.origin.x;
        double linesDeltaOriginY = line1.origin.y - line2.origin.y;

        double denominator = line1DeltaX * line2DeltaY - line2DeltaX * line1DeltaY;
        double numeratorS = line1DeltaX * linesDeltaOriginY - line1DeltaY * linesDeltaOriginX;
        double numeratorT = line2DeltaX * linesDeltaOriginY - line2DeltaY * linesDeltaOriginX;

        double t = numeratorT / denominator;

        boolean denominatorPositive = denominator > 0;

        boolean intersectionDetected = isIntersectionDetected(denominator, numeratorS, numeratorT, denominatorPositive);
        //if (!intersectionDetected)
        //   return null;

        return calculateIntersection(line1, line1DeltaX, line1DeltaY, t);
    }

    private boolean isIntersectionDetected(double denominator, double numeratorS, double numeratorT, boolean denominatorPositive) {
        boolean intersectionDetected = true;

        if (denominator == 0)
            intersectionDetected = false; // Collinear

        if ((numeratorS < 0) == denominatorPositive)
            intersectionDetected = false; // No collision


        if ((numeratorT < 0) == denominatorPositive)
            intersectionDetected = false; // No collision

        if (((numeratorS > denominator) == denominatorPositive) || ((numeratorT > denominator) == denominatorPositive))
            intersectionDetected = false; // No collision
        return intersectionDetected;
    }

    private Point calculateIntersection(Line line1, double line1DeltaX, double line1DeltaY, double t) {
        Point intersection = new Point();
        intersection.x = line1.origin.x + (t * line1DeltaX);
        intersection.y = line1.origin.y + (t * line1DeltaY);
        return intersection;
    }

    PuzzleOutLine findOutLine() throws PuzzleNotFoundException {

        PuzzleOutLine location = new PuzzleOutLine();

        int countHorizontalLines = 0;
        int countVerticalLines = 0;

        List<Line> houghLines = getHoughLines();

        for (Line line : houghLines) {
            if (line.getOrientation() == Orientation.horizontal) {
                countHorizontalLines++;
                if (location.top == null) {
                    location.top = line;
                    location.bottom = line;
                    continue;
                }
                if (line.getMinY() < location.bottom.getMinY())
                    location.bottom = line;
                if (line.getMaxY() > location.top.getMaxY())
                    location.top = line;
            } else if (line.getOrientation() == Orientation.vertical) {
                countVerticalLines++;
                if (location.left == null) {
                    location.left = line;
                    location.right = line;
                    continue;
                }
                if (line.getMinX() < location.left.getMinX())
                    location.left = line;
                if (line.getMaxX() > location.right.getMaxX())
                    location.right = line;
            } else {
                continue;
            }
        }

        if (houghLines.size() < 4)
            throw new PuzzleNotFoundException("not enough possible edges found. Need at least 4 for a rectangle.");
        if (countHorizontalLines < 2)
            throw new PuzzleNotFoundException("not enough horizontal edges found. Need at least 2 for a rectangle.");
        if (countVerticalLines < 2)
            throw new PuzzleNotFoundException("not enough vertical edges found. Need at least 2 for a rectangle.");


        location.topLeft = findIntersection(location.top, location.left);
        if (location.topLeft == null)
            throw new PuzzleNotFoundException("Cannot find top left corner");

        location.topRight = findIntersection(location.top, location.right);
        if (location.topRight == null)
            throw new PuzzleNotFoundException("Cannot find top right corner");

        location.bottomLeft = findIntersection(location.bottom, location.left);
        if (location.topLeft == null)
            throw new PuzzleNotFoundException("Cannot find bottom left corner");

        location.bottomRight = findIntersection(location.bottom, location.right);
        if (location.topLeft == null)
            throw new PuzzleNotFoundException("Cannot find bottom right corner");

        return location;
    }

    Mat getOutLineMat() throws PuzzleNotFoundException {
        if (outLineMat == null)

            generateOutlineMat();
        return outLineMat;
    }

    void generateOutlineMat() throws PuzzleNotFoundException {
        outLineMat = getThresholdMat().clone();

        PuzzleOutLine location = findOutLine();

        Imgproc.drawMarker(outLineMat, location.topLeft, GREY, MARKER_TILTED_CROSS, 10, 5, 8);
        Imgproc.drawMarker(outLineMat, location.topRight, GREY, MARKER_TILTED_CROSS, 10, 5, 8);
        Imgproc.drawMarker(outLineMat, location.bottomLeft, GREY, MARKER_TILTED_CROSS, 10, 5, 8);
        Imgproc.drawMarker(outLineMat, location.bottomRight, GREY, MARKER_TILTED_CROSS, 10, 2, 8);

        Imgproc.line(outLineMat, location.top.origin, location.top.destination, GREY);
        Imgproc.line(outLineMat, location.bottom.origin, location.bottom.destination, DARK_GREY);
        Imgproc.line(outLineMat, location.left.origin, location.left.destination, GREY);
        Imgproc.line(outLineMat, location.right.origin, location.right.destination, DARK_GREY);
    }

}

