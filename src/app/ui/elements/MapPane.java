package app.ui.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import app.Game;
import app.engine.Map;
import app.engine.entity.Entity;
import app.engine.entity.Player;
import app.engine.tiles.BaseTile;
import app.engine.tiles.Lighting;
import app.misc.DoublePosition;
import app.misc.IntegerPosition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import jdk.internal.dynalink.support.Guards;

public class MapPane extends Pane{
	Map map;
	int tilesOnScreen = 10;
	TilePane tiles = new TilePane();
	TilePane light = new TilePane();
	DoublePosition focus;
	WeakHashMap<Entity, ImageView> entityViews = new WeakHashMap<>();

	public MapPane() {
		this.getChildren().addAll(tiles);
		tiles.setLayoutX(Game.SIZE/2f);
		tiles.setLayoutY(Game.SIZE/2f);
		light.setLayoutX(Game.SIZE/2f);
		light.setLayoutY(Game.SIZE/2f);
		
		//.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(0), new Insets(0))));

	}

	public void update() {
		synchronized (entitites) {
			synchronized (entityToAdd) {
				for (Entity entity : entityToAdd) {
					entitites.add(entity);
					ImageView iv;
					entityViews.put(entity,iv = new ImageView(entity.getDrawable().getImage()));
					iv.setTranslateZ(3);
					getChildren().add(iv);
				}
				entityToAdd.clear();
			}
			synchronized (entityToRemove) {
				entitites.removeAll(entityToRemove);
				entityToRemove.clear();
			}
		}

		this.translateXProperty().set(-focus.getX()+Game.SIZE/2);
		this.translateYProperty().set(-focus.getY()+Game.SIZE/2);

		for (Entity entity : entitites) {
			ImageView ev = entityViews.get(entity);
			ev.translateXProperty().set(entity.getPos().getX());
			ev.translateYProperty().set(entity.getPos().getY());
		}

		synchronized (light) {

			for(Entry<IntegerPosition, Node> e : light.tiles.entrySet()) {
				if (e.getValue() instanceof Rectangle) {
					Rectangle r = (Rectangle) e.getValue();
					Lighting l = map.getLight(e.getKey());
					if(l!=null)
						r.setFill(	l.getColor());
				}
			}
		}
	}

	public void setFocus(DoublePosition focus) {
		this.focus = focus;
	}

	public void setMap(Map map) {
		this.map = map;
		tiles.clearTiles();
		light.clearTiles();
		light.addBackground();
		Game.instance();
		Image defaultFloor = Game.genericFloor;
		float size = (float)(Game.SIZE / tilesOnScreen); 
		tiles.width = (int) size;
		tiles.height = (int) size;
		light.width = tiles.width;
		light.height = tiles.height;

		for(int y = map.getUpperBound(); y<=map.getLowerBound(); y++)
			for(int x = map.getLeftBound(); x<=map.getRightBound(); x++) {
				IntegerPosition pos = new IntegerPosition(x, y);
				BaseTile tile = map.getTile(pos);
				if(tile!=null) {
					tiles.addTile(pos, new ImageView(tile.getDrawable().getImage()), 1);
				}else {
//					Rectangle r;
//					synchronized (light) {
//						light.addTile(pos, r = new Rectangle(light.width, light.height));
//					}
//					r.setFill(Color.WHITE);
				}
				Rectangle r = new Rectangle(light.width, light.height);
				
				r.setBlendMode(BlendMode.MULTIPLY);
				light.addTile(pos,  r);
			}
		tiles.update();
		light.update();
		onLoaded();
	}
	
	public void onLoaded() {}

	static final GaussianBlur blur = new GaussianBlur(10);
	private class TilePane extends Pane {
		HashMap<IntegerPosition, Node> tiles = new HashMap<>();
		int width = 1, height = 1;

		public void addTile( IntegerPosition pos, ImageView t , double z) {
			tiles.put(pos, t);

			t.setTranslateX(pos.getX() * width );
			t.setTranslateY(pos.getY() * height );
			t.setTranslateZ(z);
			t.setFitWidth(width);
			t.setFitHeight(height);

		}
		public void addTile( IntegerPosition pos, Rectangle t ) {
			tiles.put(pos, t);

			t.setTranslateX(pos.getX() * width );
			t.setTranslateY(pos.getY() * height );
			t.setTranslateZ(20);
			t.setBlendMode(BlendMode.MULTIPLY);
			//t.setEffect(blur);

		}

		public void addBackground() {
			//			Rectangle r = new Rectangle((map.getRightBound()-map.getLeftBound() * MapPane.this.tiles.width), (map.getLowerBound() - map.getUpperBound()) * MapPane.this.tiles.height);
			//			r.setFill(Color.BLACK);
			//			r.setBlendMode(BlendMode.MULTIPLY);
			//			
			//			tiles.put(null,r);
		}

		public void clearTiles() {
			tiles.clear();
			Platform.runLater(getChildren()::clear);
		}
		public void update() {
			Platform.runLater(()->{
				getChildren().clear();
				getChildren().addAll(tiles.values());
			});
		}
	}


	LinkedList<Entity> entityToAdd = new LinkedList<>(), entityToRemove = new LinkedList<>();
	ArrayList<Entity> entitites = new ArrayList<>();
	public void addEntity(Entity e) {
		synchronized (entityToAdd) {
			entityToAdd.add(e);
		}
	}
	public void removeEntity(Entity e) {
		synchronized (entityToRemove) {
			entityToRemove.add(e);
		}
	}
}
