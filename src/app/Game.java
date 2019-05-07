package app;

import app.engine.Engine;
import app.ui.elements.CreditsPane;
import app.ui.elements.GameHUD;
import app.ui.elements.HighScorePane;
import app.ui.elements.HowToPlayPane;
import app.ui.elements.PausePane;
import app.ui.elements.SettingsPane;
import app.ui.scenes.LevelView;
import app.ui.scenes.MainMenu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import resources.R;

public class Game extends Application{
	public static final String VERSION = "1.0.0";
	public static final int SIZE = 500;
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
	private SettingsPane settings;
	private HighScorePane highScorePane;
	private CreditsPane creditsPane;
	private LevelView levelView;
	private HowToPlayPane howToPlayPane;
	private Stage stage;
	private GameHUD gameHud;
	private PausePane pausePane;
	private MainMenu mainMenu;

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		settings = new SettingsPane();
		highScorePane = new HighScorePane();
		creditsPane = new CreditsPane();
		howToPlayPane = new HowToPlayPane();
		pausePane = new PausePane();
		gameHud = new GameHUD();
		genericFloor = new Image(R.class.getResourceAsStream("generic_floor_whiter.png"));
		Scene scene = mainMenu = new MainMenu(stage, SIZE, SIZE);
		stage.setScene(scene);
		
		stage.setTitle(TITLE);
		
		stage.setResizable( false );
		
		stage.setOnCloseRequest(this::onClose);
		
		stage.show();
	}
	
	
	public void onClose(WindowEvent event) {
		engine.stop();
	}
	
	public static Game instance() {
		return instance;
	}
	public Engine getEngine() {
		return engine;
	}
	
	public GameHUD getGameHud() {
		return gameHud;
	}
	
	public SettingsPane getSettings() {
		return settings;
	}
	
	public HowToPlayPane getHowToPlayPane() {
		return howToPlayPane;
	}
	
	public CreditsPane getCreditsPane() {
		return creditsPane;
	}
	
	public HighScorePane getHighScorePane() {
		return highScorePane;
	}
	
	public MainMenu getMainMenu() {
		return mainMenu;
	}
	public PausePane getPausePane() {
		return pausePane;
	}
	public LevelView getLevelView() {
		return levelView;
	}
	public void exit() {
		stage.close();
	}

	public void setLevelView(LevelView levelView) {
		this.levelView = levelView;
	}
	/**Returns number of pixels 1 tile takes up*/
	public double getPixelPerTile() {
		return Game.instance().getLevelView().getMapPane().pixelsPerTile();
	}
}
