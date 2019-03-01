package app.engine.tiles;

import app.misc.Position;
import javafx.scene.paint.Color;

abstract public class Emissive {
	public static final double STANDARD_LIGHT_HEIGHT = 1;
	public static final double STANDARD_LIGHT_BRIGHTNESS = 1;
	
	public Color getLightColor() {return Color.WHITE;}
	/**changes how quickly light fades out*/
	public double lightHeight() {return STANDARD_LIGHT_HEIGHT;}
	
	/**controls the distance that light stays at full strength*/
	public double lightBrightness() {return STANDARD_LIGHT_BRIGHTNESS;}
	
	abstract public Position<Double> getSource();
}
