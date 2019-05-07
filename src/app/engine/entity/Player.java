package app.engine.entity;

import java.util.HashMap;

import app.Game;
import app.engine.Engine;
import app.engine.items.BaseItem;
import app.engine.tiles.Emissive;
import app.misc.DoublePosition;
import app.misc.Keyboard;
import app.ui.elements.BaseDrawable;
import app.ui.elements.IDrawable;
import app.ui.elements.SettingsPane;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;

public class Player extends LivingEntity implements IEmissiveEntity{
	HashMap<BaseItem, Integer> inventory = new HashMap<>();
	double maxStamina = Engine.ticksPerSecond() * 5;
	SimpleIntegerProperty stamina = new SimpleIntegerProperty( (int) maxStamina );
	private PlayerEmissive playerEmissive = new PlayerEmissive(pos);
	
	public static boolean sonic = false;
	
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
			if(Keyboard.isHeld( sets.getSprintKeycode() ) && (stamina.get() > 0 || sonic) ) {
				stamina.set(stamina.subtract(1).get());
				sprint(angle);
			}else {
				if(!Keyboard.isHeld( sets.getSprintKeycode() ))
					stamina.set((int) Math.min(stamina.add(1).get(), maxStamina)  );
				walk(angle);
			}
		}else {
			velocity.set(0,0);
			if(!Keyboard.isHeld( sets.getSprintKeycode() ))
				stamina.set((int) Math.min(stamina.add(2).get(), maxStamina)  );
		}
		
		
			
		
		doMovement();
	}
	
	
	public HashMap<BaseItem, Integer> getInventory() {
		return inventory;
	}
	
	public void addItemToInventory(BaseItem item) {
		//TODO
	}
	
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
		Color lightColor = Color.SALMON;
		public PlayerEmissive(DoublePosition playerPos) {
			super();
			this.playerPos = playerPos;
		}
		@Override
		public DoublePosition getSource() {
			return playerPos.divide(Game.instance().getPixelPerTile()).add(-3, -3);
		}
		@Override
		public double flickerAmount() {
			return .02;
		}
		@Override
		public double brightness() {
			return 1.75; //TODO vary by health or other attrib?
		}
		@Override
		public Color getLightColor() {
			return lightColor; //TODO vary by attrib?
		}
	}
	
	@Override
	public Emissive getEmissive() {
		return playerEmissive;
	}
	
	@Override
	public double getSprintingSpeed() {
		return super.getSprintingSpeed() * (sonic? 3 : 1);
	}

	public SimpleDoubleProperty healthProperty() {
		return health;
	}
	public SimpleDoubleProperty maxHealthProperty() {
		return maxHealth;
	}
	
	public SimpleIntegerProperty staminaProperty() {
		return stamina;
	}

	public double getMaxStamina() {
		return maxStamina;
	}

	public void addGold(int amount) {
		gold += amount;
	}
	
}
