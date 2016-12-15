package com.sudoku.puzzlescanner;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import static org.opencv.imgproc.Imgproc.ADAPTIVE_THRESH_MEAN_C;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;

public class PuzzleFinder {

    private Mat originalMat;
    private Mat greyMat;
    private Mat thresholdMat;
    private Mat blobMat;

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
        Imgproc.adaptiveThreshold(thresholdMat, thresholdMat, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 7, 2);
        Core.bitwise_not(thresholdMat, thresholdMat);
    }
}
