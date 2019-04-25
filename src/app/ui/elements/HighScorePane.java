package app.ui.elements;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import app.Game;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class HighScorePane extends Pane{
	ArrayList<ScoreEntry> entries = new ArrayList<>();
	Button back;
	
	public HighScorePane() {
		super();
		
		
		back = new Button("Back");
		//dummy data
		entries.add( new ScoreEntry("Person", 9001, 		Date.from(Instant.now())));
		entries.add( new ScoreEntry("Someone else", 7235, 	Date.from(Instant.ofEpochMilli((long) (Math.random()* System.currentTimeMillis())))) );
		entries.add( new ScoreEntry("Name", 4252, 			Date.from(Instant.ofEpochMilli((long) (Math.random()* System.currentTimeMillis())))) );
		entries.add( new ScoreEntry("etc", 3394, 			Date.from(Instant.ofEpochMilli((long) (Math.random()* System.currentTimeMillis())))) );
		entries.add( new ScoreEntry("etc", 3386, 			Date.from(Instant.ofEpochMilli((long) (Math.random()* System.currentTimeMillis())))) );
		
		
		
		GridPane grid = new GridPane();
		grid.setBackground(new Background(new BackgroundFill(Color.SLATEGRAY.deriveColor(0, 0, 0, .5), new CornerRadii(10), new Insets(0))));
		grid.setHgap(8);
		int row = 0;
		grid.addRow(row++, heading("Place"), heading("Name"),heading("Score"),heading("Date"));
		grid.setAlignment(Pos.CENTER);
		for( ScoreEntry e : entries) {
			grid.addRow(row++, heading(String.valueOf(row-1)), label(e.getName()), label(String.valueOf(e.getScore())), label(e.getDate().toString()));
			
		}
		
		getChildren().addAll(grid, back);
		//setAlignment(Pos.CENTER);
		grid.translateXProperty().bind(widthProperty().divide(2).subtract(grid.widthProperty().divide(2)));
		grid.translateYProperty().bind(heightProperty().divide(2).subtract(grid.heightProperty().divide(2)));
		grid.maxWidthProperty().bind(widthProperty().multiply(.8) );
		
		setPrefSize(Game.SIZE, Game.SIZE);
		
		back.setOnAction(e->{
			if(onReturn!=null)
				onReturn.run();
		});
	}
	
	private Label label(String text) {
		Label l = new Label(text);
		l.setFont(new Font("consolas", 12));
		l.setWrapText(true);
		l.setTextFill(Color.WHITE);
		l.setAlignment(Pos.CENTER);
		return l;
	}
	private Label heading(String text) {
		Label l = new Label(text);
		l.setFont(new Font("consolas", 15));
		l.setTextFill(Color.WHITE);
		l.setAlignment(Pos.CENTER);
		l.setWrapText(true);
		return l;
	}
	
	
	//used so Sliding pane can be used when going back
	private Runnable onReturn;
	public void setOnReturn(Runnable onBack) {
		onReturn = onBack;
	}
	
	private class ScoreEntry implements Serializable{
		/**
		 * Generated serial UID 
		 */
		private static final long serialVersionUID = -8969846080852422352L;

		private final String name;
		private final long score;
		private final Date date;
		public ScoreEntry(String name, long score, Date date) {
			super();
			this.name = name;
			this.score = score;
			this.date = date;
		}
		public String getName() {
			return name;
		}
		public long getScore() {
			return score;
		}
		public Date getDate() {
			return date;
		}
		
	}
}
