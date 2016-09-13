/** This class is a sub class of Buttons - open
 * @author: Yuhan Luo
 */
package gui.buttons;

import gui.GUI;
import controlP5.Button;

// open file button
public class OpenButton extends Buttons {

	public OpenButton(GUI gui) {
		super(gui);
	}

	public void init() {
		super.initButton("open file", GUI.OPEN_ID);
	}

	public void display(float width, float height) {
		int button_x = (int) (gui.counter_x + gui.counter_diameter + (gui.counter_x - gui.table_bg_x - gui.table_bg_width));
		int button_y = (int) (0.01f * height);
		int button_width = (int) (0.115f * width);
		int button_height = (int) (0.03f * height);
		
		super.displayButton(button_x, button_y, button_width, button_height);

		button.getCaptionLabel().setFont(gui.createFont("verdana", 10))
		.toUpperCase(true);

		button.getCaptionLabel().getStyle().marginLeft = (int) (0.3f * button_width);
	}
	
	public Button getButton() {
		return button;
	}
}
