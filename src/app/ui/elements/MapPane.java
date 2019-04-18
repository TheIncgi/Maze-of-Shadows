package app.ui.elements;

import java.util.HashMap;

import app.engine.Map;
import app.engine.tiles.BaseTile;
import app.misc.DoublePosition;
import app.misc.IntegerPosition;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class MapPane extends Pane{
	Map map;
	TilePane<ImageView> tiles;
	TilePane<Rectangle> light;
	DoublePosition focus;
	public MapPane() {
		
	}
	
	public void setMap(Map map) {
		this.map = map;
		for(int y = map.getLowerBound(); y<=map.getUpperBound(); y++)
			for(int x = map.getLeftBound(); x<=map.getRightBound(); x++) {
				IntegerPosition pos = new IntegerPosition(x, y);
				BaseTile tile = map.getTile(pos);
				if(tile!=null) {
					tiles.addTile(pos, new ImageView(tile.getDrawable().getImage()), 1);
				}
				
			}
			
	}
	
	private class TilePane<T extends Node> extends Pane {
		HashMap<IntegerPosition, T> tiles;
		
		public void addTile( IntegerPosition pos, T t , double z) {
			tiles.put(pos, t);
			getChildren().add(t);
			t.setTranslateX(pos.getX());
			t.setTranslateY(pos.getY());
			t.setTranslateZ(z);
		}
		
		public void clearTiles() {
			tiles.clear();
			getChildren().clear();
		}
	}
}
