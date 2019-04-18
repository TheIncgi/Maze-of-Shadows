package app.ui.scenes;



import app.Game;
import app.engine.MapGenerator;
import app.ui.elements.CreditsPane;
import app.ui.elements.HighScorePane;
import app.ui.elements.HowToPlayPane;
import app.ui.elements.MapCanvas;
import app.ui.elements.MapPane;
import app.ui.elements.SettingsPane;
import app.ui.elements.SlidingPane;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainMenu extends Scene {
	
	Pane root;
	//MapCanvas canvas;
	MapPane mapView;
	MainMenuOptions buttonSet;
	SlidingPane sliding;
	Stage stage;
	public MainMenu(Stage stage, double wid, double hei) {
		super(new Pane(), wid, hei, Color.TRANSPARENT);
		this.stage = stage;
		//canvas = new MapCanvas(wid, hei);
		mapView = new MapPane();
		MapGenerator mg = new MapGenerator();
		mg.setSeed( 0 );
		//canvas.setMap(mg.generate(20));
		mapView.setMap(mg.generate(40));
		
		root = (Pane) getRoot();
		
		sliding = new SlidingPane(widthProperty(), heightProperty());
		
		buttonSet = new MainMenuOptions();
		buttonSet.layoutXProperty().bind(widthProperty().divide(2).subtract(buttonSet.widthProperty().divide(2)));
		buttonSet.layoutYProperty().bind(heightProperty().divide(2).subtract(buttonSet.heightProperty().divide(2)));
		root.getChildren().addAll(mapView, sliding);
		
		sliding.setCurrent(buttonSet);
		
		
		
		//canvas.draw();
		
		
	}

	
	
	public class MainMenuOptions extends VBox{
		public Button newGame, options, howTo, about, exit, highScore;
		public MainMenuOptions() {
			super(5);
			newGame = new Button("New Game");
			options = new Button("Options");
			highScore = new Button("High Scores");
			howTo = new Button("How to Play");
			about = new Button("Credits");
			exit = new Button("Exit");
			
			this.getChildren().addAll(newGame, options, howTo, highScore, about, exit);
			this.setAlignment(Pos.CENTER);
			
			//TODO remove test code
			setOnKeyPressed(e->{
				if(e.getCode().equals(Game.instance().getSettings().getPauseKeycode())) {
					
					sliding.fromUp(Game.instance().getPausePane());
				}
			});
			
			newGame.setOnAction(e->{
				stage.setScene(new LevelView());
			});
			options.setOnAction(e->{
				SettingsPane p = Game.instance().getSettings();
				p.setOnReturn(()->{sliding.fromLeft(buttonSet);});
				sliding.fromRight(p);
			});
			about.setOnAction(e->{
				CreditsPane c = Game.instance().getCreditsPane();
				c.setOnReturn(()->{sliding.fromUp(buttonSet);});
				sliding.fromDown(c);
			});
			highScore.setOnAction(e->{
				HighScorePane h = Game.instance().getHighScorePane();
				h.setOnReturn(()->{sliding.fromRight(buttonSet);});
				sliding.fromLeft(h);
			});
			howTo.setOnAction(e->{
				HowToPlayPane h = Game.instance().getHowToPlayPane();
				h.setOnReturn(()->{sliding.fromDown(buttonSet);});
				sliding.fromUp(h);
			});
			
			exit.setOnAction(e->{
				Game.instance().exit();
			});
		}
	}
	
	
}
