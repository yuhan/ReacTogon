/** This class is a sub class of Buttons - add
 * @author: Yuhan Luo
 */
package gui.buttons;

import gui.GUI;
import controlP5.DropdownList;

// top add instrument button
public class AddButton extends Buttons {

	public AddButton(GUI gui) {
		super(gui);
	}

	public void init() {
		super.initDropdownList("add an instrument", GUI.ADD_ID);
		dropdownList.getCaptionLabel().setFont(gui.createFont("verdana", 10))
		.toUpperCase(true);
	}

	public void display(float width, float height) {

		int bar_height = (int) (0.03f * height);
		int button_x = (int) (0.02f * width);
		int button_y = (int) (0.01f * height) + bar_height;
		int button_width = (int) (0.13f * width);
		int button_height = (int) (0.03f * height);
		
		super.displayDropdownList(button_x, button_y, button_width, button_height, 10, bar_height);

		dropdownList.getCaptionLabel().getStyle().marginLeft = (int) (0.13f * button_width);
		dropdownList.getCaptionLabel().getStyle().marginTop = (int) (0.25f * button_height);
	}
	
	public DropdownList getButton() {
		return dropdownList;
	}
}
