package app.ui.scenes;



import app.Game;
import app.engine.MapGenerator;
import app.ui.elements.MapCanvas;
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

public class MainMenu extends Scene {
	
	Pane root;
	MapCanvas canvas;
	MainMenuOptions buttonSet;
	SlidingPane sliding;

	public MainMenu(double wid, double hei) {
		super(new Pane(), wid, hei, Color.TRANSPARENT);
		
		canvas = new MapCanvas(wid, hei);
		MapGenerator mg = new MapGenerator();
		canvas.setMap(mg.generate());
		
		root = (Pane) getRoot();
		
		sliding = new SlidingPane(widthProperty(), heightProperty());
		
		buttonSet = new MainMenuOptions();
		buttonSet.layoutXProperty().bind(widthProperty().divide(2).subtract(buttonSet.widthProperty().divide(2)));
		buttonSet.layoutYProperty().bind(heightProperty().divide(2).subtract(buttonSet.heightProperty().divide(2)));
		root.getChildren().addAll(canvas, sliding);
		
		sliding.setCurrent(buttonSet);
		
		
		canvas.getMap().calculateLighting(null);
		canvas.draw();
		
		
	}

	
	
	public class MainMenuOptions extends VBox{
		public Button newGame, options, howTo, about, exit;
		public MainMenuOptions() {
			super(5);
			newGame = new Button("New Game");
			options = new Button("Options");
			howTo = new Button("How to Play");
			about = new Button("About");
			exit = new Button("Exit");
			
			this.getChildren().addAll(newGame, options, howTo, about, exit);
			this.setAlignment(Pos.CENTER);
			
			options.setOnAction(e->{
				SettingsPane p = Game.instance().getSettings();
				p.setOnReturn(()->{sliding.fromLeft(buttonSet);});
				sliding.fromRight(Game.instance().getSettings());
			});
			
			exit.setOnAction(e->{
				Game.instance().exit();
			});
		}
	}
	
	
}
