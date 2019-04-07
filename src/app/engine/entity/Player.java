package app.engine.entity;

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
	
	
}
