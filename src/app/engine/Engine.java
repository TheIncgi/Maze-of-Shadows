package app.engine;

import java.util.LinkedList;

import javax.management.RuntimeErrorException;
import javax.sql.rowset.spi.SyncResolver;

import app.engine.entity.Entity;
import app.engine.entity.Player;
import app.engine.entity.TickListener;
import app.engine.entity.Ticking;
import app.engine.tiles.BaseTile;
import app.misc.IntegerPosition;

public class Engine {
	
	Map map;
	private LinkedList<TickListener> tickListeners = new LinkedList<>();
	//add and remove in a thread safe way, also allows for adding during tick event without concurrent mod ex
	private LinkedList<TickListener> toAdd = new LinkedList<>(), toRemove = new LinkedList<>();
	//engine tick phases
	//tile, entity movement, entity status
	Thread engineThread;
	volatile boolean running = false;
	//skip ticks while loading a map so entities don't move through unloaded maps
	volatile boolean freeze = false;
	public Engine() {
		
	}
	
	public void start() {
		engineThread = new Thread(()-> {
			int tickDelay = (int) (1000/ticksPerSecond());
			long now = System.currentTimeMillis();
			long nextTick = now + tickDelay;
			while( running ) {
				now = System.currentTimeMillis();
				nextTick+=tickDelay;
				tick( now );
				if(nextTick > now)
					try {
						Thread.sleep( nextTick - now );
					} catch (InterruptedException e) {
						running = false;
						throw new RuntimeException("Sleep has been interupted by another thread");
					}
				else if( (now - nextTick)/tickDelay > 500 )
					throw new RuntimeException("The system is too running slow! Fell 500+ game ticks behind schedule!");
			}
		}, "Engine Thread"); 
		running = true;
		engineThread.start();
	}
	
	public void stop() {
		engineThread.stop();
	}
	
	private void tick( long now ) {
		if(freeze) return;
		
		synchronized(tickListeners) {
			synchronized (toAdd) {
				tickListeners.addAll( toAdd );
				toAdd.clear();
			}
			synchronized (toRemove) {
				tickListeners.removeAll( toRemove );
				toRemove.clear();
			}
		}
		
		for(TickListener t : tickListeners) {
			t.onTick( now );
		}
	}
	
	public void addTickListner(TickListener listener) {
		synchronized(toAdd) {
			toAdd.add(listener);
		}
	}
	public void removeTickListener(TickListener listener) {
		synchronized (toRemove) {
			toRemove.add(listener);
		}
	}
	public void clearTickListeners() {
		synchronized (toRemove) {
			toRemove.addAll(tickListeners);
		}
	}
	
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
