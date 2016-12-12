package com.sudoku;

import java.util.Iterator;
import java.util.Stack;

abstract class Points implements Iterator<Point> {
    protected Stack<Point> points;

    Points() {
        points = new Stack<>();
    }

    @Override
    public boolean hasNext() {
        return !points.isEmpty();
    }

    @Override
    public Point next() {
        return points.pop();
    }
}
