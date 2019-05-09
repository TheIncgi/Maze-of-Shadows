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
import app.engine.Engine;
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
		CASH,
		OUCH,
		WIND_SUSSTAIN,
		WIND_FADE_IN;
		public String getResName() {
			
			switch(this) {
			case BACKGROUND_1:
				return "Into the maze.wav";
			case WARNING:
				return "unease.wav";
			case CASH:
				return "cash.wav";
			case WIND_FADE_IN:
				return "wind_fadein.wav";
			case WIND_SUSSTAIN:
				return "wind.wav";
			case OUCH:
				return "ouch.wav";
			default:
				return null;	
			}
			
		}
	}
	private static LinkedList<Clip> activeSounds = new LinkedList<>();
	
	public static enum SoundChannel{
		MASTER,
		MUSIC,
		SFX,
		ATMOSPHERE;
		
		public DoubleProperty getSoundProperty() {
			SettingsPane sets = Game.instance().getSettings();
			switch (this) {
			case MASTER:
				return sets.getMasterVolume();
			case MUSIC:
				return sets.getMusicVolume();
			case SFX:
				return sets.getSfxVolume();
			case ATMOSPHERE:
				return sets.getAtmosphereVolume();
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
	
	public static void playThenLoop(Sounds fadeIn, Sounds susstain, SoundChannel channel) {
		Clip in = getSound(fadeIn, channel);
		Clip sus= getSound(susstain, channel);
		in.addLineListener(new LineListener() {
			@Override
			public void update(LineEvent event) {
				if(event.getType().equals(Type.STOP) && Game.instance().getEngine().isRunning()) {
					sus.loop(Clip.LOOP_CONTINUOUSLY);
					sus.start();
				}
			}
		});
		
		regVolumeListener(in, channel);
		regVolumeListener(sus, channel);
		in.start();
		
	}
	
	public static Clip playSound(Sounds sound, SoundChannel channel) { 
		Clip c = getSound(sound, channel); 
		c.start(); 
		return c;
	}
	public static Clip getSound(Sounds sound, SoundChannel channel) {
		Clip clip;
		try {
			clip = getClip( sound.getResName() );
			if(clip==null) {System.err.println("Warning: missing sound '"+sound.name()+":"+sound.getResName()+"'"); return null;};
			regVolumeListener(clip, channel);
			return clip;
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
			return null;
		}
	}
	private static void regVolumeListener(final Clip clip, final SoundChannel sc) {
		DoubleProperty volume = sc.getSoundProperty();
		
		ChangeListener<Number> listener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				setClipVolume(clip, sc.equals(SoundChannel.MASTER)?1:volume.get());
			}
		};
		
		volume.addListener( listener );
		
		if(!sc.equals(SoundChannel.MASTER)) {
			SoundChannel.MASTER.getSoundProperty().addListener( listener );
			setClipVolume(clip, SoundChannel.MASTER.getSoundProperty().get() * sc.getSoundProperty().get());
		}else {
			setClipVolume(clip, SoundChannel.MASTER.getSoundProperty().doubleValue());
		}
		
		clip.addLineListener((event)->{
			if(event.getType() .equals( Type.STOP )) {
				activeSounds.remove(clip);
				clip.close();
				sc.getSoundProperty().removeListener(listener);
				if(!sc.equals(SoundChannel.MASTER))
					SoundChannel.MASTER.getSoundProperty().removeListener(listener);
				
			}else if(event.getType().equals(Type.START)) {
				activeSounds.add(clip);
			}
		});
		
	}
	
	public static void stopAll() {
		while(activeSounds.size()>0)
			activeSounds.removeFirst().stop();
	}
	
	private static void setClipVolume(Clip clip, double level) {
		level *= SoundChannel.MASTER.getSoundProperty().get();
		FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		float range = volume.getMaximum() - volume.getMinimum();
		float gain = (float) (range * level + volume.getMinimum());
		volume.setValue(gain);
	}
}
