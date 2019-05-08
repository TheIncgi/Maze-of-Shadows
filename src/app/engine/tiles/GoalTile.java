package app.engine.tiles;

import app.ui.elements.BaseDrawable;
import app.ui.elements.IDrawable;

public class GoalTile extends BaseTile{

	@Override
	public boolean isPassable() {
		return true;
	}

	@Override
	public boolean isOpaque() {
		return false;
	}

	
	
	private IDrawable drawable = new BaseDrawable() {
		@Override
		public String getResourceName() {
			return "goal.png";
		}
	};
	@Override
	public IDrawable getDrawable() {
		return drawable;
	}

}
