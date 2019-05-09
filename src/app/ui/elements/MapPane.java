package app.ui.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import app.Game;
import app.engine.Map;
import app.engine.entity.Entity;
import app.engine.entity.IEmissiveEntity;
import app.engine.entity.Player;
import app.engine.tiles.BaseTile;
import app.engine.tiles.Lighting;
import app.misc.DoublePosition;
import app.misc.IntegerPosition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MapPane extends Pane{
	Map map;
	static int tilesOnScreen = 6;
	TilePane tiles = new TilePane();
	TilePane light = new TilePane();
	DoublePosition focus;
	WeakHashMap<Entity, ImageView> entityViews = new WeakHashMap<>();
	
	

	public MapPane() {
		this.getChildren().addAll(tiles, light);
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
					entityViews.put(entity,iv = new ImageView());
					iv.imageProperty().bind(entity.getDrawable().getImage());
					iv.setFitWidth(pixelsPerTile()/4);
					iv.setFitHeight(pixelsPerTile()/4);
					iv.setTranslateZ(3);
					getChildren().add(1,iv);
					if(entity instanceof IEmissiveEntity) {
						map.addEmissiveSource(((IEmissiveEntity) entity).getEmissive());
					}
				}
				entityToAdd.clear();
			}
			synchronized (entityToRemove) {
				for(Entity entity : entityToRemove) {
					if(entity instanceof IEmissiveEntity)
						map.removeEmissiveSource(((IEmissiveEntity) entity).getEmissive());
					entitites.remove(entity);
				}
				entityToRemove.clear();
			}
		}
		if(focus!=null) {
			this.translateXProperty().set(-focus.getX()+Game.SIZE/2);
			this.translateYProperty().set(-focus.getY()+Game.SIZE/2);
		}
		for (Entity entity : entitites) {
			ImageView ev = entityViews.get(entity);
			boolean flag = true
					&& -ev.getTranslateX() <= MapPane.this.getTranslateX() + Game.SIZE +pixelsPerTile()/4
					&& MapPane.this.getTranslateX() <= -ev.getTranslateX() + Game.SIZE +pixelsPerTile()/4
					&& -ev.getTranslateY() <= MapPane.this.getTranslateY() + Game.SIZE +pixelsPerTile()/4
					&& MapPane.this.getTranslateY() <= -ev.getTranslateY() + Game.SIZE +pixelsPerTile()/4
					;
			ev.setVisible(flag);
			ev.translateXProperty().set(entity.getPos().getX() - ev.getFitWidth()/2);
			ev.translateYProperty().set(entity.getPos().getY() - ev.getFitHeight()/2);
		}

		tiles.updateVisiblity();
		light.updateVisiblity();
		
//		synchronized (tiles) {
//			for (Entry<IntegerPosition, Node> e : tiles.tiles.entrySet()) {
//				if(e.getValue() instanceof ImageView)
//			}
//		}
		
		map.calculateLighting();
		synchronized (light) {
			for(Entry<IntegerPosition, Node> e : light.tiles.entrySet()) {
				if (e.getValue().isVisible() && e.getValue() instanceof Rectangle) {
					Rectangle r = (Rectangle) e.getValue();
					Lighting l = map.getLight(e.getKey());
					if(l!=null)
						r.setFill(	l.getColor());
					else
						r.setFill( Color.BLACK );
				}
			}
		}
	}

	public void setFocus(DoublePosition focus) {
		this.focus = focus;
	}

	public void setMap(Map map) {
		this.map = map;
		map.calculateLighting();
		tiles.clearTiles();
		light.clearTiles();
		light.addBackground();
		Game.instance();
		//Image defaultFloor = Game.genericFloor;
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
					ImageView iv;
					tiles.addTile(pos, iv = new ImageView(), 1);
					final int fx = x , fy = y;
					iv.setOnMouseClicked(e->{if(Player.sonic)System.out.printf("%d, %d\n",fx,fy);});
					iv.imageProperty().bind(tile.getDrawable().getImage());
				}
				Rectangle r;
				synchronized (light) {
					light.addTile(pos, r = new Rectangle(light.width, light.height));
					r.setTranslateZ(25);
				}
				r.setFill(Math.random()>.5?Color.WHITE:Color.GRAY);
				
				
			}
		tiles.update();
		light.update();
		light.setBlendMode(BlendMode.MULTIPLY);
		blur.setInput(Game.instance().getSettings().colorAdjust);
		light.setEffect(blur);
		Platform.runLater(MapPane.this::update);
		onLoaded();
	}
	
	
	
	public void onLoaded() {}

	static final GaussianBlur blur = new GaussianBlur(Game.SIZE/tilesOnScreen/2);
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
			t.setOnMouseClicked(e->{
				System.out.println(pos.toString());
			});

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
		public void updateVisiblity() {
			//int leftTile = -(int) (MapPane.this.translateXProperty().get() / width);
			//int rightTile = leftTile + ((tilesOnScreen+1) * width);
			//int topTile = -(int) (MapPane.this.getTranslateY() / height);
			//int bottomTile = topTile + ((tilesOnScreen+1)*height);
			//int vis = 0;
			//int invis = 0;
			for (Entry<IntegerPosition, Node> entry : tiles.entrySet()) {
				Node n = entry.getValue();
				boolean flag = true
						&& -n.getTranslateX() <= MapPane.this.getTranslateX() + Game.SIZE + width
						&& MapPane.this.getTranslateX() <= -n.getTranslateX() + Game.SIZE + width
						&& -n.getTranslateY() <= MapPane.this.getTranslateY() + Game.SIZE + height
						&& MapPane.this.getTranslateY() <= -n.getTranslateY() + Game.SIZE + height
						;
				n.setVisible( flag );
			//	vis+=flag?1:0;
			//	invis+=flag?0:1;
			}
			//System.out.printf("Visibilty: %6.3f\n", (vis/(float)(vis+invis)));
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
	
	public static double pixelsPerTile() {
		return Game.SIZE / tilesOnScreen;
	}
}
