package app;

import app.engine.Engine;
import app.ui.scenes.MainMenu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import resources.R;

public class Game extends Application{
	public static final String VERSION = "1.0.0";
	public static final int SIZE = 1000;
	private static Game instance;
	public static final String TITLE = "[WIP Title] Maze of Shadows - Version "+VERSION;
	
	private Engine engine;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public Game() {
		instance = this;
		engine = new Engine();
	}
	
	
	public static Image genericFloor;
	
	@Override
	public void start(Stage stage) throws Exception {
		genericFloor = new Image(R.class.getResourceAsStream("generic_floor.png"));
		Scene scene = new MainMenu(SIZE, SIZE);
		stage.setScene(scene);
		
		stage.setTitle(TITLE);
		
		stage.setResizable( false );
		
		stage.show();
	}
	
	
	
	public static Game instance() {
		return instance;
	}
	public Engine getEngine() {
		return engine;
	}
}
