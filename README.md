# Sudoku Solver

## What?

Sam's free time project to write a simple Android Sudoku solver. Please bear in mind that I'm: -

1. Not an App developer
2. Not a graphic designer

so please don't judge me too much :-) I know this looks a bit rough! The point was for me to have a play with some fun technologies rather than to produce a polished commercial App.

## How?

The idea is to use: -
* An off-the-shelf OCR library, e.g. Tesseract https://github.com/tesseract-ocr/tesseract
* A basic backtracking Sudoku solving algorithm https://see.stanford.edu/materials/icspacs106b/Lecture11.pdf

## //TODO:

* ~~Work out how to create a basic Android App~~
* ~~Take a picture and show a preview~~
* Get the image from the SD Card (look at https://developer.android.com/training/camera/photobasics.html#TaskPath)
* Analyse the image to get the Sudoku grid (look at http://sudokugrab.blogspot.co.uk/2009/07/how-does-it-all-work.html)
* ~~Solve the puzzle~~

## Environment

You will need: -

* Android Studio
* Android SDK 25 (Nougat)
* You will need to install the android version of OpenCV
from http://sourceforge.net/projects/opencvlibrary/files/opencv-android/3.1.0/OpenCV-3.1.0-android-sdk.zip/download
using the instuctions from here http://blog.codeonion.com/2015/11/25/creating-a-new-opencv-project-in-android-studio/

Open CV should be imported as a module called "openCVLibraryxxx" where xxx is the version e.g. openCVLibrary 310.