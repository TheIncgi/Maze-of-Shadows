package app.ui.scenes;

import app.Game;
import app.engine.Engine;
import app.engine.Map;
import app.engine.MapGenerator;
import app.engine.entity.Player;
import app.ui.elements.MapCanvas;
import app.ui.elements.PausePane;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class LevelView extends Scene{
	MapGenerator generator = new MapGenerator();
	Engine engine;
	PausePane pauseOverlay = Game.instance().getPausePane();
	Pane root;
	MapCanvas mapCanvas;
	
	public LevelView() {
		super(new Pane(), Game.SIZE, Game.SIZE);
		root = (Pane) getRoot();
		mapCanvas = new MapCanvas(Game.SIZE, Game.SIZE);
		engine = new Engine();
		engine.setUiUpdater( mapCanvas::draw );
		engine.setOnFrozen( this::loadLevel );
		Player player = new Player(5);
		
		engine.addTickListner( player );
		mapCanvas.
	}
	
	public void loadLevel() {
		engine.setMap(generator.generate());
		mapCanvas.setMap(engine.getMap());
	}
}
