package app.misc;

import java.util.Optional;

import app.Game;
import app.engine.entity.Player;
import app.engine.items.BaseItem;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;

public class Utils {
	public static double clamp(double in, double low, double high) {
		return Math.max(low, Math.min(high, in));
	}
	public static boolean inRange(float x, float low, float high){
		return low <= x && x < high;
	}

	/**For continue maze prompt*/
	public static final ButtonType later = new ButtonType("Not yet..."), decend = new ButtonType("Decend"), shop = new ButtonType("Shop");
	public static void contiuneMazePrompt(OnResult onResult) {
		confirmDialog("Choose Action", "Will you decent futher into the maze?", onResult, later, decend,shop);
	}

	public static void confirmDialog(String title, String message, OnResult onResult, ButtonType... buttons) {
		Platform.runLater(()->{
			Alert alert = new Alert(AlertType.NONE);
			alert.setContentText(message);
			alert.setTitle(title);
			alert.getButtonTypes().clear();
			alert.getButtonTypes().addAll(buttons);
			alert.resultProperty().addListener(e->{
				onResult.onResult(alert.getResult());
			});
			alert.setOnCloseRequest(e->{
				if(alert.getResult()==null)
					alert.setResult(later);
			});
			alert.show();
		});
	}
	
	public static void shop(OnItemSelectedFromShop oisfs, BaseItem... items) {
		Platform.runLater(()->{
			Stage stage = new Stage();
			ScrollPane scroll = new ScrollPane();
			stage.setScene(new Scene(scroll, Game.SIZE/2, Game.SIZE/3));
			VBox vbox = new VBox(5);
			scroll.setContent(vbox);
			for(BaseItem i : items) {
				ImageView iv = new ImageView();
				iv.imageProperty().bind(i.getImage());
				
				HBox h = new HBox(5, iv, new Label(String.format("%d %s for %d gold", 1, i.getItemName(), i.getCost())));
				if(i.getCost() > Game.instance().getLevelView().getPlayer().getGold())
					h.setDisable(true);
				vbox.getChildren().add(h);
				h.setOnMouseEntered(e->{h.setEffect(new ColorAdjust(0, 0, .3, .3));});
				h.setOnMouseExited(e->{h.setEffect(null);});
				h.setOnMouseClicked(e->{
					oisfs.onSelect(i);
					stage.close();
				});
			}
			Button neverMind = new Button("Nevermind...");
			vbox.getChildren().add(neverMind);
			neverMind.setOnAction(e->{
				oisfs.onSelect(null);
				stage.close();
			});
			stage.setOnCloseRequest(e->{oisfs.onSelect(null);});
			stage.show();
		});
	}

	@FunctionalInterface
	public static interface OnResult {
		public void onResult(ButtonType button);
	}
	@FunctionalInterface
	public static interface OnItemSelectedFromShop {
		public void onSelect(BaseItem item);
	}

	public static double distance(int x, int y, int i, int j) {
		double dx = i-(double)x;
		double dy = j-(double)y;
		return Math.sqrt(dx*dx + dy*dy);
	}
	public static double distance(double x, double y, double i, double j) {
		double dx = i-(double)x;
		double dy = j-(double)y;
		return Math.sqrt(dx*dx + dy*dy);
	}
}
