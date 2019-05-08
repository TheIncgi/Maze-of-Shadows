package app.ui.scenes;

import app.Game;
import app.engine.Engine;
import app.engine.MapGenerator;
import app.engine.entity.Player;
import app.misc.Keyboard;
import app.ui.elements.GameHUD;
import app.ui.elements.MapPane;
import app.ui.elements.PausePane;
import app.ui.elements.SlidingPane;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class LevelView extends Scene{
	MapGenerator generator = new MapGenerator();
	Engine engine;
	PausePane pauseOverlay = Game.instance().getPausePane();
	Pane root;
	//MapCanvas mapCanvas;
	MapPane mapPane;
	
	Pane gameView = new Pane();
	
	GameHUD hud;
	public int level = 0;
	
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
		Game.instance().setLevelView(this);
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

		root.getChildren().add(sliding);
		gameView.getChildren().addAll(mapPane, hud = Game.instance().getGameHud());
		sliding.setCurrent(gameView);
		
		hud.healthBar.progressProperty().bind( player.healthProperty().divide(player.maxHealthProperty() ));
		hud.staminaBar.progressProperty().bind( player.staminaProperty().divide(player.getMaxStamina()) );
		
		engine.start();
		
		
	}
	
	public void loadLevel() {
		engine.setMap(generator.generate( level )); //TODO set up to 40
		mapPane.setMap(engine.getMap());
		
		engine.unfreeze();
		player.getPos().set(3.5 * Game.instance().getPixelPerTile(), 3.5 * Game.instance().getPixelPerTile());
	}
	
	public MapPane getMapPane() {
		return mapPane;
	}

	//called from keyboard handler
	public void onPause() {
		if(!Game.instance().getEngine().isPaused()) {
			System.out.println("Paused!");
			Game.instance().getEngine().setPaused(true);
			sliding.fromRight(pauseOverlay);
		}else {
			System.out.println("Unpaused!");
			Game.instance().getEngine().setPaused(false);
			sliding.fromLeft(gameView);
		}
	}
	
}
