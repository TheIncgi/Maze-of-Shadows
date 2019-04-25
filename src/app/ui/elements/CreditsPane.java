package app.ui.elements;

import app.Game;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
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
		box.setBackground(new Background(new BackgroundFill(Color.SLATEGRAY.deriveColor(0, 0, 0, .5), new CornerRadii(10), new Insets(0))));
		box.setPadding(new Insets(10));
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
