package app.ui.scenes;


import app.Game;
import app.engine.Engine;
import app.engine.MapGenerator;
import app.engine.entity.Monster;
import app.engine.entity.Player;
import app.misc.IntegerPosition;
import app.misc.Keyboard;
import app.ui.elements.GameHUD;
import app.ui.elements.MapPane;
import app.ui.elements.PausePane;
import app.ui.elements.SlidingPane;
import app.ui.elements.SlidingPane.Extra;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;

public class LevelView extends Scene{
	MapGenerator generator = new MapGenerator();
	Engine engine;
	PausePane pauseOverlay = Game.instance().getPausePane();
	Pane root;
	//MapCanvas mapCanvas;
	MapPane mapPane;
	
	//Pane gameView = new Pane();
	
	GameHUD hud;
	public static final SimpleIntegerProperty level = new SimpleIntegerProperty(1);
	
	SlidingPane sliding = new SlidingPane(this.widthProperty(), this.heightProperty());
	
	AnimationTimer timer = new AnimationTimer() {
		//long next = System.nanoTime();
		//long millisPerNano = 1000000;
		@Override
		public void handle(long now) {
			if(!engine.isRunning()) {
				this.stop();
			}else {
				mapPane.update();
			}
		}
	};
	private Player player;
	public LevelView() {
		super(new Pane(), Game.SIZE, Game.SIZE);
		//Game.instance().setLevelView(this);
		root = (Pane) getRoot();
		
		setOnKeyPressed(Keyboard::onKeyPress);
		setOnKeyReleased(Keyboard::onKeyRelease);

		//mapCanvas = new MapCanvas(Game.SIZE, Game.SIZE);
		mapPane = new MapPane(){
			public void onLoaded() {
				engine.unfreeze();
				timer.start();
			}
		};
		engine = Game.instance().getEngine();
		
		engine.setOnFrozen( this::loadLevel );
		player = new Player(5); // 5 is health
		engine.addEntity( player );
		mapPane.addEntity( player );
		mapPane.setFocus( player.getPos() );

		root.getChildren().addAll(mapPane, sliding);//)sliding);
		//gameView.getChildren().addAll(mapPane, hud = Game.instance().getGameHud());
		sliding.setCurrent(hud = Game.instance().getGameHud());
		
		hud.healthBar.progressProperty().bind( player.healthProperty().divide(player.maxHealthProperty() ));
		hud.staminaBar.progressProperty().bind( player.staminaProperty().divide(player.getMaxStamina()) );
		
	
		
		
	}
	public static SimpleIntegerProperty getLevelProperty() {
		return level;
	}
	public void loadLevel() {
		engine.setMap(generator.generate( level.get() )); //TODO set up to 40
		Platform.runLater(()->{	level.set(level.get()+1);});
		mapPane.setMap(engine.getMap());
		engine.getMap().addEmissiveSource(player.getEmissive());
		
		for(IntegerPosition pos : engine.getMap().getMonsterSpawns()) {
			Monster monster = new Monster(pos.getX()+3, pos.getY()+3);
			engine.addEntity(monster);
			mapPane.addEntity(monster);
		}
		
		engine.unfreeze();
		player.getPos().set(3.5 * Game.getPixelPerTile(), 3.5 * Game.getPixelPerTile());
	}
	
	public MapPane getMapPane() {
		return mapPane;
	}
	public Player getPlayer() {
		return player;
	}
	
	private ColorAdjust ca = new ColorAdjust(0d, 0, 0, 0);
	private GaussianBlur blur = new GaussianBlur(0);
	//called from keyboard handler
	public void onPause() {
		double maxBlur = Game.SIZE/20d;
		double maxBrighnessChange = -.4;
		double maxSaturationChange = -.3;
		if(!Game.instance().getEngine().isPaused()) {
			System.out.println("Paused!");
			Game.instance().getEngine().setPaused(true);
			
			blur.setInput(ca);
			mapPane.setEffect(blur);
			sliding.fromDown(pauseOverlay, new Extra() {
				@Override
				public void interpolate(double frac) {
					blur.setRadius(maxBlur*frac);
					ca.setBrightness(maxBrighnessChange*frac);
					ca.setSaturation(maxSaturationChange*frac);
				}
				@Override public void onFinish() {}
			});
		}else {
			System.out.println("Unpaused!");
			Game.instance().getEngine().setPaused(false);
			sliding.fromUp(Game.instance().getGameHud(), new Extra() {
				@Override
				public void interpolate(double frac) {
					frac = 1-frac;
					blur.setRadius(maxBlur*frac);
					ca.setBrightness(maxBrighnessChange*frac);
					ca.setSaturation(maxSaturationChange*frac);
				}
				@Override
				public void onFinish() {
					mapPane.setEffect(null);
				}
			});
			
		}
	}
	public void resetAll() {
		player.reset();
		Keyboard.releaseAll();
	}
	
	
}
