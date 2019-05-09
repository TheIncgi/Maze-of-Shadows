package app.engine.items;

import app.engine.Map;
import app.engine.entity.Entity;
import app.ui.elements.IDrawable;

abstract public class BaseItem implements IDrawable{
	public BaseItem() {
	}
	abstract public void use(Entity entity, Map map);
	
	/**Can reference current level to scale cost*/
	abstract public int getCost();
	abstract public String getItemName() ;
	
}
