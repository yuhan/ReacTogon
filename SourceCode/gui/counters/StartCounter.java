/** This class is a sub class of Counter for StartCounter
 * @author: Ross, Yuhan Luo
 */
package gui.counters;

import gui.GUI;
import controllers.Direction;

public class StartCounter extends Counter {
	
	public StartCounter(GUI gui, int y) {
		super(gui, y);
		direction = Direction.N;
		type = GUI.START_COUNTER_ID;
	}
	
	public StartCounter(GUI gui, int row, int col, int type, Direction direction){
		super(gui, row, col, type, direction);
	}
	
	protected void displayThis() {
		displayRotation();
		gui.shape(gui.start_counter, x, y, gui.counter_diameter, gui.counter_diameter);
		gui.resetMatrix();
	}
}
