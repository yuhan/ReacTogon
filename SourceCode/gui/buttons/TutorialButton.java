/** This class is a sub class of Buttons - tutorial
 * @author: Yuhan Luo
 */
package gui.buttons;

import gui.GUI;
import controlP5.Button;

// play button
public class TutorialButton extends Buttons {

	public TutorialButton(GUI gui) {
		super(gui);
	}

	public void init() {
		super.initButton("show tutorial", GUI.TUTORIAL_ID);
	}

	public void display(float width, float height) {
		int button_x = (int) ((0.02f + 0.13 + 0.01f) * width);
		int button_y = (int) (0.01f * height);
		int button_width = (int) (0.10f * width);
		int button_height = (int) (0.03f * height);
		
		super.displayButton(button_x, button_y, button_width, button_height);

		button.getCaptionLabel().setFont(gui.createFont("verdana", 10))
		.toUpperCase(true);

		button.getCaptionLabel().getStyle().marginLeft = (int) (0.1f * button_width);
	}
	
	public Button getButton() {
		return button;
	}
}
