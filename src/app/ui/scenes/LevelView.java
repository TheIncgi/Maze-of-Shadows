package app.ui.scenes;

import app.Game;
import app.engine.Engine;
import app.engine.Map;
import app.engine.MapGenerator;
import app.engine.entity.Player;
import app.misc.Keyboard;
import app.ui.elements.MapCanvas;
import app.ui.elements.PausePane;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class LevelView extends Scene{
	MapGenerator generator = new MapGenerator();
	Engine engine;
	PausePane pauseOverlay = Game.instance().getPausePane();
	Pane root;
	MapCanvas mapCanvas;
	AnimationTimer timer = new AnimationTimer() {
		//long next = System.nanoTime();
		//long millisPerNano = 1000000;
		@Override
		public void handle(long now) {
			if(!engine.isRunning()) {
				this.stop();
			}else {
				//if(next <= now) {
					mapCanvas.draw();
				//	next = now + millisPerNano * 1000/60;
				//}
			}
		}
	};
	
	public LevelView() {
		super(new Pane(), Game.SIZE, Game.SIZE);
		root = (Pane) getRoot();
		
		setOnKeyPressed(Keyboard::onKeyPress);
		setOnKeyReleased(Keyboard::onKeyRelease);

		mapCanvas = new MapCanvas(Game.SIZE, Game.SIZE);
		engine = Game.instance().getEngine();
		
		engine.setOnFrozen( this::loadLevel );
		Player player = new Player(5);
		engine.addEntity( player );
		mapCanvas.addEntity( player );
		mapCanvas.setFocus( player.getPos() );
		
		root.getChildren().add(mapCanvas);
		engine.start();
		engine.unfreeze();
		timer.start();
	}
	
	public void loadLevel() {
		engine.setMap(generator.generate( 100 ));
		mapCanvas.setMap(engine.getMap());
		engine.unfreeze();
	}
}
