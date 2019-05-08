package app.ui.elements;

import app.misc.Keybinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


/**
 * Done as a Pane so it can be overlayed with the game in the background
 * */
public class SettingsPane extends BorderPane{
	Text text;
	GridPane grid;
	
	private Slider contrast = new Slider(-.2, .2, 0);
	private Slider brightness = new Slider(-.2, .2, 0);
	private Slider saturation = new Slider(-.2, .2, 0);
	
	private Slider masterVolume = new Slider(0, 100, 75);
	private Slider music = new Slider(0, 100, 75);
	private Slider effects = new Slider(0, 100, 75);
	
	public ColorAdjust colorAdjust;
	
	private Keybinding up 	   = new Keybinding(KeyCode.W);
	private Keybinding left    = new Keybinding(KeyCode.A);
	private Keybinding right   = new Keybinding(KeyCode.D);
	private Keybinding down    = new Keybinding(KeyCode.S);
	private Keybinding pauseKey= new Keybinding(KeyCode.P);
	private Keybinding sprint  = new Keybinding(KeyCode.SHIFT);
	

	
	Button back;
	
	public SettingsPane() {

		this.setCenter(grid = new GridPane());
		
		grid.setAlignment(Pos.CENTER);
		
		grid.setBackground(new Background(new BackgroundFill(Color.SLATEGRAY.deriveColor(0, 0, 0, .5), new CornerRadii(10), new Insets(0))));
		grid.setPadding(new Insets(10));
		ColumnConstraints c1, c2;
		c1 = new ColumnConstraints();
		c2 = new ColumnConstraints();
		c2.setHgrow(Priority.ALWAYS);
		grid.getColumnConstraints().addAll(c1, c2);
		
		int r=0;
		grid.add(heading("Color Options: "), 0, r++, 2, 1);
		grid.addRow(r++, label("Brightness"), brightness);
		grid.addRow(r++, label("Contrast"), contrast);
		grid.addRow(r++, label("Saturation"), saturation);
		
		grid.addRow(r++, heading("Keybindings:"));
		grid.addRow(r++, label("Up:"), up);
		grid.addRow(r++, label("Left:"), left);
		grid.addRow(r++, label("Down:"), down);
		grid.addRow(r++, label("Right:"), right);
		grid.addRow(r++, label("Sprint:"), sprint);
		grid.addRow(r++, label("Pause:"), pauseKey);
		
		grid.addRow(r++, heading("Sound options:"));
		grid.addRow(r++, label("Master Volume:"), masterVolume);
		grid.addRow(r++, label("Music:"), music);
		grid.addRow(r++, label("Effects:"), effects);
		setBottom(back = new Button("Back"));
		back.setOnAction(e->{if(onReturn!=null)onReturn.run();});
		
		grid.setAlignment(Pos.CENTER);
		colorAdjust = new ColorAdjust();
		
		brightness.valueProperty().addListener((e)->{
			colorAdjust.setBrightness(brightness.getValue());
		});
		contrast.valueProperty().addListener(e->{
			colorAdjust.setContrast(contrast.getValue());
		});
		saturation.valueProperty().addListener(e->{
			colorAdjust.setSaturation(saturation.getValue());
		});
		
		
		extra(brightness);
		extra(contrast);
		extra(saturation);
		extra(masterVolume);
		extra(music);
		extra(effects);
		
	}
	
	private Label label(String text) {
		Label l = new Label(text);
		l.setTextFill(Color.WHITE);
		return l;
	}
	private Label heading(String text) {
		Label l = label(text);
		Font f = l.getFont();
		l.setFont(new Font(f.getName(), f.getSize()*1.3));
		return l;
	}
	
	private void extra(Slider s) {
		Tooltip tip = new Tooltip();
		s.valueProperty().addListener(e->{tip.setText(String.format("%.3f", s.valueProperty().get()));});
		Tooltip.install(s, tip);
		
		s.setMajorTickUnit((s.getMax()-s.getMin())/10);
		s.setMinorTickCount(2);
		s.setSnapToTicks(true);
		s.setShowTickMarks(true);
	}
	
	private Runnable onReturn;
	public void setOnReturn(Runnable r) {
		onReturn = r;
	}
	
	public KeyCode getUpKeycode() {
		return up.getKeyCode();
	}
	public KeyCode getDownKeycode() {
		return down.getKeyCode();
	}
	public KeyCode getLeftKeycode() {
		return left.getKeyCode();
	}
	public KeyCode getRightKeycode() {
		return right.getKeyCode();
	}
	public KeyCode getPauseKeycode() {
		return pauseKey.getKeyCode();
	}
	public KeyCode getSprintKeycode() {
		return sprint.getKeyCode();
	}
	
	public DoubleProperty getMasterVolume() {
		return masterVolume.valueProperty();
	}
	public DoubleProperty getMusicVolume() {
		return music.valueProperty();
	}
	public DoubleProperty getSfxVolume() {
		return effects.valueProperty();
	}
	
	public StringProperty getPauseKeyText() {
		return pauseKey.textProperty();
	}
}
