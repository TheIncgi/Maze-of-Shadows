package app.ui.elements;

import app.misc.Keybinding;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


/**
 * Done as a Pane so it can be overlayed with the game in the background
 * */
public class SettingsPane extends BorderPane{
	Text text;
	GridPane grid;
	
	private Slider contrast = new Slider(-.2, .2, 0);
	private Slider brightness = new Slider(-.2, .2, 0);
	private Slider saturation = new Slider(-.2, .2, 0);
	public ColorAdjust colorAdjust;
	
	private Keybinding up 	   = new Keybinding(KeyCode.W);
	private Keybinding left    = new Keybinding(KeyCode.A);
	private Keybinding right   = new Keybinding(KeyCode.D);
	private Keybinding down    = new Keybinding(KeyCode.DOWN);
	
	Button back;
	
	public SettingsPane() {
		this.setCenter(new ScrollPane(grid = new GridPane()));
		
		
		
		
		int r=0;
		grid.add(new Label("Color Options: "), 0, r++, 2, 1);
		grid.addRow(r++, new Label("Brightness"), brightness);
		grid.addRow(r++, new Label("Contrast"), contrast);
		grid.addRow(r++, new Label("Saturation"), saturation);
		
		grid.addRow(r++, new Label("Keybindings:"));
		grid.addRow(r++, new Label("Up:"), up);
		grid.addRow(r++, new Label("Left:"), left);
		grid.addRow(r++, new Label("Down:"), down);
		grid.addRow(r++, new Label("Right:"), right);
		
		
		
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

}
