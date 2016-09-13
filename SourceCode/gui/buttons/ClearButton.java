/** This class is a sub class of Buttons - clear
 * @author: Yuhan Luo
 */
package gui.buttons;

import gui.GUI;
import controlP5.Button;

// counter clear button
public class ClearButton extends Buttons {

	public ClearButton(GUI gui) {
		super(gui);
	}

	public void init() {
		super.initButton("clear", GUI.CLEAR_ID);
		button.setColorForeground(0xffec908b)
        	.setColorActive(0xffCA6963);
	}
	
	public void display(float width, float height) {
		int button_x = (int)gui.counter_x;
		int button_y = gui.counter_y + (gui.counter_diameter + gui.counter_gap) * 4
				+ 10;
		int button_width = gui.counter_diameter;
		int button_height = (int) (gui.counter_diameter * 0.5f);
		
		super.displayButton(button_x, button_y, button_width, button_height);
		button.getCaptionLabel().setFont(gui.createFont("verdana", 13))
				.toUpperCase(true);
	}
	
	public Button getButton() {
		return button;
	}
}
