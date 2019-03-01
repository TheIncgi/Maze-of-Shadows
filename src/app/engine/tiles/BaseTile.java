package app.engine.tiles;

import java.util.List;

import app.misc.Position;
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
	
	/**Called when the player enters this tile*/
	public void onEnter(Position<Integer> tilePos) {}
	/**Called when the player exits this tile*/
	public void onExit(Position<Integer> tilePos) {}
	
	/**Returns this tiles renderer*/
	abstract public IDrawable getDrawable();
	
	public List<Emissive> getEmissives(Position<Integer> tilePos ){
		return null;
	}
	
}
