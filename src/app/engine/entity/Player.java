package app.engine.entity;

import app.engine.tiles.Emissive;
import app.misc.DoublePosition;
import javafx.scene.paint.Color;

import java.util.HashMap;

import app.engine.items.BaseItem;

public class Player extends LivingEntity {
	HashMap<BaseItem, Integer> inventory = new HashMap<>();
	int gold = 0;
	
	public Player(int maxHealth) {
		super(maxHealth);
	}
	
	public void useItem(BaseItem item) {
		
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
