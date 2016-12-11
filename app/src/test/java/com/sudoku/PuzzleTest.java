package com.sudoku;

import org.junit.Test;

public class PuzzleTest {

    @Test
    void Constructor_Valid2dArray_ValuesMatchOriginal() {
        Integer[][] array = GetValid2dArray();

        Puzzle sut = new Puzzle(array);

//        for(int y = 0; y < 9; y++)
//        {
//            for(int x = 0; x < 9; x++)
//            {
//                if(sut)
//            }
//        }
    }

    private Integer[][] GetValid2dArray() {
        Integer[][] intArray = new Integer[9][9];
        intArray[0][0] = 1;
        intArray[1][1] = 1;
        intArray[3][3] = 1;
        return intArray;
    }

}
