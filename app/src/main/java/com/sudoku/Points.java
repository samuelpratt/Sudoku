package com.sudoku;

import java.util.Iterator;
import java.util.Stack;

class Points implements Iterator<Point> {
    private Stack<Point> points;

    Points() {
        points = new Stack<>();
        for (int y = 8; y >= 0; y--) {
            for (int x = 8; x >= 0; x--) {
                points.push(new Point(x, y));
            }
        }
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
