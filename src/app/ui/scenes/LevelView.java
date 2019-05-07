package app.ui.scenes;

import app.Game;
import app.engine.Engine;
import app.engine.MapGenerator;
import app.engine.entity.Player;
import app.misc.Keyboard;
import app.ui.elements.GameHUD;
import app.ui.elements.MapPane;
import app.ui.elements.PausePane;
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
	GameHUD hud;
	
	AnimationTimer timer = new AnimationTimer() {
		//long next = System.nanoTime();
		//long millisPerNano = 1000000;
		@Override
		public void handle(long now) {
			if(!engine.isRunning()) {
				this.stop();
			}else {
				//if(next <= now) {
//					mapCanvas.draw();
				//	next = now + millisPerNano * 1000/60;
				//}
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
		
		root.getChildren().addAll(mapPane, hud = Game.instance().getGameHud());
		
		hud.healthBar.progressProperty().bind( player.healthProperty().divide(player.maxHealthProperty() ));
		hud.staminaBar.progressProperty().bind( player.staminaProperty().divide(player.getMaxStamina()) );
		
		engine.start();
		
		
	}
	
	public void loadLevel() {
		engine.setMap(generator.generate( 20 )); //TODO set up to 40
		mapPane.setMap(engine.getMap());
		
		engine.unfreeze();
		player.getPos().set(3.5 * Game.instance().getPixelPerTile(), 3.5 * Game.instance().getPixelPerTile());
	}
	
	public MapPane getMapPane() {
		return mapPane;
	}
	
}
