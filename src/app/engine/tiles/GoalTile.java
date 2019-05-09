package app.engine.tiles;

import app.Game;
import app.engine.entity.Entity;
import app.engine.entity.Player;
import app.misc.IntegerPosition;
import app.misc.Keyboard;
import app.misc.Utils;
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
	
	@Override
	public void onEnter(Entity e, IntegerPosition tilePos) {
		super.onEnter(e, tilePos);
		if(e instanceof Player){
			Keyboard.disableInput();
			Game.instance().getEngine().setPaused(true);
			Utils.contiuneMazePrompt((result)->{
				if(result == null || result.equals(Utils.later)) {
					Game.instance().getEngine().setPaused(false);
					Keyboard.enableInput();
				}else if(result.equals(Utils.decend)) {
					Game.instance().getEngine().freeze();
					Game.instance().getEngine().setPaused(false);
					Keyboard.enableInput();
				}else if(result.equals(Utils.shop)) {
					Utils.shop(item->{
						
					}/*, TODO random items*/);
					Game.instance().getEngine().freeze();
					Game.instance().getEngine().setPaused(false);
					Keyboard.enableInput();
				}else{
					Game.instance().getEngine().setPaused(false);
					Keyboard.enableInput();
				}
				
			});
			
			
			
		}
	}
	
}
