package app.engine.entity;

import app.Game;
import app.engine.Engine;
import app.engine.tiles.BaseTile;
import app.misc.DoublePosition;
import app.misc.IntegerPosition;
import app.ui.elements.GameHUD;
import app.ui.elements.IDrawable;
import app.ui.elements.MapPane;
import javafx.application.Platform;
import javafx.scene.paint.Color;

public abstract class Entity implements TickListener {
	DoublePosition pos  = new DoublePosition(0,0);
	//BoundingBox bounds = new BoundingBox(0, 0, 1, 1);
	IntegerPosition lastTilePos = null;
	//BoundingBox visualBounds;
	DoublePosition velocity = new DoublePosition(0d, 0d);
	//ArrayList<IntegerPosition> tileIntersections = new ArrayList<IntegerPosition>((int)((Math.ceil(bounds.getWidth())+1 )*(Math.ceil(bounds.getHeight())+1))); //used to track when an entity enters or exits tiles
	
	/**Measured in tiles per game tick*/
	public static final double MAX_ENTITY_SPEED = 200;
	public static final double baseSpeed = 100;
	
//	public boolean intersectsTile(IntegerPosition tilePos) {
//		return intersectsTile(tilePos.getX(), tilePos.getY());
//	}
	
	public DoublePosition getPos() {
		return pos;
	}
	
	/**return speed in tiles per tick*/
	public double getWalkingSpeed() {
		return Engine.tilesPerSecond(baseSpeed); //walk 2 tiles per second
	}
	/**returns sprinting speed in tiles per tick*/
	public double getSprintingSpeed() {
		return getWalkingSpeed()*1.75;
	}
	
	
	/**
	 * Called each game tick to update the entity's position
	 * */
	public final void doMovement() {
		MapPane mapView = Game.instance().getLevelView().getMapPane();
		double movementScale = mapView.pixelsPerTile();
		

		double x =  (pos.getFloorX() / movementScale) - 3;
		double y =  (pos.getFloorY() / movementScale) - 3;
		BaseTile current = Game.instance().getEngine().getMap().getTile((int)x, (int)y);
		double nextX = Math.floor(x + velocity.getX()/movementScale);
		double nextY = Math.floor(y +velocity.getY()/movementScale );
		BaseTile next = Game.instance().getEngine().getMap().getTile( (int)nextX, (int)nextY );
		
		
		
		
		if( next == null || next.isPassable() )
			pos.addToSelf(velocity);	
		

		
		
		int currentX = (int) Math.floor(x + velocity.getX()/movementScale);
		int currentY = (int) Math.floor(y +velocity.getY()/movementScale );
		if(lastTilePos == null) {
			lastTilePos = new IntegerPosition(currentX, currentY);
			enteredTile(lastTilePos);
		}else {
			if(lastTilePos.getX() != currentX || lastTilePos.getY() != currentY) {
				exitedTile(lastTilePos);
				lastTilePos.set(currentX, currentY);
				enteredTile(lastTilePos);
			}
		}

	}
	
//	/**
//	 * Check if the entity has entered or exited any tiles
//	 * */
//	protected void tileCheck() {
//		tileIntersections.removeIf(tile->(exitedTile(tile)));
//	}
	private void enteredTile(IntegerPosition tile) {
		Game.instance().getEngine().enterTile(this, tile);
	}
	private void exitedTile(IntegerPosition tile) {
		Game.instance().getEngine().exitTile(this, tile);
	}
	/**
	 * Use entities walking speed to travel in some direction\
	 * Angle is in radians
	 * */
	public void walk(double angle) {
//		angle = Math.toRadians(angle);
		double s = getWalkingSpeed();
		velocity.set(s*Math.cos(angle), s*Math.sin(angle));
	}
	/**
	 * Use entities sprinting speed to travel in some direction
	 * */
	public void sprint(double angle) {
		double s = getSprintingSpeed();
		velocity.set(s*Math.cos(angle), s*Math.sin(angle));
	}
	/**
	 * Stops entities movement
	 * */
	public void stop() {
		velocity.set(0d, 0d);
	}
	
//	/**Check if a entity intersects some tile*/
//	private boolean intersectsTile(int tileX, int tileY) {
//		double up =  pos.getY() - bounds.up;
//		double down= pos.getY() + bounds.down;
//		double left= pos.getX() - bounds.left;
//		double right=pos.getX() + bounds.right;
//		return BaseTile.intersectsTile(tileX, tileY, left, up) ||
//			   BaseTile.intersectsTile(tileX, tileY, right, up)||
//			   BaseTile.intersectsTile(tileX, tileY, left, down)||
//			   BaseTile.intersectsTile(tileX, tileY, right, down);
//	}
//	/**Check if this entity moved by some offset intersects some tile*/
//	private boolean intersectsTile(int tileX, int tileY, double eOffsetX, double eOffsetY) {
//		double up =  pos.getY()+eOffsetY - bounds.up;
//		double down= pos.getY()+eOffsetY + bounds.down;
//		double left= pos.getX()+eOffsetX - bounds.left;
//		double right=pos.getX()+eOffsetX + bounds.right;
//		return BaseTile.intersectsTile(tileX, tileY, left, up) ||
//			   BaseTile.intersectsTile(tileX, tileY, right, up)||
//			   BaseTile.intersectsTile(tileX, tileY, left, down)||
//			   BaseTile.intersectsTile(tileX, tileY, right, down);
//	}
//	
	abstract public IDrawable getDrawable();
	
	
	abstract public void onDeath();


	
	
}
