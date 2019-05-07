package app.ui.elements;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import resources.R;

public class ToggleDrawable implements IDrawable{
	String res1, res2;
	Image a, b;
	SimpleBooleanProperty property = new SimpleBooleanProperty(false); //false is a, true is b
	SimpleObjectProperty<Image> active = new SimpleObjectProperty<>();
	
	public ToggleDrawable(String res1, String res2) {
		this.res1 = res1;
		this.res2 = res2;
		a = new Image(R.class.getResourceAsStream(res1));
		b = new Image(R.class.getResourceAsStream(res2));
		active.set(a);
		property.addListener(e->{active.set(property.get()?b:a);});
	}
	
	@Override
	public SimpleObjectProperty<Image> getImage() {
		return active;
	}

	@Override
	public void draw(GraphicsContext g, double pixelx, double pixely, double width, double height) {}

	@Override
	public String getResourceName() {
		return property.get()?res2 : res1;
	}
	
	/**The toggle property*/
	public SimpleBooleanProperty getProperty() {
		return property;
	}
}
