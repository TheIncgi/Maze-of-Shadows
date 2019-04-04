package app.ui.elements;

import app.Game;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class CreditsPane extends Pane {
	Button back;
	public CreditsPane() {
		super();
		setPrefSize(Game.SIZE, Game.SIZE);
		VBox box = new VBox();
		back = new Button("Back");
		box.getChildren().addAll(
				label("Credits:", 35),
				new HBox(5, label("Jose - [Description]", 20)),
				new HBox(5, label("Jaime - [Description]", 20)),
				new HBox(5, label("Ian - [Description]", 20))
				);
		box.setAlignment(Pos.CENTER);
		box.translateXProperty().bind(widthProperty().divide(2).subtract(box.widthProperty().divide(2)));
		box.translateYProperty().bind(heightProperty().divide(2).subtract(box.heightProperty().divide(2)));
		
		getChildren().addAll(box, back);
		back.setOnAction(e->{
			if (onReturn != null)
				onReturn.run();
		});
	}
	
	private Label label(String text, int size) {
		Label l = new Label(text);
		l.setFont(new Font("consolas", size));
		l.setTextFill(Color.WHITE);
		l.setAlignment(Pos.CENTER);
		return l;
	}
	
	private Runnable onReturn;
	public void setOnReturn( Runnable r ) {
		this.onReturn = r;
	}
	
}
