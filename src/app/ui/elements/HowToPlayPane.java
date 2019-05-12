package app.ui.elements;

import app.Game;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
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
		Label theText = new Label("Dodge the monsters!\nCollect as much gold as you can!");
		
		
		
		theHeading.setFont(new Font(35));
		theText.setFont(new Font(15));
		theHeading.setTextFill(Color.WHITE);
		theText.setTextFill(Color.WHITE);
		theText.setWrapText(true);
		VBox box = new VBox(8, theHeading, theText, back);
		box.setBackground(new Background(new BackgroundFill(Color.SLATEGRAY.deriveColor(0, 0, 0, .5), new CornerRadii(10), new Insets(0))));
		box.setPadding(new Insets(10));
		this.getChildren().add(box);
		box.setAlignment(Pos.CENTER);
		box.translateXProperty().bind(widthProperty().divide(2).subtract(box.widthProperty().divide(2)));
		box.translateYProperty().bind(heightProperty().divide(2).subtract(box.heightProperty().divide(2)));
		box.setPrefWidth(Game.SIZE*.8);
		theText.maxWidthProperty().bind(box.widthProperty());
		
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
