package app.ui.elements;

import app.Game;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameHUD extends BorderPane{
	double height = 25;
	
	HBox top = new HBox(5);
	Label healthLabel = new Label("Health:");
	public ProgressBar healthBar = new ProgressBar();
	
	Label staminaLabel = new Label("Stamina:");
	public ProgressBar staminaBar = new ProgressBar();
	
	public Circle debug1 = new Circle(height/2, Color.GRAY);
	public Circle debug2 = new Circle(height/2, Color.GRAY);
	public Circle debug3 = new Circle(height/2, Color.GRAY);
	public Circle debug4 = new Circle(height/2, Color.GRAY);
	
	public Label debugText = new Label();
	
	public GameHUD() {
		super();
		this.setTop(top);
		
		top.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), new Insets(0))));
		
		healthBar.setProgress(1);
		healthBar.setPrefSize(Game.SIZE/5, height);
		
		staminaBar.setProgress(1);
		staminaBar.setPrefSize(Game.SIZE/5, height);
		
		debugText.setBackground( new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), new Insets(0))) );
		
		top.getChildren().addAll(healthLabel, healthBar, debug1, debug2, debug3, debug4, staminaLabel, staminaBar);
		setBottom(debugText);
	}
}
