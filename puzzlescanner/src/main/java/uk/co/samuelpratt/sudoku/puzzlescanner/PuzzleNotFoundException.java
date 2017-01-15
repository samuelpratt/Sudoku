package uk.co.samuelpratt.sudoku.puzzlescanner;

public class PuzzleNotFoundException extends Exception {
    public PuzzleNotFoundException(String message) {
        super(message);
    }
}