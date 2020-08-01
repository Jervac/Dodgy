package com.dodgy.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class ColorUtil {
	
	public static void clearGray() {
		Gdx.gl.glClearColor(104 / 255f, 102 / 255f, 102 / 255f, 1);
	}
	
	public static void clearWhite() { Gdx.gl.glClearColor(244 / 255f, 246 / 255f, 245 / 255f, 1); }
	
	public static void clearBlack() { Gdx.gl.glClearColor(52 / 255f, 52 / 255f, 52 / 255f, 1); }

    public static void clearBlue() { Gdx.gl.glClearColor(33 / 255f, 171 / 255f, 181 / 255f, 1); }

	public static void clearBlu() { Gdx.gl.glClearColor(108 / 255f, 212 / 255f, 227 / 255f, 1); }

	public static Color blu0 = new Color(43/255f, 103/255f, 255/255f,1);
	public static Color blu0_dark = new Color(0/255f,21/255f,74/255f,1);
    public static Color red = new Color(255/255f,4/255f,48/255f,1);
    public static Color red_dark = new Color(70/255f,0/255f,12/255f,1);
    public static Color blu1 = new Color(21/255f,217/255f,255/255f,1);
    public static Color blu1_dark = new Color(0/255f,53/255f,64/255f,1);
    public static Color violet = new Color(181/255f,32/255f,255/255f,1);
    public static Color violet_dark = new Color(43/255f,0/255f,64/255f,1);
    public static Color pink = new Color(255/255f,32/255f,239/255f,1);
    public static Color pink_dark = new Color(64/255f,0/255f,59/255f,1);

}