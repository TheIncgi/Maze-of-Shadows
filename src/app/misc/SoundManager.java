package app.misc;

import java.io.IOException;
import java.util.LinkedList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import app.Game;
import app.ui.elements.SettingsPane;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;



import resources.R;

public class SoundManager {
	public static enum Sounds{
		BACKGROUND_1,
		WARNING,
		CASH;
		public String getResName() {
			
			switch(this) {
			case BACKGROUND_1:
				return "Into the maze.wav";
			case WARNING:
				return "unease.wav";
			case CASH:
				return "cash.wav";
			default:
				return null;	
			}
			
		}
	}
	private static LinkedList<Clip> activeSounds = new LinkedList<>();
	
	public static enum SoundChannel{
		MASTER,
		MUSIC,
		SFX;
		
		public DoubleProperty getSoundProperty() {
			SettingsPane sets = Game.instance().getSettings();
			switch (this) {
			case MASTER:
				return sets.getMasterVolume();
			case MUSIC:
				return sets.getMusicVolume();
			case SFX:
				return sets.getSfxVolume();
			default:
				return null;
			}
		}
	}
	
	
	
	private static Clip getClip(String resName) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
		if(resName==null) return null;
		Clip clip = AudioSystem.getClip();
		clip.open(  AudioSystem.getAudioInputStream(R.class.getResourceAsStream(resName)));
		return clip;
	}
	
	public static Clip playSound(Sounds sound, SoundChannel channel) {
		Clip clip;
		try {
			clip = getClip( sound.getResName() );
			if(clip==null) {System.err.println("Warning: missing sound '"+sound.name()+":"+sound.getResName()+"'"); return null;};
			regVolumeListener(clip, channel);
			clip.start();
			return clip;
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
			return null;
		}
	}
	private static void regVolumeListener(final Clip clip, final SoundChannel sc) {
		DoubleProperty volume = sc.getSoundProperty();
		volume.addListener(new WeakChangeListener<Number>(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				setClipVolume(clip, newValue.doubleValue());
			}
		}));
		
		clip.addLineListener((event)->{
			if(event.getType() .equals( Type.STOP )) {
				activeSounds.remove(clip);
				clip.close();
				
			}else if(event.getType().equals(Type.START)) {
				activeSounds.add(clip);
			}
		});
	}
	
	public void stopAll() {
		
	}
	
	private static void setClipVolume(Clip clip, double level) {
		FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		float range = volume.getMaximum() - volume.getMinimum();
		float gain = (float) (range * level + volume.getMinimum());
		volume.setValue(gain);
	}
}
