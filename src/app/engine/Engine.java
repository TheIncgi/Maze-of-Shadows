package app.engine;

import app.engine.entity.Entity;
import app.engine.tiles.BaseTile;
import app.misc.IntegerPosition;

public class Engine {
	
	Map map;
	
	//engine tick phases
	//tile, entity movement, entity status
	
	/**return the distance traveled each game tick at the speed nTiles/sec*/
	public static double tilesPerSecond(double nTiles) {
		return nTiles/Engine.ticksPerSecond();
	}
	public static double ticksPerSecond() {
		return 10;
	}
	
	public void enterTile(Entity entity, IntegerPosition tilePos) {
		BaseTile tile = map.getTile(tilePos);
		tile.onEnter(entity, tilePos);
	}
	public void exitTile(Entity entity, IntegerPosition tilePos) {
		BaseTile tile = map.getTile(tilePos);
		tile.onExit(entity, tilePos);
	}
}
