package uk.co.samuelpratt.sudoku.puzzlescanner;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;

import org.junit.Assert;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

public class BitmapFixture {

    static {
        if (!OpenCVLoader.initDebug()) {
            Assert.fail("OpenCV Did not init");
        }

    }

    public static void writePngForMat(Mat write, String fileName) {
        Context context = InstrumentationRegistry.getContext();
        File path = context.getExternalFilesDir(null);
        assert path != null;
        if (!path.exists()) path.mkdirs();

        File file = new File(path, fileName + ".png");
        if (!Imgcodecs.imwrite(file.getAbsolutePath(), write))
            Assert.fail("Write failed");
    }

    public static Mat readBitMapFromResouce(int resource) {
        Context testContext = InstrumentationRegistry.getContext();

        Resources testRes = testContext.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(testRes, resource);
        Mat mat = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8SC1);
        Utils.bitmapToMat(bitmap, mat);
        Imgproc.resize(mat, mat, new Size(0, 0), .4, .4, Imgproc.INTER_LINEAR);
        return mat;
    }
}
