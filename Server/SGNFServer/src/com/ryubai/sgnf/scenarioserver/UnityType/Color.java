package com.ryubai.sgnf.scenarioserver.UnityType;

public class Color {

	public float r = 1f;
	public float g = 1f;
	public float b = 1f;
	public float a = 1f;

	public Color() {
	}

	public Color(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = 1f;
	}

	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public static ColorHSV RGBToHSV(Color c){

		ColorHSV hsv = new ColorHSV();

		float min, max, delta;
		min = Math.min(Math.min(c.r, c.g), c.b);
	    max = Math.max(Math.max(c.r, c.g), c.b);
	    
		hsv.v = max;

		delta = max - min;

		if (max != 0)
			hsv.s = delta / max;
		else {
			hsv.s = 0;
			hsv.h = -1;
			return hsv;
		}
		if (c.r == max)
			hsv.h = (c.g - c.b) / delta; // between yellow & magenta
		else if (c.g == max)
			hsv.h = 2 + (c.b - c.r) / delta; // between cyan & yellow
		else
			hsv.h = 4 + (c.r - c.g) / delta; // between magenta & cyan

		hsv.h *= 60; // degrees
		if (hsv.h < 0)
			hsv.h += 360;

		return hsv;
	}
}
