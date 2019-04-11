package app.misc;

import java.util.HashMap;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Used to track which keys are currently down
 * */
public class Keyboard {
	
	private static final HashMap<KeyCode, Boolean> heldKeys = new HashMap<>();
	private Keyboard() {}
	
	public static void onKeyPress(KeyEvent event) {
		System.out.println("Key down: "+event.getCharacter());
		heldKeys.put(event.getCode(), true);
	}
	public static void onKeyRelease(KeyEvent event) {
		System.out.println("Key up: "+event.getCharacter());
		heldKeys.remove(event.getCode());
	}
	
	public static boolean isHeld( KeyCode code ) {
		return heldKeys.getOrDefault(code, false);
	}
	public static boolean isHeld( Keybinding binding ) {
		return isHeld( binding.getKeyCode() );
	}
}
