package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import app.engine.Engine;
import app.ui.elements.CreditsPane;
import app.ui.elements.GameHUD;
import app.ui.elements.HighScorePane;
import app.ui.elements.HowToPlayPane;
import app.ui.elements.MapPane;
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
	
	private static HashMap<String, Serializable> userdata;

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		settings = new SettingsPane();
		highScorePane = new HighScorePane();
		creditsPane = new CreditsPane();
		howToPlayPane = new HowToPlayPane();
		pausePane = new PausePane();
		gameHud = new GameHUD();
		levelView = new LevelView();
		genericFloor = new Image(R.class.getResourceAsStream("generic_floor_whiter.png"));
		loadUserData();
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

	
	/**Returns number of pixels 1 tile takes up*/
	public static double getPixelPerTile() {
		return MapPane.pixelsPerTile();
	}

	
	private static final File userDataFile = new File("MazeOfShadows.userData");
	public static void saveUserdata() {
		if(userdata == null) return;
		instance.settings.exportSettings(userdata);
		instance.highScorePane.exportSettings(userdata);
		try(FileOutputStream fos = new FileOutputStream(userDataFile); ObjectOutputStream oos = new ObjectOutputStream(fos);){
			oos.writeObject(userdata);
			System.out.println("User data saved!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public static void loadUserData() {
		if(!userDataFile.exists()) {
			userdata = new HashMap<>();
			saveUserdata();
		}else {
			try(FileInputStream fis = new FileInputStream(userDataFile); ObjectInputStream ois = new ObjectInputStream(fis)){
				userdata = (HashMap<String, Serializable>) ois.readObject();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		instance.settings.importSettings(userdata);
		instance.highScorePane.importSettings(userdata);
		System.out.println("User data loaded");
	}
}
