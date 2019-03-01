package app.engine.tiles;

import app.misc.Utils;
import javafx.scene.paint.Color;

public class Lighting {
	private Color color;
	private double[] rgb = new double[3];
	private boolean valid = false; //if needs to be recalculated
	public Lighting() {
		color = color.BLACK;
	}
	
	public void reset() {
		color = color.BLACK;
		rgb[0] = 0;
		rgb[1] = 0;
		rgb[2] = 0;
		valid = false;
	}
	public void add(Color color, double factor) {
		rgb[0] += color.getRed() * factor;
		rgb[1] += color.getGreen() * factor;
		rgb[2] += color.getBlue() * factor;
		valid = false;
	}
	
	public Color getColor() {
		if(!valid) {
			for(int i = 0; i<3; i++)
				rgb[i]=Utils.clamp(rgb[i], 0, 1);
			color = new Color(rgb[0], rgb[1], rgb[2], 1.0);
			valid = true;
		}
		return color;
	}
	
	@Override
	public String toString() {
		return String.format("Light: [Brightness: %.3f, Color: %s]", color.getBrightness(), color);
	}
}
