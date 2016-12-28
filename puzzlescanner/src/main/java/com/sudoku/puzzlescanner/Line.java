package com.sudoku.puzzlescanner;

import org.opencv.core.Point;

public class Line {
    Point origin;
    Point destination;

    Line(Point origin, Point destination) {
        this.origin = origin;
        this.destination = destination;
    }
}
