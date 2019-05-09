package app.ui.elements;

import app.Game;
import app.misc.Utils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class PausePane extends Pane {
	Button unpause, help, settings;
	SlidingPane slide = new SlidingPane(this.widthProperty(), this.heightProperty());
	VBox box;
	public PausePane() {
		super();
		
		Label unpauseTip = new Label("Press ["+Game.instance().getSettings().getPauseKeyText().get()+"] to unpause");
		unpauseTip.setTextFill(Color.WHITE);
		Game.instance().getSettings().getPauseKeyText().addListener(e->{
			unpauseTip.setText("Press ["+Game.instance().getSettings().getPauseKeyText().get()+"] to unpause");
		});
		
		help  = new Button("Help");
		settings = new Button("Settings");
		Label pauseLabel = new Label("PAUSED");
		pauseLabel.setFont(new Font(35));
		pauseLabel.setTextFill(Color.WHITE);
		
		box = new VBox(8, pauseLabel,unpauseTip, help, settings);
		//Rectangle bg = new Rectangle(0, 0, Game.SIZE, Game.SIZE);
		setPrefSize(Game.SIZE, Game.SIZE);
		
		
		getChildren().addAll(/*bg,*/slide);
		slide.setCurrent(box);
		box.layoutXProperty().bind(widthProperty().divide(2).subtract(box.widthProperty().divide(2)));
		box.layoutYProperty().bind(heightProperty().divide(2).subtract(box.heightProperty().divide(2)));
		//b.setBackground(new Background(new BackgroundFill(Color.SLATEGRAY.deriveColor(0, 0, 0, .5), new CornerRadii(10), new Insets(0))));
		help.setOnAction(e->{
			HowToPlayPane htpp = Game.instance().getHowToPlayPane();
			htpp.setOnReturn(()->{
				slide.fromRight(box);
			});
			slide.fromLeft(htpp);
		});
		settings.setOnAction(e->{
			SettingsPane sets = Game.instance().getSettings();
			sets.setOnReturn(()->{
				slide.fromLeft(box);
			});
			slide.fromRight(sets);
		});
		
	}

	public void enableShop() {
		Button shop = new Button("Cheat even more");
		box.getChildren().add(shop);
		shop.setOnAction(e->{
			Utils.shop((item)->{
				
			});
		});
	}
}
