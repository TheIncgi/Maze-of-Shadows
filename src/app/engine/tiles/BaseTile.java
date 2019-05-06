package app.engine.tiles;

import java.util.List;

import app.engine.entity.Entity;
import app.misc.IntegerPosition;
import app.ui.elements.IDrawable;

/**
 * A single instance of each tile is created during startup then reused
 * many times throughout the map
 */
abstract public class BaseTile {
	abstract public boolean isPassable();
	/**
	 * true if light should not be calculated inside the tile
	 * */
	abstract public boolean isOpaque();
	
	/**Called when any entity enters this tile*/
	public void onEnter(Entity e, IntegerPosition tilePos) {}
	/**Called when any entity exits this tile*/
	public void onExit(Entity e, IntegerPosition tilePos) {}
	
	/**Returns this tiles renderer*/
	abstract public IDrawable getDrawable();
	
	public List<Emissive> getEmissives(IntegerPosition tilePos ){
		return null;
	}
	
	@Override
	public String toString() {
		return String.format("BaseTile: [passable: %s, opaque: %s, resImg: %s]", String.valueOf(isPassable()),String.valueOf(isOpaque()), getDrawable().getResourceName());
	}
	
//	private static BoundingBox tileBound = new BoundingBox(0, 0, 1, 1);
//	public static boolean intersectsTile(int tx, int ty, double x, double y) {
//		return tileBound.pointInside(x-tx, y-ty);
//	}
}
