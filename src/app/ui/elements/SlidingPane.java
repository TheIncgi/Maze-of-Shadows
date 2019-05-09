package app.ui.elements;

import javafx.animation.Transition;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**Animation fanciness*/
public class SlidingPane extends Pane{
	private final Object ACT_LOCK = new Object();
	ReadOnlyDoubleProperty sceneWidth;
	ReadOnlyDoubleProperty sceneHeight;
	private Pane current;
	//private AnimationTimer timer;
	private Transition active;
	private Pane next;
	
	public static Duration transitionTime = Duration.millis(350);
	
	public SlidingPane(ReadOnlyDoubleProperty sceneWidth, ReadOnlyDoubleProperty sceneHeight) {
		super();
		this.sceneWidth = sceneWidth;
		this.sceneHeight = sceneHeight;
		
	}
	
	public void setCurrent(Pane p) {
		current = p;
		this.getChildren().clear();
		this.getChildren().add(p);
	}
	
	private void ifInterrupt() {
		synchronized (ACT_LOCK) {
			if(active==null) return;
			active.stop();
			this.getChildren().remove(current);
			current.setDisable( false );
			
			current = next;
			current.setDisable(false);
			current.setTranslateX(0);
			current.setTranslateY(0);
			active = null;
		}
	}
	
	
	public void fromUp(Pane next) {fromUp(next, null);}
	public void fromDown(Pane next) {fromDown(next, null);}
	public void fromLeft(Pane next) {fromLeft(next, null);}
	public void fromRight(Pane next) {fromRight(next, null);}
	
	public synchronized void fromUp(Pane next, final Extra extra) {
		ifInterrupt();
		this.next = next;
		this.getChildren().add(next);
		current.setDisable(true);
		next.setDisable(true);
		next.setTranslateX(0);
		next.setTranslateY(-sceneHeight.get());
		Transition t = new Transition() {
			{
				setCycleDuration(transitionTime);
			}
			@Override
			protected void interpolate(double frac) {
				current.setTranslateY(sceneHeight.get()*frac);
				next.setTranslateY(current.getTranslateY()-sceneHeight.get());
				if (extra != null) {
					extra.interpolate(frac);
				}
			}
		};
		synchronized(ACT_LOCK) {active=t;}
		t.setOnFinished(e->{
			next.setDisable(false);
			this.getChildren().remove(current);
			current = next;
			synchronized (ACT_LOCK) {
				active = null;
			}
			this.next = null;
			if(extra!=null) extra.onFinish();
		});
		t.play();
	}
	public synchronized void fromDown(Pane next, final Extra extra) {
		ifInterrupt();
		this.next = next;
		this.getChildren().add(next);
		current.setDisable(true);
		next.setDisable(true);
		next.setTranslateX(0);
		next.setTranslateY(sceneHeight.get());
		Transition t = new Transition() {
			{
				setCycleDuration(transitionTime);
			}
			@Override
			protected void interpolate(double frac) {
				current.setTranslateY(-sceneHeight.get()*frac);
				next.setTranslateY(current.getTranslateY()+sceneHeight.get());
				if (extra != null) {
					extra.interpolate(frac);
				}
			}
		};
		synchronized(ACT_LOCK) {active=t;}
		t.setOnFinished(e->{
			next.setDisable(false);
			this.getChildren().remove(current);
			current = next;
			synchronized (ACT_LOCK) {
				active = null;
			}
			this.next = null;
			if(extra!=null) extra.onFinish();
		});
		t.play();
	}
	public synchronized void fromLeft(Pane next, final Extra extra) {
		ifInterrupt();
		this.next = next;
		this.getChildren().add(next);
		current.setDisable(true);
		next.setDisable(true);
		next.setTranslateX(-sceneWidth.get());
		next.setTranslateY(0);
		Transition t = new Transition() {
			{
				setCycleDuration(transitionTime);
			}
			@Override
			protected void interpolate(double frac) {
				current.setTranslateX(sceneWidth.get()*frac);
				next.setTranslateX(current.getTranslateX()-sceneWidth.get());
				if (extra != null) {
					extra.interpolate(frac);
				}
			}
		};
		synchronized(ACT_LOCK) {active=t;}
		t.setOnFinished(e->{
			next.setDisable(false);
			this.getChildren().remove(current);
			current = next;
			synchronized (ACT_LOCK) {
				active = null;
			}
			this.next = null;
			if(extra!=null) extra.onFinish();
		});
		t.play();
	}
	public synchronized void fromRight(Pane next, final Extra extra) {
		ifInterrupt();
		this.next = next;
		this.getChildren().add(next);
		current.setDisable(true);
		next.setDisable(true);
		next.setTranslateX(sceneWidth.get());
		next.setTranslateY(0);
		Transition t = new Transition() {
			{
				setCycleDuration(transitionTime);
			}
			@Override
			protected void interpolate(double frac) {
				current.setTranslateX(-sceneWidth.get()*frac);
				next.setTranslateX(current.getTranslateX()+sceneWidth.get());
				if (extra != null) {
					extra.interpolate(frac);
				}
			}
		};
		synchronized(ACT_LOCK) {active=t;}
		t.setOnFinished(e->{
			next.setDisable(false);
			this.getChildren().remove(current);
			current = next;
			synchronized (ACT_LOCK) {
				active = null;
			}
			this.next = null;
			if(extra!=null) extra.onFinish();
		});
		
		t.play();
	}
	
	public static interface Extra{
		void interpolate(double frac);
		void onFinish();
	}
}
