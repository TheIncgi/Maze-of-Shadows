package app.ui.elements;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import resources.R;

abstract public class BaseDrawable implements IDrawable{
	protected Image image;
	SimpleObjectProperty<Image> imageProperty;
	/**Get the image used to draw the current tile/entity*/
	public SimpleObjectProperty<Image> getImage() {
		if (image == null) {
			image = new Image(R.class.getResourceAsStream( getResourceName() ));
			imageProperty = new SimpleObjectProperty<Image>(image);
		}
		return imageProperty;
	}
	
	@Override
	public void draw(GraphicsContext g, double pixelx, double pixely, double width, double height) {
		//g.drawImage(getImage(), pixelx, pixely, width, height);
	}
	@Override
	public String getResourceName() {
		return "missing_texture.png";
	}
}
