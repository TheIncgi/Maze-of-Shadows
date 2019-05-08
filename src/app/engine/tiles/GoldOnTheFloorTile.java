package app.engine.tiles;

import java.util.ArrayList;
import java.util.List;

import app.Game;
import app.engine.entity.Entity;
import app.engine.entity.Player;
import app.misc.DoublePosition;
import app.misc.IntegerPosition;
import app.misc.SoundManager;
import app.misc.SoundManager.SoundChannel;
import app.misc.SoundManager.Sounds;
import app.ui.elements.BaseDrawable;
import app.ui.elements.IDrawable;
import app.ui.elements.ToggleDrawable;
import javafx.scene.paint.Color;

public class GoldOnTheFloorTile extends BaseTile{
	boolean pickedUp = false;
	int pileSize = 1;
	ArrayList<Emissive> emissives = new ArrayList<>();
	
	private ToggleDrawable drawable = new ToggleDrawable("gold_pile.png", "looted_item.png");
	
	public GoldOnTheFloorTile(int tx, int ty) {
		final DoublePosition lightSource = new DoublePosition(tx, ty);
		emissives.add(new Emissive() {
			@Override
			public DoublePosition getSource() {
				return lightSource.add(0,0);
			}
			@Override
			public Color getLightColor() {
				return Color.GOLD;
			}
			@Override
			public double brightness() {
				return 1.4;
			}
			@Override
			public double getFocus() {
				return 4;
			}
		});
	}
	
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
		System.out.println("On tile entered gold!");
		if(e instanceof Player && !pickedUp) {
			drawable.getProperty().set(pickedUp = true);
			Player p = (Player)e;
			p.addGold(pileSize);
			Game.instance().getEngine().getMap().removeEmissiveSource(emissives.get(0));
			emissives.remove(0);
			SoundManager.playSound(Sounds.CASH, SoundChannel.SFX);
		}
	}
	@Override
	public IDrawable getDrawable() {
		return drawable;
	}
	
	
	@Override
	public List<Emissive> getEmissives(IntegerPosition tilePos) {
		return emissives;
	}
}
