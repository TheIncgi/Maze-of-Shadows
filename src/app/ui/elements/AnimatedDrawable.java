package app.ui.elements;

import java.io.File;
import java.util.ArrayList;

import javafx.scene.image.Image;
import resources.R;

public class AnimatedDrawable extends BaseDrawable{
	private ArrayList<Image> frames;
	// how long to display each frame
	private long frameTime = 200; //ms
	private String srcFolder;
	
	public AnimatedDrawable(String resFolderName) {
		File folder = new File(R.class.getResource(resFolderName).getFile());
		if(!folder.isDirectory()) {throw new IllegalArgumentException("Folder expected for animated resource");}
		for(int i = 0; true; i++) {
			File tmp = new File(folder, i+".png");
			if(!tmp.exists()) break;
			frames.add(new Image(R.class.getResourceAsStream(resFolderName+File.pathSeparator+i+".png")));
		}
		srcFolder = resFolderName;
	}
	
	/**Set the framerate for the animation rounded to the nearest millisecond*/
	public void setFPS(double fps) {
		frameTime = Math.round(1000/fps);
	}
	
	@Override
	public Image getImage() {
		return frames.get((int) (System.currentTimeMillis() / frameTime % frames.size()) );
	}
	
	@Override
	public String getResourceName() {
		return srcFolder;
	}
}
