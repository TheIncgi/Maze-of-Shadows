package app.ui.elements;

import javafx.animation.AnimationTimer;
import javafx.animation.Transition;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**Animation fanciness*/
public class SlidingPane extends Pane{
	ReadOnlyDoubleProperty sceneWidth;
	ReadOnlyDoubleProperty sceneHeight;
	private Pane current;
	private AnimationTimer timer;
	
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
	
	public void fromUp(Pane next) {
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
			}
		};
		t.setOnFinished(e->{
			next.setDisable(false);
			this.getChildren().remove(current);
			current = next;
		});
		t.play();
	}
	public void fromDown(Pane next) {
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
			}
		};
		t.setOnFinished(e->{
			next.setDisable(false);
			this.getChildren().remove(current);
			current = next;
		});
		t.play();
	}
	public void fromLeft(Pane next) {
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
			}
		};
		t.setOnFinished(e->{
			next.setDisable(false);
			this.getChildren().remove(current);
			current = next;
		});
		t.play();
	}
	public void fromRight(Pane next) {
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
			}
		};
		t.setOnFinished(e->{
			next.setDisable(false);
			this.getChildren().remove(current);
			current = next;
		});
		
		t.play();
	}
}
