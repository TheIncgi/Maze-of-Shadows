package app.misc;

import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.B;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.LEFT;
import static javafx.scene.input.KeyCode.RIGHT;
import static javafx.scene.input.KeyCode.UP;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import app.Game;
import app.engine.entity.Player;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Used to track which keys are currently down
 * */
public class Keyboard {
	private static KeyCode[] code = {UP, UP, DOWN, DOWN, LEFT, RIGHT, LEFT, RIGHT, B, A};
	private static Queue<KeyCode> hist = new LinkedList<>();
	
	private static volatile boolean keyboardEnabled = true; 
	private static final HashMap<KeyCode, Boolean> heldKeys = new HashMap<>();
	private Keyboard() {}
	
	public static void onKeyPress(KeyEvent event) {
		if(heldKeys.containsKey(event.getCode()) || !keyboardEnabled) return;
		
		hist.add(event.getCode());
		if(hist.size()>code.length) hist.poll();
		heldKeys.put(event.getCode(), true);
		
		if(event.getCode().equals(Game.instance().getSettings().getPauseKeycode())) {
			Game.instance().getLevelView().onPause();
		}
		
		if(hist.size() != code.length) return;
		Iterator<KeyCode> itter = hist.iterator();
		for(int i = 0; i<code.length; i++) {
			if(itter.hasNext() && !itter.next().equals( code[i] )) {
				return;
			}
		}
		Player.sonic = true;
		Game.instance().getGameHud().staminaBar.progressProperty().unbind();
		Game.instance().getGameHud().staminaBar.setProgress(-1);
		Game.instance().getPausePane().enableShop();
		Game.instance().getGameHud().enableDebugText();
		
		
	}
	public static void onKeyRelease(KeyEvent event) {
		heldKeys.remove(event.getCode());
	}
	
	public static boolean isHeld( KeyCode code ) {
		return heldKeys.getOrDefault(code, false);
	}
	public static boolean isHeld( Keybinding binding ) {
		return isHeld( binding.getKeyCode() );
	}

	public static void disableInput() {
		keyboardEnabled = false;
		releaseAll();
	}
	public static void enableInput() {
		keyboardEnabled = true;
	}

	public static void releaseAll() {
		heldKeys.clear();
		hist.clear();
	}
}
