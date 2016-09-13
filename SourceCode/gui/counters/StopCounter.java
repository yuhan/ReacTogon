/** This class is a sub class of Counter for StopCounter
 * @author: Ross, Yuhan Luo
 */
package gui.counters;

import gui.GUI;
import controllers.Direction;

public class StopCounter extends Counter {
	
	public StopCounter(GUI gui, int y) {
		super(gui, y);
		direction = Direction.END;
		type = GUI.STOP_COUNTER_ID;
	}

	public StopCounter(GUI gui, int row, int col, int type, Direction direction){
		super(gui, row, col, type, direction);
	}
	
	protected void displayThis() {
		gui.shape(gui.stop_counter, x, y, gui.counter_diameter, gui.counter_diameter);
	}
}
