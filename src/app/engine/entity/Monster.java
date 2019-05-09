package app.engine.entity;

import java.util.ArrayList;

import app.Game;
import app.engine.Engine;
import app.engine.tiles.BaseTile;
import app.ui.elements.AnimatedDrawable;
import app.ui.elements.IDrawable;
import app.ui.elements.MapPane;
import javafx.scene.image.Image;
import resources.R;

public class Monster extends LivingEntity {
	TravelDir dir = TravelDir.random();
	public Monster(int x, int y) {
		super(Integer.MAX_VALUE); //regular doctors visits makes this monster almost undefeatable
		getPos().set((x+.5)*Game.getPixelPerTile(), (y+.5)*Game.getPixelPerTile());
	}

	@Override
	public void onTick(long now) {
		drawable.onTick( now );
		
		//MapPane mapView = Game.instance().getLevelView().getMapPane();
		double movementScale = MapPane.pixelsPerTile();
		
		double x =  (pos.getFloorX() / movementScale) - 3;
		double y =  (pos.getFloorY() / movementScale) - 3;
		//BaseTile current = Game.instance().getEngine().getMap().getTile((int)x, (int)y);
		double nextX = Math.floor(x + velocity.getX()/movementScale);
		double nextY = Math.floor(y +velocity.getY()/movementScale );
		BaseTile next = Game.instance().getEngine().getMap().getTile( (int)nextX, (int)nextY );
		
		Game.instance().getEngine();
		if(next!=null && !next.isPassable() || Math.random() < 1/Engine.ticksPerSecond())
			dir = TravelDir.random();
		
		walk(Math.atan2(dir.getDY(), dir.getDX())	);
		

		doMovement();
	}
	
	
	private static final ArrayList<Image> frames = new ArrayList<>();
	static {
		for(int i = 0; i<=3; i++)
			frames.add(new Image(R.class.getResourceAsStream("redBlob_"+i+".png")));
	}
	private static AnimatedDrawable drawable = new AnimatedDrawable(3, frames);
	
	@Override
	public IDrawable getDrawable() {
		return drawable;
	}
	
	private enum TravelDir {
		north,
		south,
		east,
		west;
		
		public int getDX() {
			switch (this) {
			case east:
				return 1;
			case west:
				return -1;
			default:
				return 0;
			}
		}
		public int getDY() {
			switch (this) {
			case north:
				return -1;
			case south:
				return 1;
			default:
				return 0;
			}
		}
		
		public static TravelDir random() {
			return values()[(int) (Math.random()*values().length)];
		}
	}
}
