package app.engine.entity;

import app.engine.tiles.Emissive;
import app.misc.DoublePosition;
import app.misc.Keybinding;
import app.misc.Keyboard;
import app.ui.elements.BaseDrawable;
import app.ui.elements.IDrawable;
import app.ui.elements.SettingsPane;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import resources.R;

import java.util.HashMap;

import app.Game;
import app.engine.items.BaseItem;

public class Player extends LivingEntity {
	HashMap<BaseItem, Integer> inventory = new HashMap<>();
	int gold = 0;
	
	public Player(int maxHealth) {
		super(maxHealth);
	}
	
	public void useItem(BaseItem item) {
		
	}
	
	
	@Override
	public void onTick( long now ) {
		SettingsPane sets = Game.instance().getSettings();
		int dx = 0, dy = 0;
		if(Keyboard.isHeld( sets.getUpKeycode() )) 
			dy--;
		if(Keyboard.isHeld( sets.getDownKeycode() ))
			dy++;
		if(Keyboard.isHeld( sets.getLeftKeycode() ))
			dx--;
		if(Keyboard.isHeld( sets.getRightKeycode() ))
			dx++;
		if(dx!=0 || dy!=0) {
			double angle = Math.atan2(dy, dx);	
			walk(angle);
		}else {
			velocity.set(0,0);
		}
		doMovement();
	}
	
	
	public HashMap<BaseItem, Integer> getInventory() {
		return inventory;
	}
	
	public void addItemToInventory(BaseItem item) {}
	
	public int getGold() {
		return gold;
	}
	
	public void setGold(int gold) {
		this.gold = gold;
	}
	
	
	
	private static BaseDrawable drawable = new BaseDrawable() {
		@Override
		public String getResourceName() {
			return "placeholder 2.png";
		}
	};
	@Override
	public IDrawable getDrawable() {
		return drawable;
	}
	
	public static class PlayerEmissive extends Emissive {
		private DoublePosition playerPos;
		public PlayerEmissive(DoublePosition playerPos) {
			super();
			this.playerPos = playerPos;
		}
		@Override
		public DoublePosition getSource() {
			return playerPos;
		}
		@Override
		public double flickerAmount() {
			return 1;
		}
		@Override
		public double brightness() {
			return 3; //TODO vary by health or other attrib?
		}
		@Override
		public Color getLightColor() {
			return super.getLightColor(); //TODO vary by attrib?
		}
	}
}
