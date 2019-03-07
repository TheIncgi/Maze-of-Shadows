package app.engine;

import java.util.ArrayList;

import app.engine.items.BaseItem;

public class Shop {
	
	ArrayList<Offer> offers = new ArrayList<>();
	
	public Shop() {
		
	}
	
	public static class Offer{
		BaseItem item;
		int price;
		public Offer(BaseItem item, int price) {
			this.item = item;
			this.price = price;
		}
		
	}
	
}
