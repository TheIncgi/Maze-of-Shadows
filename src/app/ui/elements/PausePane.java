package app.ui.elements;

import app.Game;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class PausePane extends Pane {
	Button unpause, help, settings;
	
	public PausePane() {
		super();
		
		unpause = new Button("Unapuse");
		help  = new Button("Help");
		settings = new Button("Settings");
		Label pauseLabel = new Label("PAUSED");
		pauseLabel.setFont(new Font(35));
		pauseLabel.setTextFill(Color.WHITE);
		
		VBox box = new VBox(8, pauseLabel, unpause, help, settings);
		Rectangle bg = new Rectangle(0, 0, Game.SIZE, Game.SIZE);
		setPrefSize(Game.SIZE, Game.SIZE);
		
		getChildren().addAll(bg, box);
		box.layoutXProperty().bind(widthProperty().divide(2).subtract(box.widthProperty().divide(2)));
		box.layoutYProperty().bind(heightProperty().divide(2).subtract(box.heightProperty().divide(2)));
		
		
		
	}
}
