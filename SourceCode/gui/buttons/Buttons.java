/** This class is a super class, initializing and displaying buttons
 * @author: Yuhan Luo
 */
package gui.buttons;

import gui.GUI;
import gui.Instrument;
import controlP5.Button;
import controlP5.DropdownList;

public class Buttons {
	
	protected Button button;
	protected DropdownList dropdownList;

	protected GUI gui;

	public Buttons(GUI gui) {
		this.gui = gui;
	}

	public void initButton(String name, int id) {
		button = gui.cp5.addButton(name).setValue(0)
				.setColorBackground(gui.color(255, 255, 255, 50))
				.setColorForeground(0xffdab950)
				.setColorActive(0xfff2ce5a)
				.setId(id);
	}

	public void displayButton(int x, int y, int width, int height) {
		button.setPosition(x, y)
			  .setSize(width, height);
	}

	public void initDropdownList(String name, int id) {
		dropdownList = gui.cp5.addDropdownList(name)
				.setColorBackground(gui.color(77, 72, 91, 200))
				.setColorForeground(0xff8994cd).setColorActive(0xffb9c0e9)
				.setScrollbarVisible(true).setId(id).setOpen(false);
		dropdownList.addItems(Instrument.INSTRUMENT_NAMES);
		dropdownList.getCaptionLabel().setFont(gui.createFont("verdana", 10))
				.toUpperCase(true);
	}

	public void displayDropdownList(int x, int y, int width, int height,
			int shown_num, int bar_height) {
		dropdownList.setPosition(x, y)
					.setSize(width, bar_height * shown_num)
					.setItemHeight(bar_height)
					.setBarHeight(bar_height);
	}
}
