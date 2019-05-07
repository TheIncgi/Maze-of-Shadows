package app.ui.elements;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public interface IDrawable {
	@Deprecated
	public void draw(GraphicsContext g, double pixelx, double pixely, double width, double height);
	public SimpleObjectProperty<Image> getImage();
	public String getResourceName();
}
