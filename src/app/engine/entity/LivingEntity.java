package app.engine.entity;

import app.engine.Engine;
import app.ui.elements.IDrawable;
import javafx.beans.property.SimpleDoubleProperty;

public class LivingEntity extends Entity {
	SimpleDoubleProperty health;
	SimpleDoubleProperty maxHealth;
	int invulnrableTime = 0;
	/**how many milliseconds untill the next health unit is restored<br>
	 * default -1 for disabled*/
	int regenTime = -1;
	
	public LivingEntity(int maxHealth) {
		super();
		this.maxHealth = new SimpleDoubleProperty(maxHealth);
		this.health = new SimpleDoubleProperty(maxHealth);
	}
	
	public void heal(int amount) {
		if(amount <= 0) throw new IllegalArgumentException("Amount must be greater than / equal to 0");
		health.set(Math.min(maxHealth.get(), health.get()+amount));
	} 
	public void damage(int amount) {
		if(invulnrableTime>0) return;
		if(amount <= 0) throw new IllegalArgumentException("Amount must be greater than / equal to 0");
		health.set(Math.max(0,  health.get()-amount));
		if(amount <= 0)
			onDeath();
		invulnrableTime = (int) Engine.ticksPerSecond();
	}
	
	
	public void onDeath() {}

	@Override
	public IDrawable getDrawable() {
		return null;
	}

	@Override
	public void onTick(long now) {
		invulnrableTime=Math.max(0, invulnrableTime-1);
	}
}
