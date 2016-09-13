/** This class is for initializing counters, which cannot be dragged
 * @author: Ross Alan Michael Putman
 */
package gui;

//the counters displayed on the side of the screen that the user creates new counters from, not draggable
public class InitCounter {

    private GUI gui;
    
	InitCounter(GUI gui) {
    	this.gui = gui;
    }

    public void display() {
        // four types of counters
        gui.shape(gui.start_counter, gui.counter_x, gui.counter_y, gui.counter_diameter, gui.counter_diameter);
        gui.shape(gui.ricochet_counter, gui.counter_x, gui.counter_y + gui.counter_diameter + gui.counter_gap, gui.counter_diameter, gui.counter_diameter);
        gui.shape(gui.splitter_counter, gui.counter_x, gui.counter_y + (gui.counter_diameter + gui.counter_gap) * 2, gui.counter_diameter, gui.counter_diameter);
        gui.shape(gui.stop_counter, gui.counter_x, gui.counter_y + (gui.counter_diameter + gui.counter_gap) * 3, gui.counter_diameter, gui.counter_diameter);
    }
}

