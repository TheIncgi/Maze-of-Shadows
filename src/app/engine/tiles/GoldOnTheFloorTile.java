package app.engine.tiles;

import app.Game;
import app.engine.entity.Entity;
import app.engine.entity.Player;
import app.misc.IntegerPosition;
import app.ui.elements.BaseDrawable;
import app.ui.elements.IDrawable;
import app.ui.elements.ToggleDrawable;

public class GoldOnTheFloorTile extends BaseTile{
	boolean pickedUp = false;
	int pileSize = 1;
	
	private ToggleDrawable drawable = new ToggleDrawable("missing_texture.png", "missing_texture.png");
	@Override
	public boolean isOpaque() {
		return false;
	}
	@Override
	public boolean isPassable() {
		return true;
	}
	@Override
	public void onEnter(Entity e, IntegerPosition tilePos) {
		super.onEnter(e, tilePos);
		
		if(e instanceof Player && !pickedUp) {
			drawable.getProperty().set(pickedUp = true);
			Player p = (Player)e;
			p.addGold(pileSize);
		}
	}
	@Override
	public IDrawable getDrawable() {
		return drawable;
	}
}
