package app.engine.entity;

import app.engine.tiles.Emissive;
import app.misc.DoublePosition;
import javafx.scene.paint.Color;

public class Player extends LivingEntity {

	
	
	public Player() {
		super( 5 ); //TODO what is the players max health, this is a placeholder
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
