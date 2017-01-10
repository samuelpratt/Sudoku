package com.sudoku;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.ImageView;

import com.sudoku.puzzlescanner.PuzzleScanner;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

//Based on https://developer.android.com/training/camera/photobasics.html
public class TakeAPictureActivity extends Activity{
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takeapicture);
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(null, "Error occurred while creating the File", ex);
            }
            // Continue only if the File was successfully created
            Uri photoURI = null;
            if (photoFile != null) {
                try {
                    photoURI = FileProvider.getUriForFile(getApplicationContext(), "com.sudoku.fileprovider", photoFile);
                } catch (Exception ex) {
                    Log.e(null, "An error occurred getting the URI for the image file", ex);
                }
                try {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (Exception ex) {
                    Log.e(null, "An error occurred taking the picture", ex);
                }

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (pictureWasTakenOk(requestCode, resultCode)) {

            processImage();
        } else {
            cancelAndReturnToMainActivity();
        }
    }

    private void processImage() {
        try {
            Bitmap imageBitmap = getCameraImageFromStorage();
            setImage(imageBitmap);

            PuzzleScanner puzzleScanner = new PuzzleScanner(imageBitmap, this.getApplicationContext());
            ImageView imageView = (ImageView)findViewById(R.id.PreviewImageView);

            String[] methodChain = new String[]{"getThreshold", "getLargestBlob", "getHoughLines", "getOutLine", "extractPuzzle"};

            UpdateImageTask updateImageTask = new UpdateImageTask(imageView, puzzleScanner, methodChain);
            updateImageTask.execute();
        } catch (Exception ex) {
            Log.e(null, "Error extracting puzzle", ex);
        }
    }

    private void setImage(Bitmap imageBitmap) throws Exception {
        ImageView imageView = (ImageView) findViewById(R.id.PreviewImageView);
        imageView.setImageBitmap(imageBitmap);
        imageView.invalidate();
    }

    private Bitmap getCameraImageFromStorage() {
        try {
            Thread.sleep(200);
        } catch (Exception ex) {
            Log.d(null, "error sleeping waiting for photo to be written");
        }
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        return BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
    }


    private void cancelAndReturnToMainActivity() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
    }

    private boolean pictureWasTakenOk(int requestCode, int resultCode) {
        return requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}

class UpdateImageTask extends AsyncTask<Void, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    private WeakReference<PuzzleScanner> puzzleScannerReference;
    private String[] methodChain;

    UpdateImageTask(ImageView imageView, PuzzleScanner puzzleScanner, String[] methodChain) {
        this.imageViewReference = new WeakReference<>(imageView);
        this.puzzleScannerReference = new WeakReference<>(puzzleScanner);
        this.methodChain = methodChain;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap result = null;
        Method[] allMethods = puzzleScannerReference.get().getClass().getDeclaredMethods();
        for (Method m : allMethods) {
            if (m.getName() == methodChain[0]) {
                try {
                    result = (Bitmap) m.invoke(puzzleScannerReference.get());
                } catch (Exception ex) {
                    Log.e(null, "error calling method", ex);
                }
                break;
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
                imageView.invalidate();
            }
        }
        String[] newMethodChain = getNewMethodChain(methodChain);
        if (newMethodChain.length == 0)
            return;
        UpdateImageTask chainTask = new UpdateImageTask(this.imageViewReference.get(), this.puzzleScannerReference.get(), newMethodChain);
        chainTask.execute();
    }

    private String[] getNewMethodChain(String[] methodChain) {
        if (methodChain.length < 2)
            return new String[0];

        String[] newMethodChain = new String[methodChain.length - 1];
        for (int i = 1; i < methodChain.length; i++) {
            newMethodChain[i - 1] = methodChain[i];
        }
        return newMethodChain;
    }
}
