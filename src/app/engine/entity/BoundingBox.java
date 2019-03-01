package app.engine.entity;

import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

public class BoundingBox {
	/**
	 * Boundries are releative to the entity center
	 * lower bounds are inclusive
	 * */
	double left, right, up, down;
	public BoundingBox(double left, double up, double right, double down) {
		this.left = left;
		this.right = right;
		this.up = up;
		this.down = down;
	}
	
	public double getWidth() {
		return Math.abs(right+left);
	}
	public double getHeight() {
		return Math.abs(up+down);
	}
	
	/**
	 * Checks if the point x, y is contained by the bounding box
	 * these coordianates should be relative to the entities position
	 * */
	public boolean pointInside(double x, double y) {
		return -left <= x &&
			   x < right &&
			   -up <= y    &&
			   y < down;
	}
}
