package com.sudoku.puzzlescanner;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PuzzleParser {

    private static final int NUMBER_BORDER = 5;
    private static final String TESS_LANG = "eng";
    private static final String TESS_DATA_DIR = "tessdata";
    private static final String TESS_TRAINING_FILE = "eng.traineddata";

    private static final int PUZZLE_SIZE = 9;

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

        Mat numberMat = extractNumberMatFromPuzzle(x, y);
        cleanUpNumberMat(numberMat);
        return numberMat;
    }

    private void cleanUpNumberMat(Mat numberMat) {
        Imgproc.cvtColor(numberMat, numberMat, Imgproc.COLOR_RGB2GRAY);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ERODE, new Size(5, 5));
        Imgproc.erode(numberMat, numberMat, kernel);
        Imgproc.dilate(numberMat, numberMat, kernel);
        Core.bitwise_not(numberMat, numberMat);
    }

    @NonNull
    private Mat extractNumberMatFromPuzzle(int x, int y) {
        int incrementX = puzzleWidth / PUZZLE_SIZE;
        int incrementY = puzzleHeight / PUZZLE_SIZE;

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

    Integer getNumberForPosition(int x, int y) throws PuzzleNotFoundException {
        Mat squareMat = getMatForPosition(x, y);

        Bitmap squareBmp = Bitmap.createBitmap(squareMat.cols(), squareMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(squareMat, squareBmp);

        tessBaseAPI.setImage(squareBmp);
        String textFound = tessBaseAPI.getUTF8Text();

        if (textFound == null || textFound.isEmpty())
            return null;

        Integer result;

        try {
            result = Integer.parseInt(textFound);
        } catch (Exception ex) {
            throw new PuzzleNotFoundException("String '" + textFound + "' couldn't be converted to an integer");
        }
        return result;
    }

    Integer[][] getPuzzle() throws PuzzleNotFoundException {
        Integer[][] result = new Integer[9][9];

        for (int x = 0; x < PUZZLE_SIZE; x++) {
            for (int y = 0; y < PUZZLE_SIZE; y++) {
                result[x][y] = getNumberForPosition(x, y);
            }
        }
        return result;
    }

    private void setUpTessTwo() throws IOException {
        initTrainingDataDir();
        copyTrainingDataIfRequired();
        initTestTwo();

    }

    private void initTestTwo() {
        tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.init(context.getExternalFilesDir(null).getAbsolutePath(), TESS_LANG);
        tessBaseAPI.setVariable("tessedit_char_whitelist", "0123456789");
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
