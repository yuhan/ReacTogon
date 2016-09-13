/** This class is a sub class of Buttons - play
 * @author: Yuhan Luo
 */
package gui.buttons;

import gui.GUI;
import controlP5.Button;

// play button
public class PlayButton extends Buttons {

	public PlayButton(GUI gui) {
		super(gui);
	}

	public void init() {
		super.initButton("play", GUI.PLAY_ID);
	}

	public void display(float width, float height) {
		int button_y = (int) ((0.43f + 0.37f + 0.07f) * height);
		int button_width = (int) (0.225f * width);
		int button_height = (int) (0.09f * height);
		int button_x = (int) (gui.counter_x + gui.counter_diameter + (gui.counter_x - gui.table_bg_x - gui.table_bg_width)  + 0.01f * width);

		super.displayButton(button_x, button_y, button_width, button_height);

		button.getCaptionLabel().setFont(gui.createFont("verdana", 40))
				.toUpperCase(true);
		button.getCaptionLabel().getStyle().marginLeft = (int) (0.3f * button_width);
	}
	
	public Button getButton() {
		return button;
	}
}
