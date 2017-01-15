package uk.co.samuelpratt.sudoku;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import uk.co.samuelpratt.sudoku.puzzlesolver.Point;
import uk.co.samuelpratt.sudoku.puzzlesolver.Puzzle;


public class PuzzleAdaptor extends BaseAdapter {

    private final Context mContext;
    private Puzzle puzzle;

    public PuzzleAdaptor(Context context, Puzzle puzzle) {
        this.mContext = context;
        this.puzzle = puzzle;
    }

    @Override
    public int getCount() {
        return Puzzle.SIZE * Puzzle.SIZE;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    private Point getPoint(int position) {
        int x = (position % Puzzle.SIZE);
        int y = position / Puzzle.SIZE;

        return new Point(x, y);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.linearlayout_number, null);
        }

        GridView par = (GridView) parent;
        int width = ((GridView) parent).getColumnWidth();

        TextView textView = (TextView) convertView.findViewById(R.id.number);

        textView.getLayoutParams().height = width;
        textView.getLayoutParams().width = width;


        Point p = getPoint(position);
        Integer value = puzzle.getNumber(p);

        if (value == null)
            textView.setText("");
        else if (value == -1)
            textView.setText("?");
        else
            textView.setText(String.valueOf(value));
        return textView;
    }

}