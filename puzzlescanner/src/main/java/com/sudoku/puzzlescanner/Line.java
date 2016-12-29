package com.sudoku.puzzlescanner;

import org.opencv.core.Point;

enum Orientation {
    horizontal,
    vertical,
    fortyFiveDegree,
}

class Line {
    Point origin;
    Point destination;

    Line(Point origin, Point destination) {
        this.origin = origin;
        this.destination = destination;
    }

    Orientation getOrientation() {
        if (getHeight() == getwidth())
            return Orientation.fortyFiveDegree;
        if (getHeight() > getwidth())
            return Orientation.vertical;
        return Orientation.horizontal;
    }

    private double getHeight() {
        return getMaxY() - getMinY();
    }

    private double getwidth() {
        return getMaxX() - getMinX();
    }

    double getMinX() {
        if (origin.x < destination.x)
            return origin.x;
        return destination.x;
    }

    double getMaxX() {
        if (origin.x > destination.x)
            return origin.x;
        return destination.x;
    }

    double getMaxY() {
        if (origin.y > destination.y)
            return origin.y;
        return destination.y;
    }

    double getMinY() {
        if (origin.y < destination.y)
            return origin.y;
        return destination.y;
    }
}
