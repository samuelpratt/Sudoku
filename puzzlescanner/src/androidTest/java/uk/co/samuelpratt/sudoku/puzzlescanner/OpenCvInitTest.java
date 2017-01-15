package uk.co.samuelpratt.sudoku.puzzlescanner;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

@RunWith(AndroidJUnit4.class)
public class OpenCvInitTest {

    static {
        if (!OpenCVLoader.initDebug()) {
            Assert.fail("OpenCV Did not init");
        }

    }

    @Test
    public void InitOpenCv_CallOpenCv_FileWritten() {

        //Arrange
        Bitmap bmp = getTestBitmap();

        //Act - This will throw a linker error if OpenCV is not correctly initilised
        Mat tmp = convertBitMapToMat(bmp);

        //Act - This will fail if the image cannot be written
        WritePngForMat(tmp);
    }

    private void WritePngForMat(Mat tmp) {
        Context context = InstrumentationRegistry.getContext();
        File path = context.getExternalFilesDir(null);
        assert path != null;
        if (!path.exists()) path.mkdirs();

        File file = new File(path, "image.png");
        if (!Imgcodecs.imwrite(file.getAbsolutePath(), tmp))
            Assert.fail("Write failed");
    }

    private Mat convertBitMapToMat(Bitmap bmp) {
        Mat tmp = new Mat(bmp.getWidth(), bmp.getHeight(), CvType.CV_8SC4);
        Utils.bitmapToMat(bmp, tmp);
        Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_RGB2GRAY);
        Utils.matToBitmap(tmp, bmp);
        return tmp;
    }

    private Bitmap getTestBitmap() {
        Context testContext = InstrumentationRegistry.getContext();

        Resources testRes = testContext.getResources();
        return BitmapFactory.decodeResource(testRes, R.drawable.sudoku);
    }

    @Test
    public void SdCard_getAbsolutePathCanWrite_True() {
        Context testContext = InstrumentationRegistry.getContext();
        File sdCard = testContext.getExternalFilesDir(null);
        Boolean canWrite = sdCard.canWrite();
        Assert.assertNotNull(canWrite);
        Assert.assertTrue(canWrite);
    }

    @Test
    public void ScCard_IsMounted_True() {
        String storageState = Environment.getExternalStorageState();
        Assert.assertEquals("mounted", storageState);

    }
}
