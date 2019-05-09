package app.engine.entity;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import app.Game;
import app.engine.Engine;
import app.engine.items.BaseItem;
import app.engine.tiles.Emissive;
import app.misc.DoublePosition;
import app.misc.Keyboard;
import app.misc.SoundManager;
import app.misc.SoundManager.SoundChannel;
import app.misc.SoundManager.Sounds;
import app.misc.Utils;
import app.ui.elements.AnimatedDrawable;
import app.ui.elements.BaseDrawable;
import app.ui.elements.IDrawable;
import app.ui.elements.MapPane;
import app.ui.elements.SettingsPane;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import resources.R;

public class Player extends LivingEntity implements IEmissiveEntity{
	HashMap<BaseItem, Integer> inventory = new HashMap<>();
	double maxStamina = Engine.ticksPerSecond() * 5;
	SimpleIntegerProperty stamina = new SimpleIntegerProperty( (int) maxStamina );
	private PlayerEmissive playerEmissive = new PlayerEmissive(pos);
	
	public static boolean sonic = false;
	
	SimpleIntegerProperty gold = new SimpleIntegerProperty(0);
	
	int score = 0;
	
	public Player(int maxHealth) {
		super(maxHealth);
		gold.addListener(e->{
			Platform.runLater(()->{Game.instance().getGameHud().goldAmount.setText(
					getGold()==0? "Very poor" : Integer.toString(getGold())
					);
			});
		});
	}
	
	public void useItem(BaseItem item) {
		
	}
	
	@Override
	public void damage(int amount) {
		if(invulnrableTime>0) return;
		if(amount <= 0) throw new IllegalArgumentException("Amount must be greater than / equal to 0");
		health.set(Math.max(0,  health.get()-amount));
		if(health.get() <= 0)
			onDeath();
		invulnrableTime = (int) Engine.ticksPerSecond();
		SoundManager.playSound(Sounds.OUCH, SoundChannel.SFX);
	}
	
	@Override
	public void onTick( long now ) {
		super.onTick(now);
		for(Entity e : Game.instance().getEngine().getEntities()) {
			if(e!=this && Utils.distance(e.pos.getX(), e.pos.getY(), pos.getX(), pos.getY()) < MapPane.pixelsPerTile()/4) {
				if(e instanceof Monster)
					damage(1);
			}
		}
		
		
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
		
		
		drawable.onTick(now);
		
		doMovement();
	}
	
	
	public HashMap<BaseItem, Integer> getInventory() {
		return inventory;
	}
	
	public void addItemToInventory(BaseItem item) {
		//TODO
	}
	
	public int getGold() {
		return gold.get();
	}
	
	public void setGold(int gold) {
		this.gold.set(gold);
	}
	
	
	
//	private static BaseDrawable drawable = new BaseDrawable() {
//		@Override
//		public String getResourceName() {
//			return "placeholder 2.png";
//		}
//	};
	private static ArrayList<Image> frames = new ArrayList<>();
	static {
		for(int i = 0; i<=3; i++)
			frames.add(new Image(R.class.getResourceAsStream("player_"+i+".png")));
	}
	AnimatedDrawable drawable = new AnimatedDrawable(3, frames) {
		public long getframeDelay() {
			double fps = Keyboard.isHeld( Game.instance().getSettings().getSprintKeycode() )?
							(sonic? 20 : 6) : 3; 
			return (long) (1000/fps);
		};
	};
	@Override
	public IDrawable getDrawable() {
		return drawable;
	}
	
	public class PlayerEmissive extends Emissive {
		private DoublePosition playerPos;
		Color lightColor = Color.SALMON;
		public PlayerEmissive(DoublePosition playerPos) {
			super();
			this.playerPos = playerPos;
		}
		@Override
		public DoublePosition getSource() {
			Game.instance();
			return playerPos.divide(Game.getPixelPerTile()).add(-3, -3);
		}
		@Override
		public double flickerAmount() {
			return .02;
		}
		@Override
		public double brightness() {
			return 1.75 * (health.get()/maxHealth.get()); //TODO vary by health or other attrib?
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
		setGold(getGold()+amount);
		score+=amount;
	}
	
	@Override
	public void onDeath() {
		Game.instance().getEngine().stop();
		SoundManager.stopAll();
		System.out.println("Prompting for player name...SS");
		String name = JOptionPane.showInputDialog("Enter your name for the score board");
		if(name == null) {name = System.getProperty("user.name");}
		if(name == null) {name = "[?]";}
		Game.instance().getHighScorePane().addScore(name, score);
		reset();
		Platform.runLater(Game.instance().getMainMenu()::returnToMainMenu);
	}

	public void reset() {
		health.set(maxHealth.get());
		stamina.set((int) maxStamina);
		sonic = false;
		score = 0;
		setGold( 0 );
		inventory.clear();
	}
}
