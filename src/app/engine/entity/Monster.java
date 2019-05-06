package app.engine.entity;

public class Monster extends LivingEntity {

	public Monster(int maxHealth) {
		super(Integer.MAX_VALUE);
	}

	@Override
	public void onTick(long now) {
	}
}
