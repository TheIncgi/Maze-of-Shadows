package app.ui.elements;

import java.util.HashMap;

import app.Game;
import app.engine.Map;
import app.engine.tiles.BaseTile;
import app.misc.DoublePosition;
import app.misc.IntegerPosition;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class MapPane extends Pane{
	Map map;
	int tilesOnScreen = 32;
	TilePane tiles = new TilePane();
	TilePane light = new TilePane();
	DoublePosition focus;
	public MapPane() {
		this.getChildren().addAll(tiles, light);
		tiles.setLayoutX(Game.SIZE/2f);
		tiles.setLayoutY(Game.SIZE/2f);
		light.setLayoutX(Game.SIZE/2f);
		light.setLayoutY(Game.SIZE/2f);
		
	}
	
	public void setMap(Map map) {
		this.map = map;
		tiles.clearTiles();
		Game.instance();
		Image defaultFloor = Game.genericFloor;
		float size = (float)(Game.SIZE / tilesOnScreen); 
		tiles.width = (int) size;
		tiles.height = (int) size;
		
		for(int y = map.getUpperBound(); y<=map.getLowerBound(); y++)
			for(int x = map.getLeftBound(); x<=map.getRightBound(); x++) {
				IntegerPosition pos = new IntegerPosition(x, y);
				BaseTile tile = map.getTile(pos);
				if(tile!=null) {
					tiles.addTile(pos, new ImageView(tile.getDrawable().getImage()), 1);
				}else {
					
				}
				
			}
			
	}
	
	private class TilePane extends Pane {
		HashMap<IntegerPosition, Node> tiles = new HashMap<>();
		int width = 1, height = 1;
		
		public void addTile( IntegerPosition pos, ImageView t , double z) {
			tiles.put(pos, t);
			getChildren().add(t);
			t.setTranslateX(pos.getX() * width );
			t.setTranslateY(pos.getY() * height );
			t.setTranslateZ(z);
			t.setFitWidth(width);
			t.setFitHeight(height);
		}
		
		public void clearTiles() {
			tiles.clear();
			getChildren().clear();
		}
	}
}
