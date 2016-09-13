/** This class is draggable counters: display rotation, click, rollover and drag
 * @author: Yuhan Luo
 */
package gui.counters;

import gui.GUI;
import main.HelperFunctions;
import controllers.Direction;
import processing.core.PConstants;

public abstract class Counter {

	private boolean dragging = false; // Is the object being dragged?
	private boolean rollover = false; // Is the mouse over the ellipse?
	private boolean stopped = false;
	
	protected int fixedRowNum;	
	protected int fixedColNum;
	private float offsetX, offsetY; // Mouseclick offset
	private float relaX, relaY;
	
	protected float x;
	protected float y; // Location and size
	protected GUI gui;
	protected int type;
	protected Direction direction;

	// constructor for undraggable counters
	protected Counter(GUI gui, int y) {
		this.gui = gui;		
		this.x = gui.counter_x;
		this.y = y;
		this.offsetX = 0;
		this.offsetY = 0;
		this.fixedRowNum = -1;
		this.fixedColNum = -1;
	}
	
	// constructor for draggable counters
	public Counter(GUI gui, int row, int col, int type, Direction direction){
		this.gui = gui;		
		this.offsetX = 0;
		this.offsetY = 0;
		this.fixedColNum = col;
		this.fixedRowNum = row;
		this.x = colToX(fixedColNum);
		this.y = rowToY(fixedRowNum);
		this.type = type;
		this.direction = direction;
		this.stopped = true;
	}
	
	// Method to display
	public void display() {
		
		if (dragging) {
			gui.fill(gui.color(235, 193, 78, 220));
		} else if (rollover) {
			gui.fill(gui.color(235, 193, 78, 220));
		} else {	
			// if the counter is not moving,
			// its position would be changed according to the window size
			gui.fill(gui.color(235, 193, 78));
			this.x = colToX(fixedColNum);
			this.y = rowToY(fixedRowNum);
		}
		gui.stroke(255);
		gui.ellipse(x + gui.counter_diameter / 2, y + gui.counter_diameter / 2, gui.counter_diameter, gui.counter_diameter);

		displayThis();
	}
	
	protected abstract void displayThis();
	
	public void displayRotation() {
		gui.translate(x + gui.counter_diameter / 2, y + gui.counter_diameter / 2);
		// ordinal() is Direction represented as an int.
		gui.rotate(PConstants.PI / 3 * direction.ordinal());
		gui.translate(-(x + gui.counter_diameter / 2), -(y + gui.counter_diameter / 2));
	}

	// Is a point inside the rectangle (for click)
	public void clicked(int mouseX, int mouseY) {
		if (mouseX > x && mouseX < x + gui.counter_diameter && mouseY > y && mouseY < y + gui.counter_diameter) {
			dragging = true;
			// If so, keep track of relative location of click to corner of rectangle
			offsetX = x - mouseX;
			offsetY = y - mouseY;
			if (gui.mouseButton == PConstants.RIGHT) {
				direction = direction.next();
			}
			if (offsetX != 0 || offsetY != 0) { // if moved, change 'stopped' recorder to false
				stopped = false;
			}
		}
	}

	// Is a point inside the rectangle (for rollover)
	public void rollover(int mouseX, int mouseY) {
		if (mouseX > x && mouseX < x + gui.counter_diameter && mouseY > y && mouseY < y + gui.counter_diameter) {
			rollover = true;
		} else {
			rollover = false;
		}
	}

	// Stop dragging
	public void stopDragging() {
		dragging = false;
		// use mouse coordinates rather than Counter's x,y
		relaX = gui.mouseX - gui.table_bg_x; 
		relaY = gui.mouseY - gui.table_bg_y;
		
		if (!stopped) {
			fixedColNum = (int) (relaX / (3 * gui.radius / 2));

			// if col is even number, the row contains 7 notes; otherwise, 9 notes.
			if (HelperFunctions.isEven(fixedColNum)) {
				fixedRowNum = (int)((relaY - (gui.note_height/2) ) / gui.note_height)  * 2 + 1;
			} else {
				fixedRowNum = (int) (relaY / gui.note_height) * 2;
			}
			x = colToX(fixedColNum);
			y = rowToY(fixedRowNum);
			
			stopped = true; // stop drag = true, which avoids the fixed counters move
		}
	}
	
	/** helper functions **/
	// convert fixed column number to position x
	private float colToX(int col){
		return gui.table_bg_x + gui.counter_fix_x + col * (3 * gui.radius / 2);
	}
	// convert fixed row number to position y
	private float rowToY(int row){
		return gui.table_bg_y + row * gui.note_height / 2;
	}
	
	// Drag the rectangle
	public void drag(int mouseX, int mouseY) {
		if (dragging) {
			x = mouseX + offsetX;
			y = mouseY + offsetY;
		}
	}

	/** getter and setter **/
	public Direction getDirection() {
		return direction;
	}

	public int getType() {
		return this.type;
	}

	public int getCol() {
		return fixedColNum;
	}

	public int getRow() {
		return fixedRowNum;
	}

//	public void setCol(int col) {
//		this.fixedColNum = col;
//	}
//	
//	public void setRow(int row) {
//		this.fixedRowNum = row;
//	}
	
	public void setType(int type) {
		this.type = type;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public boolean isMoved() {
		return !stopped;
	}
}
