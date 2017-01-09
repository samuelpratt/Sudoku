package com.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;

import com.sudoku.puzzlesolver.Point;
import com.sudoku.puzzlesolver.Puzzle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = (GridView) findViewById(R.id.gridView1);
        Puzzle puzzle = new Puzzle();
        puzzle.setNumber(new Point(0, 0), 1);
        puzzle.setNumber(new Point(8, 0), 2);
        puzzle.setNumber(new Point(0, 8), 3);
        puzzle.setNumber(new Point(8, 8), 7);
        PuzzleAdaptor booksAdapter = new PuzzleAdaptor(this, puzzle);
        gridView.setAdapter(booksAdapter);
    }

    public void TakeAPicture(View v) throws Exception {
        Intent takeAPictureIntent = new Intent(this, TakeAPictureActivity.class);
        startActivity(takeAPictureIntent);
    }
}
