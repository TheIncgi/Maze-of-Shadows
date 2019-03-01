package app.engine.entity;

import java.util.ArrayList;

import app.Game;
import app.engine.Engine;
import app.engine.tiles.BaseTile;
import app.misc.DoublePosition;
import app.misc.IntegerPosition;

public class Entity {
	DoublePosition pos;
	BoundingBox bounds;
	DoublePosition velocity = new DoublePosition(0d, 0d);
	ArrayList<IntegerPosition> tileIntersections = new ArrayList<IntegerPosition>((int)((Math.ceil(bounds.getWidth())+1 )*(Math.ceil(bounds.getHeight())+1))); //used to track when an entity enters or exits tiles
	
	/**Measured in tiles per game tick*/
	public static final double MAX_ENTITY_SPEED = .95;
	
	public boolean intersectsTile(IntegerPosition tilePos) {
		return intersectsTile(tilePos.getX(), tilePos.getY());
	}
	
	
	
	public double getWalkingSpeed() {
		return Engine.tilesPerSecond(2); //walk 2 tiles per second
	}
	public double getSprintingSpeed() {
		return getWalkingSpeed()*1.75;
	}
	
	/**
	 * Called each gameTick to update the entity's position
	 * */
	public void doMovement() {
		pos.addToSelf(velocity);
		tileCheck();
	}
	
	/**
	 * Check if the entity has entered or exited any tiles
	 * */
	protected void tileCheck() {
		tileIntersections.removeIf(tile->(exitedTile(tile)));
	}
	private boolean exitedTile(IntegerPosition tile) {
		if(intersectsTile(tile.getX(), tile.getY())) {
			Game.instance().getEngine().exitTile(this, tile);
			return true;
		}
		return false;
	}
	/**
	 * Use entities walking speed to travel in some direction
	 * */
	public void walk(double angle) {
		angle = Math.toRadians(angle);
		double s = getWalkingSpeed();
		velocity.set(s*Math.cos(angle), s*Math.sin(angle));
	}
	/**
	 * Use entities sprinting speed to travel in some direction
	 * */
	public void sprint(double angle) {
		angle = Math.toRadians(angle);
		double s = getSprintingSpeed();
		velocity.set(s*Math.cos(angle), s*Math.sin(angle));
	}
	/**
	 * Stops entities movement
	 * */
	public void stop() {
		velocity.set(0d, 0d);
	}
	
	/**Check if a entity intersects some tile*/
	private boolean intersectsTile(int tileX, int tileY) {
		double up =  pos.getY() - bounds.up;
		double down= pos.getY() + bounds.down;
		double left= pos.getX() - bounds.left;
		double right=pos.getX() + bounds.right;
		return BaseTile.intersectsTile(tileX, tileY, left, up) ||
			   BaseTile.intersectsTile(tileX, tileY, right, up)||
			   BaseTile.intersectsTile(tileX, tileY, left, down)||
			   BaseTile.intersectsTile(tileX, tileY, right, down);
	}
	/**Check if this entity moved by some offset intersects some tile*/
	private boolean intersectsTile(int tileX, int tileY, double eOffsetX, double eOffsetY) {
		double up =  pos.getY()+eOffsetY - bounds.up;
		double down= pos.getY()+eOffsetY + bounds.down;
		double left= pos.getX()+eOffsetX - bounds.left;
		double right=pos.getX()+eOffsetX + bounds.right;
		return BaseTile.intersectsTile(tileX, tileY, left, up) ||
			   BaseTile.intersectsTile(tileX, tileY, right, up)||
			   BaseTile.intersectsTile(tileX, tileY, left, down)||
			   BaseTile.intersectsTile(tileX, tileY, right, down);
	}
	
}
