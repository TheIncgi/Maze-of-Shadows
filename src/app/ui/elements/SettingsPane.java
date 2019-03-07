package app.ui.elements;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
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
	
	Slider contrast = new Slider(-.2, .2, 0);
	Slider brightness = new Slider(-.2, .2, 0);
	Slider saturation = new Slider(-.2, .2, 0);
	ColorAdjust colorAdjust;
	Button back;
	
	public SettingsPane() {
		this.setCenter(new ScrollPane(grid = new GridPane()));
		
		
		
		
		int r=0;
		grid.add(new Label("Debug Options: "), 0, r++, 2, 1);
		grid.addRow(r++, new Label("Brightness"), brightness);
		grid.addRow(r++, new Label("Contrast"), contrast);
		grid.addRow(r++, new Label("Saturation"), saturation);
		
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

}
