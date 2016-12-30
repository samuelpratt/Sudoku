package com.sudoku.puzzlescanner;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import static com.sudoku.puzzlescanner.Constants.NUMBER_BORDER;

public class PuzzleParser {

    private Mat puzzleMat;
    private int puzzleHeight;
    private int puzzleWidth;

    PuzzleParser(Mat puzzleMat) {
        this.puzzleMat = puzzleMat;
        this.puzzleHeight = puzzleMat.height();
        this.puzzleWidth = puzzleMat.width();
    }

    Mat getMatForPosition(int x, int y) {

        int incrementX = puzzleWidth / 9;
        int incrementY = puzzleHeight / 9;

        int startX = (incrementX * x) - NUMBER_BORDER;
        if (startX < 0)
            startX = 0;

        int startY = (incrementY) * y - NUMBER_BORDER;
        if (startY < 0)
            startY = 0;

        int width = incrementX + NUMBER_BORDER;
        if (startX + width > puzzleWidth)
            width = puzzleWidth - startX;

        int height = incrementY + NUMBER_BORDER;
        if (startY + height > puzzleHeight)
            height = puzzleHeight - startY;


        Rect rect = new Rect(startX, startY, width, height);
        return new Mat(puzzleMat, rect);
    }
}
