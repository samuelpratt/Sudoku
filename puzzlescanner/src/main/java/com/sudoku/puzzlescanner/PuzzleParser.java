package com.sudoku.puzzlescanner;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Rect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.sudoku.puzzlescanner.Constants.NUMBER_BORDER;
import static com.sudoku.puzzlescanner.Constants.TESS_DATA_DIR;
import static com.sudoku.puzzlescanner.Constants.TESS_LANG;
import static com.sudoku.puzzlescanner.Constants.TESS_TRAINING_FILE;

public class PuzzleParser {

    private Context context;
    private Mat puzzleMat;
    private int puzzleHeight;
    private int puzzleWidth;
    private TessBaseAPI tessBaseAPI;

    PuzzleParser(Mat puzzleMat, Context context) throws IOException {
        this.puzzleMat = puzzleMat;
        this.puzzleHeight = puzzleMat.height();
        this.puzzleWidth = puzzleMat.width();
        this.context = context;

        setUpTessTwo();
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

    Integer getLetterForPosition(int x, int y) {
        Mat squareMat = getMatForPosition(x, y);

        MatOfByte matOfByte = new MatOfByte();
        Bitmap squareBmp = Bitmap.createBitmap(squareMat.cols(), squareMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(squareMat, squareBmp);
        tessBaseAPI.setImage(squareBmp);
        String textFound = tessBaseAPI.getUTF8Text();

        if (textFound == null || textFound.isEmpty())
            return null;

        return Integer.parseInt(textFound);

    }

    private void setUpTessTwo() throws IOException {
        initTrainingDataDir();
        copyTrainingDataIfRequired();
        initTestTwo();

    }

    private void initTestTwo() {
        tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.init(context.getExternalFilesDir(null).getAbsolutePath(), TESS_LANG);
    }

    private void copyTrainingDataIfRequired() throws IOException {
        if (!getTrainingDataFile().exists()) {
            copyTrainingFile();
        }
    }

    private void copyTrainingFile() throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream instream = assetManager.open(getTrainingDataAssetFileName());
        OutputStream outstream = new FileOutputStream(getTrainingDataFile());
        byte[] buffer = new byte[1024];
        int read;
        while ((read = instream.read(buffer)) != -1) {
            outstream.write(buffer, 0, read);
        }
        outstream.flush();
        outstream.close();
        instream.close();
    }

    private String getTrainingDataAssetFileName() {

        return TESS_DATA_DIR + "/" + TESS_TRAINING_FILE;

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void initTrainingDataDir() {
        File trainingDataDir = getTrainingDataDir();
        if (!trainingDataDir.exists())
            trainingDataDir.mkdirs();
    }

    private File getTrainingDataFile() {
        return new File(getTrainingDataDir(), TESS_TRAINING_FILE);
    }

    private File getTrainingDataDir() {
        File path = context.getExternalFilesDir(null);
        return new File(path, TESS_DATA_DIR);
    }


}
