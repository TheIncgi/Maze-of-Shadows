package app.ui.elements;

import java.util.ArrayList;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

public class AnimatedDrawable extends BaseDrawable{
	ArrayList<Image> frames;
	private int fps;
	SimpleObjectProperty<Image> activeFrame;
	int cursor = 0;
	
	public AnimatedDrawable(int fps, ArrayList<Image> frames) {
		this.fps = fps;
		this.frames = frames;
		activeFrame = new SimpleObjectProperty<>(frames.get(0));
	}
	
	
	
	@Override
	public SimpleObjectProperty<Image> getImage() {
		return activeFrame;
	}
	public String getResourceName() {
		return null;
	};
	public void nextFrame() {
		activeFrame.set(frames.get(cursor = (cursor+1)%frames.size()));
	}
	long nextFrame = System.currentTimeMillis();
	public void onTick( long now ) {
		if(nextFrame <= now) {
			nextFrame();
			nextFrame=now+getframeDelay();
		}
	}
	
	public long getframeDelay() {
		return (long) (1000/fps);
	}
}
