/** This class is a sub class of Counter for RicochetCounter
 * @author: Ross, Yuhan Luo
 */
package gui.counters;

import gui.GUI;
import controllers.Direction;

public class RicochetCounter extends Counter {

	public RicochetCounter(GUI gui, int y) {
		super(gui, y);
		direction = Direction.N;
		type = GUI.RICOCHET_COUNTER_ID;
	}
	
	public RicochetCounter(GUI gui, int row, int col, int type, Direction direction){
		super(gui, row, col, type, direction);
	}
	
	protected void displayThis() {
		displayRotation();
		gui.shape(gui.ricochet_counter, x, y, gui.counter_diameter, gui.counter_diameter);
		gui.resetMatrix();
	}

}
