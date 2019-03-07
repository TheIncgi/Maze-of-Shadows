package app.engine.entity;

public class LivingEntity extends Entity {
	int health;
	int maxHealth;
	
	/**how many milliseconds untill the next health unit is restored<br>
	 * default -1 for disabled*/
	int regenTime = -1;
	
	public LivingEntity(int maxHealth) {
		super();
		this.maxHealth = maxHealth;
	}
	
	public void heal(int amount) {
		if(amount <= 0) throw new IllegalArgumentException("Amount must be greater than / equal to 0");
		health = Math.min(maxHealth, health+amount);
	} 
	public void damage(int amount) {
		if(amount <= 0) throw new IllegalArgumentException("Amount must be greater than / equal to 0");
		health = Math.max(0,  health-amount);
		if(amount == 0)
			onDeath();
	}
	
	
	public void onDeath() {}
}
