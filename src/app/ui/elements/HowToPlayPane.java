package app.ui.elements;

import app.Game;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class HowToPlayPane extends Pane{
	Button back;
	public HowToPlayPane() {
		super();
		setPrefSize(Game.SIZE, Game.SIZE);
		back = new Button("Back");
		
		Label theHeading = new Label("How to Play:");
		Label theText = new Label("The player navigates though the maze using the\nmovement keys while trying to avoid monsters and collect loot\nThe player can use items using item keybindings."
				+ "\nExample text\nExample text\nExample text....");
		
		theHeading.setFont(new Font(35));
		theText.setFont(new Font(20));
		theHeading.setTextFill(Color.WHITE);
		theText.setTextFill(Color.WHITE);
		VBox box = new VBox(8, theHeading, theText, back);
		this.getChildren().add(box);
		box.setAlignment(Pos.CENTER);
		box.translateXProperty().bind(widthProperty().divide(2).subtract(box.widthProperty().divide(2)));
		box.translateYProperty().bind(heightProperty().divide(2).subtract(box.heightProperty().divide(2)));
		
		back.setOnAction(e->{
			if (onReturn != null)
				onReturn.run();
		});
	}
	
	private Runnable onReturn;
	public void setOnReturn( Runnable r ) {
		this.onReturn = r;
	}
}
