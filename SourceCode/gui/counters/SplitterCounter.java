/** This class is a sub class of Counter for SplitterCounter
 * @author: Ross, Yuhan Luo
 */
package gui.counters;

import gui.GUI;
import controllers.Direction;

public class SplitterCounter extends Counter {

	public SplitterCounter(GUI gui, int y) {
		super(gui, y);
		direction = Direction.ALL;
		type = GUI.SPLITTER_COUNTER_ID;
	}
	public SplitterCounter(GUI gui, int row, int col, int type, Direction direction){
		super(gui, row, col, type, direction);
	}
	
	protected void displayThis() {
		gui.shape(gui.splitter_counter, x, y, gui.counter_diameter, gui.counter_diameter);
	}
}
