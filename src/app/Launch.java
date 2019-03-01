package app;

import app.ui.scenes.MainMenu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import resources.R;

public class Launch extends Application{
	
	public static final int SIZE = 1000;
	
	private static Launch instance;
	public static final String TITLE = "Maze of Shadows";
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public Launch() {
		instance = this;
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
	
	
	
	public static Launch instance() {
		return instance;
	}
}
