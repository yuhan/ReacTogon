/** This class is a sub class of Buttons - change
 * @author: Yuhan Luo
 */
package gui.buttons;

import gui.GUI;
import controlP5.DropdownList;

// right click change instrument button
public class ChangeButton extends Buttons {

	public ChangeButton(GUI gui) {
		super(gui);
	}

	public void init() {
		super.initDropdownList("change instrument", GUI.CHANGE_ID);
		dropdownList.setOpen(true).setVisible(false);
	}
	
	// show button
	public void show(float x, float y) {
		float width = gui.width;
		float height = gui.height;
		
		int bar_height = (int) (0.03f * height);
		int button_x = (int) (0.02f * width);
		int button_y = (int) (0.01f * height) + bar_height;
		int button_width = (int) (0.17f * width);
		int button_height = (int) (0.03f * height);
		
		super.displayDropdownList(button_x, button_y, button_width, button_height, 5, bar_height);

		dropdownList.getCaptionLabel().getStyle().marginLeft = (int) (0.13f * button_width);
		dropdownList.getCaptionLabel().getStyle().marginTop = (int) (0.25f * button_height);
		
		dropdownList.setPosition(x, y).setOpen(true).setVisible(true);
	}

	// hide button
	public void hide() {
		dropdownList.setVisible(false);
	}

	public DropdownList getButton() {
		return dropdownList;
	}
}
