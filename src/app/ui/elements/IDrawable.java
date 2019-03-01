package app.ui.elements;

import app.engine.tiles.Lighting;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import resources.R;

public interface IDrawable {
	public void draw(GraphicsContext g, double pixelx, double pixely, double width, double height);
	public Image getImage();
	public String getResourceName();
}
