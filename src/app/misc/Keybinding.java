package app.misc;

import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;

public class Keybinding extends Button{
	
	KeyCode code = null;

	public Keybinding(KeyCode defKey) {
		super(defKey.toString());
		code = defKey;
		
		
		
		this.setOnKeyPressed(e->{
			code = e.getCode();
			setText(code.toString());
		});
	}
	
	public KeyCode getKeyCode() {
		return code;
	}
}
