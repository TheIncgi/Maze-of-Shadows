package app.engine.tiles;

import app.misc.DoublePosition;
import javafx.scene.paint.Color;

abstract public class Emissive {
	public static double lightScale = 1; // used to fade in and out, affects brightness
	
	public static final double STANDARD_LIGHT_HEIGHT = 1;
	public static final double STANDARD_LIGHT_BRIGHTNESS = 2;
	public static final double STANDARD_LIGHT_FOCUS = 3;
	
	public Color getLightColor() {return Color.WHITE;}
	/**changes how quickly light fades out (smooth)*/
	public double lightHeight() {return STANDARD_LIGHT_HEIGHT;}
	
	/**controls the distance that light stays at full strength*/
	public double brightness() {return lightScale * STANDARD_LIGHT_BRIGHTNESS;}
	
	/**Changes how sharp the edge of a bright light should be<br>
	 * Used for spotlights / focused light sources*/
	public double getFocus() {return STANDARD_LIGHT_FOCUS;}
	
	/**Amount the light value can randomly vary by (effects each tile seperatly)<br>
	 * Very small amounts are recommended (like .02)*/
	public double flickerAmount() {
		return 0;
	}
	/**How long until a light randomly changes again*/
	public int flickerTime() {
		return 50;
	}
	abstract public DoublePosition getSource();
}
