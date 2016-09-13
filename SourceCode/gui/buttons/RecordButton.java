/** This class is a sub class of Buttons - record
 * @author: Yuhan Luo
 */
package gui.buttons;

import gui.GUI;
import controlP5.Button;

// record MIDI button
public class RecordButton extends Buttons {

	public RecordButton(GUI gui) {
		super(gui);
	}

	public void init() {
		super.initButton("record music", GUI.RECORD_ID);
		button.
		setColorBackground(gui.color(202, 105, 99, 200))
				.setColorForeground(gui.color(202, 105, 99, 230))
				.setColorActive(gui.color(202, 105, 99, 255))
				.setLabelVisible(false);
	}

	public void display(float width, float height) {
		int button_x = (int) (gui.counter_x + 0.2f * gui.counter_diameter);
		int button_y = (int) (0.02f * height);
		int button_width = (int) (0.6f * gui.counter_diameter);
		int button_height = (int) (0.6f * gui.counter_diameter);

		super.displayButton(button_x, button_y, button_width, button_height);

	}

	public Button getButton() {
		return button;
	}
}