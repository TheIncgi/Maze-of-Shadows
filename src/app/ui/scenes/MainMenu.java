package app.ui.scenes;



import app.engine.MapGenerator;
import app.ui.elements.MapCanvas;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class MainMenu extends Scene {
	
	MapCanvas canvas;
	//Rectangle background;
	public MainMenu(double wid, double hei) {
		super(new Pane(), wid, hei, Color.TRANSPARENT);
		//background = new Rectangle(wid, hei);
		//background.setFill(new RadialGradient(0d, 0, wid/2, hei/2, Math.min(wid, hei)/2, false, CycleMethod.NO_CYCLE, new Stop(0,  Color.TRANSPARENT), new Stop(.5,  Color.BLACK)));
		canvas = new MapCanvas(wid, hei);
		MapGenerator mg = new MapGenerator();
		canvas.setMap(mg.generate());
		Pane pane = (Pane) getRoot();
		//pane.getChildren().add(background);
		pane.getChildren().add(canvas);
		
		canvas.getMap().calculateLighting(null);
		canvas.draw();
	}
	
	
}
