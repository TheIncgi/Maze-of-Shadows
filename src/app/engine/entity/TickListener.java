package app.engine.entity;

public interface TickListener {
	//called once per game tick for regisetered objects
	public void onTick(long now);
}
