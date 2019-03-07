package app.engine.items;

import app.engine.Map;
import app.engine.entity.Entity;
import app.ui.elements.IDrawable;

public class BaseItem {
	IDrawable icon;
	public BaseItem(IDrawable icon) {
		this.icon = icon;
	}
	
	public void use(Entity entity, Map map) {}
	
}
