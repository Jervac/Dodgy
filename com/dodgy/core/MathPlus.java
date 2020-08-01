package com.dodgy.core;

import java.util.Random;

public class MathPlus
{

    public static int randInt(int min, int max)
    {
        Random rand = new Random();
        int number = rand.nextInt((max - min) + 1) + min;
        return number;
    }

    public static boolean randBool()
    {
        int trueOrFalse = randInt(0, 1);
        if(trueOrFalse == 0)
            return false;
        else
            return true;
    }


}