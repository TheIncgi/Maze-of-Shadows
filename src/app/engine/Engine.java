package app.engine;

import java.io.IOException;
import java.util.LinkedList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import app.engine.entity.Entity;
import app.engine.entity.TickListener;
import app.engine.tiles.BaseTile;
import app.engine.tiles.Emissive;
import app.misc.IntegerPosition;
import resources.R;

public class Engine {
	
	Map map;
	private LinkedList<Entity> entities = new LinkedList<>();
	//add and remove in a thread safe way, also allows for adding during tick event without concurrent mod ex
	private LinkedList<Entity> toAdd = new LinkedList<>(), toRemove = new LinkedList<>();
	//engine tick phases
	//tile, entity movement, entity status
	Thread engineThread;
	volatile boolean running = false;
	//skip ticks while loading a map so entities don't move through unloaded maps
	private volatile boolean freeze = false;
	private boolean notifiedFrozen;
	
	public Engine() {
	}
	
	public void start() {
		engineThread = new Thread(()-> {
			Emissive.lightScale = 0;
			freeze();
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
//				else if( (now - nextTick)/tickDelay > 500 )
//					throw new RuntimeException("The system is too running slow! Fell 500+ game ticks behind schedule!");
			}
		}, "Engine Thread"); 
		running = true;
		engineThread.start();
		startMusic();
	}
	
	public void stop() {
		running = false;
	}
	
	private void tick( long now ) {
		if(freeze) {
			Emissive.lightScale = Math.max(0, Emissive.lightScale - 1/(ticksPerSecond())); //FIXME doesn't seem to animate well...
			if(Emissive.lightScale == 0)
				if(onFrozen!=null && !notifiedFrozen) {
					notifiedFrozen = true;
					onFrozen.run();
				}
			return;
		}else {
			Emissive.lightScale = Math.min(1, Emissive.lightScale + 1/(ticksPerSecond()));
		}
		
		synchronized(entities) {
			synchronized (toAdd) {
				entities.addAll( toAdd );
				toAdd.clear();
			}
			synchronized (toRemove) {
				entities.removeAll( toRemove );
				toRemove.clear();
			}
		}
		
		for(Entity t : entities) {
			if(t instanceof TickListener)
				((TickListener)t).onTick( now );
		}
		
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void setMap(Map map) {
		this.map = map;
	}
	public Map getMap() {
		return map;
	}
	
	private Runnable onFrozen;
	/**Set callback for when the game is done diming lights so next map can be loaded in the darkness*/
	public void setOnFrozen( Runnable r ) {
		this.onFrozen = r;
	}
	
	
	
	public void freeze() {
		System.out.println("Freezing");
		notifiedFrozen = false;
		freeze = true;
	}
	public void unfreeze() {
		System.out.println("Unfreezing");
		freeze = false;
	}
	
	public void addEntity(Entity listener) {
		synchronized(toAdd) {
			toAdd.add(listener);
		}
	}
	public void removeEntity(Entity listener) {
		synchronized (toRemove) {
			toRemove.add(listener);
		}
	}
	public void clearEntities() {
		synchronized (toRemove) {
			toRemove.addAll(entities);
		}
	}
	
	/**return the distance traveled each game tick at the speed nTiles/sec*/
	public static double tilesPerSecond(double nTiles) {
		return nTiles/Engine.ticksPerSecond();
	}
	public static double ticksPerSecond() {
		return 30;
	}
	
	public void enterTile(Entity entity, IntegerPosition tilePos) {
		if(entity == null || tilePos == null) return;
		BaseTile tile = map.getTile(tilePos);
		if(tile==null)return;
		tile.onEnter(entity, tilePos);
	}
	public void exitTile(Entity entity, IntegerPosition tilePos) {
		if(entity == null || tilePos == null) return;
		BaseTile tile = map.getTile(tilePos);
		if(tile==null)return;
		tile.onExit(entity, tilePos);
	}
	
	
	private Clip getClip(String resName) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
		Clip clip = AudioSystem.getClip();
		clip.open(  AudioSystem.getAudioInputStream(R.class.getResourceAsStream(resName)));
		return clip;
	}
	private Clip music, warningSound;
	public void startMusic() {
		try{
			if(music == null) 
				music = getClip("Into the maze.wav");
			music.start();
		}catch (Throwable e) {
			e.printStackTrace();
		}
	}
	public void stopMusic() {
		if(music!=null)
			music.stop();
	}
	public void startWarningSound() {
		try {
			if(warningSound==null)
				warningSound = getClip("unease.wav");
			warningSound.start();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	public void stopWarningSound() {
		if(warningSound!=null)
			warningSound.stop();
	}
}
