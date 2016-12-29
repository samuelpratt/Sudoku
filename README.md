# Sudoku Solver

## What?

Sam's free time project to write a simple Android Sudoku solver App.

The idea is that you will be able to take a picture of the puzzle and the App will extract the puzzle
from the image and then solve it.

Please bear in mind that I'm: -

1. Not an App developer
2. Not a graphic designer

so please don't judge me too much :-) I know this looks a bit rough! The point was for me to have a play with some fun technologies rather than to produce a polished commercial App.

## How?

The idea is to use: -
* An off-the-shelf OCR library, e.g. Tesseract https://github.com/tesseract-ocr/tesseract
* A basic backtracking Sudoku solving algorithm

## //TODO:

* ~~Work out how to create a basic Android App~~
* ~~Take a picture and show a preview~~
* Get the image from the SD Card (look at https://developer.android.com/training/camera/photobasics.html#TaskPath)
* ~~Analyse the image to find the Sudoku grid (See below)~~
* Extract the Puzzle and read the numbers from the Sudoku grid
* ~~Solve the puzzle~~

## Extracting the puzzle from the image

This is by far the hardest part. The two best articles I found on this are: -

* http://aishack.in/tutorials/sudoku-grabber-opencv-detection/
* http://sudokugrab.blogspot.co.uk/2009/07/how-does-it-all-work.html

### Cleaning up the image

This involves: -

* Removing colour
* Thresholding the image to make it binary using an adaptive threshold to deal with shadows and variations in light
* Eroding the image (effectivly expanding lines) to fill in small gaps

<img src="./docs/original.png" height="250" width="250" >
<img src="./docs/threshold.png" height="250" width="250" >

### Finding the Grid

This involves: -

* Isolating the grid from the rest of the image (I used a simple floodfill agorthm based on the ImageShack article to find the largest connected area in the image)
* Finding edges using a Hough Transform (http://docs.opencv.org/2.4/doc/tutorials/imgproc/imgtrans/hough_lines/hough_lines.html)
* Calculating the corners of the images by : -
  * Finding outer edges (i.e. those nearest to the borders of the image)
  * Calcuating their intersections to find the corners (see http://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect)

<img src="./docs/largestBlob.png" height="250" width="250" >
<img src="./docs/houghLines.png" height="250" width="250" >
<img src="./docs/outline.png" height="250" width="250" >

### Extracting the Puzzle

This involves : -

* A bit more image clean up i.e. removing the grid that we found so that if some of it gets into the extracted image it
doesn't confuse the OCR algorthm.
* Stretching the image straight (http://docs.opencv.org/3.1.0/da/d6e/tutorial_py_geometric_transformations.html)

<img src="./docs/extracted.png" height="250" width="250" >

## Solving the puzzle.

This is pretty easy. You can use a backtracking algorithm which
will effectivly brute force the solution by trying every possible combination
of numbers until it finds a solution that satisfies the constraints. See the
followng links for more information: -

* https://see.stanford.edu/materials/icspacs106b/Lecture11.pdf
* https://en.wikipedia.org/wiki/Sudoku_solving_algorithms


## Environment

You will need: -

* Android Studio
* Android SDK 25 (Nougat)
* The Android Emulator
* OpenCV (see below)

### Installing OpenCV

As I didn't want to check this in you will need to do the following: -

* You will need to install the android version of OpenCV
from http://sourceforge.net/projects/opencvlibrary/files/opencv-android/3.1.0/OpenCV-3.1.0-android-sdk.zip/download: -
 * Use the instuctions from here http://blog.codeonion.com/2015/11/25/creating-a-new-opencv-project-in-android-studio/
 * Open CV should be imported as a module called "openCVLibraryxxx-1" where xxx is the version e.g. openCVLibrary310-1.
* In order to get the tests in the puzzlescanner module to run you will need to import the OpenCV native binaries into the project
as the unit tests don't use OpenCV manager. To do this: -
 * Copy the contents of OpenCV-android-sdk/sdk/native/libs to Sudoku/puzzlescanner/src/main/jnilibs/
