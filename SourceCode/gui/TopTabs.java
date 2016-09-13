/** This class is for display top multiple instrument tabs
 * @author: Yuhan Luo
 */
package gui;

import controlP5.RadioButton;

public class TopTabs {
	private int tab_x;
	private int tab_y;
	private int tab_width;
	private int tab_height;
	private int tab_num;
	private RadioButton tab;
	private GUI gui;

	TopTabs(GUI gui, int n) {
		this.gui = gui;

		tab_num = n;
		tab = gui.cp5.addRadioButton("tabs")
				.setColorBackground(gui.color(255, 255, 255, 50))
				.setColorForeground(0xff8994cd).setColorActive(0xffb9c0e9)
				.setItemsPerRow(tab_num).setSpacingColumn(0)
				.setNoneSelectedAllowed(false);
		for (int i = 0; i < GUI.NUMBER_TAB; i++) {
			tab.addItem("tab" + i, i);
		}
		for (int i = 1; i < GUI.NUMBER_TAB; i++) {
			tab.getItem(i).setVisible(false);
		}
	}

	public void display(float width, float height) {
		tab_x = (int) (0.02f * width);
		tab_y = (int) (0.05f * height);
		tab_width = (int) (0.13f * width);
		tab_height = (int) (0.05f * height);
		tab.setPosition(tab_x, tab_y).setSize(tab_width, tab_height);
		for (int i = 0; i < GUI.NUMBER_TAB; i++) {
			tab.getItem(i).getCaptionLabel()
					.setFont(gui.createFont("verdana", 10)).toUpperCase(true);
			tab.getItem(i).getCaptionLabel().getStyle().marginLeft = (int) (-gui.width * 0.13f);
		}
	}

	public RadioButton getButton() {
		return tab;
	}

}