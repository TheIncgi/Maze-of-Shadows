package app.ui.elements;

import app.engine.tiles.Lighting;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import resources.R;

abstract public class BaseDrawable implements IDrawable{
	protected Image image;
	
	/**Get the image used to draw the current tile/entity*/
	public Image getImage() {
		if (image == null) {
			image = new Image(R.class.getResourceAsStream( getResourceName() ));
		}
		return image;
	}
	
	@Override
	public void draw(GraphicsContext g, double pixelx, double pixely, double width, double height) {
		g.drawImage(getImage(), pixelx, pixely, width, height);
	}
	
	public String getResourceName() {
		return "missing_texture.png";
	}
}
