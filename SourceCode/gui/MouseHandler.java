/** This class is a handler dealing with mouse events
 * @author: Yuhan Luo
 */
package gui;

import gui.counters.Counter;
import gui.counters.RicochetCounter;
import gui.counters.SplitterCounter;
import gui.counters.StartCounter;
import gui.counters.StopCounter;

import java.util.Map;

import main.HelperFunctions;
import processing.core.PApplet;

public class MouseHandler {
	private static MouseHandler instance;
	private GUI gui;
	private static final int PRESS_ONE_CASE = 0;
	private static final int DRAG_MOUSE_CASE = 1;

	private int counter_x;
	private int counterDiameter;
	private int counter_gap;
	private int counter_y;
	private int COUNTER_RIGHT;
	private int START_COUNTER_Y_TOP;
	private int START_COUNTER_Y_BOTTOM;
	private int RICOCHET_COUNTER_Y_TOP;
	private int RICOCHET_COUNTER_Y_BOTTOM;
	private int SPLITTER_COUNTER_Y_TOP;
	private int SPLITTER_COUNTER_Y_BOTTOM;
	private int STOP_COUNTER_Y_TOP;
	private int STOP_COUNTER_Y_BOTTOM;

	private boolean isDraggingCounter;
	private int prevClickRow;
	private int prevClickCol;

	private MouseHandler(GUI gui) {
		this.gui = gui;
		updateVariable();
		isDraggingCounter = false;
		prevClickRow = -1;
		prevClickCol = -1;
	}

	public static MouseHandler getInstance(GUI gui) {
		if (instance == null) {
			instance = new MouseHandler(gui);
		}
		return instance;
	}
	
	// update variables all the time
	public void updateVariable() {
		counter_x = gui.counter_x;
		counter_y = gui.counter_y;
		counterDiameter = gui.counter_diameter;
		counter_gap = gui.counter_gap;
		COUNTER_RIGHT = counter_x + counterDiameter;
		START_COUNTER_Y_TOP = counter_y + (counterDiameter + counter_gap) * GUI.START_COUNTER_ID;
		START_COUNTER_Y_BOTTOM = counter_y + (counterDiameter + counter_gap) * GUI.START_COUNTER_ID + counterDiameter;
		RICOCHET_COUNTER_Y_TOP = counter_y + (counterDiameter + counter_gap) * GUI.RICOCHET_COUNTER_ID;
		RICOCHET_COUNTER_Y_BOTTOM = counter_y + (counterDiameter + counter_gap) * GUI.RICOCHET_COUNTER_ID + counterDiameter;
		SPLITTER_COUNTER_Y_TOP = counter_y + (counterDiameter + counter_gap) * GUI.SPLITTER_COUNTER_ID;
		SPLITTER_COUNTER_Y_BOTTOM = counter_y + (counterDiameter + counter_gap) * GUI.SPLITTER_COUNTER_ID + counterDiameter;
		STOP_COUNTER_Y_TOP = counter_y + (counterDiameter + counter_gap) * GUI.STOP_COUNTER_ID;
		STOP_COUNTER_Y_BOTTOM = counter_y + (counterDiameter + counter_gap) * GUI.STOP_COUNTER_ID + counterDiameter;
	}
	
	// mouse press
	public Counter pressed() {
		// control dropdown list
		if (!gui.addButton.getButton().isMouseOver()) {
			gui.addButton.getButton().close();
		}
		
		if (isInitChange()){
		} else if (!gui.changeButton.getButton().isMouseOver()) {
			gui.changeButton.hide();
		} 
		
		Counter tempCounter = gui.tempCounter;

		int mouseX = gui.mouseX;
		int mouseY = gui.mouseY;

		// check which counter the mouse is over
		if (isMouseOverCounter(GUI.START_COUNTER_ID, mouseX, mouseY)) {
			tempCounter = new StartCounter(this.gui, START_COUNTER_Y_TOP);
		} else if (isMouseOverCounter(GUI.RICOCHET_COUNTER_ID, mouseX, mouseY)) {
			tempCounter = new RicochetCounter(this.gui, RICOCHET_COUNTER_Y_TOP);
		} else if (isMouseOverCounter(GUI.SPLITTER_COUNTER_ID, mouseX, mouseY)) {
			tempCounter = new SplitterCounter(this.gui, SPLITTER_COUNTER_Y_TOP);
		} else if (isMouseOverCounter(GUI.STOP_COUNTER_ID, mouseX, mouseY)) {
			tempCounter = new StopCounter(this.gui, STOP_COUNTER_Y_TOP);
		}

		// if any counter is moved
		if (tempCounter != null) {
			tempCounter.clicked(mouseX, mouseY);
			isDraggingCounter = true;
		}
		
		for (Map.Entry<Cell, Counter> nc : gui.layers.get(gui.getCurrentTab()).getNoteCounter().entrySet()) {
			Counter counter = (Counter) nc.getValue();
			Cell cell = (Cell) nc.getKey();
			counter.clicked(mouseX, mouseY);
			
			// if a current counter is moved
			if (counter.isMoved()) {
				isDraggingCounter = true;

				tempCounter = counter;
				// ##########################
				cell.removeCounter();
				// #########################
				gui.layers.get(gui.getCurrentTab()).getNoteCounter().remove(cell);
			}
		}
		if (!isDraggingCounter){
			makeNoteSound(true, 0);			
		}

		return tempCounter;
	}

	// mouse drag
	public void dragged() {
		if (!isDraggingCounter) {
			makeNoteSound(true, 1);
		}
	}

	// mouse release
	public Counter released() {
		Counter tempCounter = gui.tempCounter;

		if (tempCounter != null) {
			isDraggingCounter = false;
			tempCounter.stopDragging();
			int tempCol = tempCounter.getCol();
			int tempRow = tempCounter.getRow();
			
			// ensure the counter not be placed outside the table
			if (!isInsideTable(tempRow, tempCol)) {
				tempCounter = null;

			} else if (cellExists(tempCol, tempRow)) {
			// avoid overlap
					// println("overlap" + tempCol + "," + tempRow);
					tempCounter = null;
			} else if (!gui.layers.get(gui.getCurrentTab()).getNoteCounter().containsValue(tempCounter)) {
			// if counter is new created
				gui.layers.get(gui.getCurrentTab()).getNoteCounter()
				.put(gui.layers.get(gui.getCurrentTab()).getHarmonicTable().getCell(tempRow, tempCol), tempCounter);
	
			// clear the temp counter after adding it into the hashmap
					tempCounter = null;
			}
			
			// if the tempCounter is still not null
			for (Map.Entry<Cell, Counter> nc : gui.layers.get(gui.getCurrentTab()).getNoteCounter().entrySet()) {
				Counter c = (Counter) nc.getValue();
//				Cell n = (Cell) nc.getKey();
				c.stopDragging();

				// move the current counter
				if (tempCounter != null && tempCounter == c) {
					gui.layers.get(gui.getCurrentTab()).getNoteCounter()
					.put(gui.layers.get(gui.getCurrentTab()).getHarmonicTable().getCell(tempRow, tempCol), tempCounter);
					// println(n.getCol() + "," + n.getRow());
					tempCounter = null;
				}
			}
		}
		makeNoteSound(false, 0);

		return tempCounter;
	}

	// ########### When click on one note, sound make ############## //
	private void makeNoteSound(boolean isPressed, int n) {
		
		// if users click on the drop down list, no sound
		if (gui.addButton.getButton().isOpen() || gui.changeButton.getButton().isVisible()){
			return;
		}
		
		float myX = gui.mouseX - gui.table_bg_x; // use mouse coordinates rather than Counter's x,y
		float myY = gui.mouseY - gui.table_bg_y;

		int clickedRow = 0;
		int clickedCol = (int) (myX / (3 * gui.radius / 2));

		if (HelperFunctions.isEven(clickedCol)) { // if col is even number, the row contains 7 notes; otherwise, 9 notes.
			clickedRow = (int) ((myY - PApplet.sqrt(3) * gui.radius / 2) / gui.note_height) * 2 + 1;
		} else {
			clickedRow = (int) (myY / gui.note_height) * 2;
		}

		switch (n) {
		case PRESS_ONE_CASE: // press once case
			if (isInsideTable(clickedRow, clickedCol)) {
				gui.audioController.getAudioPlayerCurrentTab(gui.getCurrentTab()).stream(
						gui.layers.get(gui.getCurrentTab()).getHarmonicTable().getCell(clickedRow, clickedCol).getCell(), isPressed);
				// println("0: "+ clickedRow + "," + clickedCol);
				prevClickRow = clickedRow;
				prevClickCol = clickedCol;
			}
			break;
		case DRAG_MOUSE_CASE: // drag mouse case
			if (isInsideTable(clickedRow, clickedCol) && !(clickedRow == prevClickRow && clickedCol == prevClickCol)) {
				gui.audioController.getAudioPlayerCurrentTab(gui.getCurrentTab()).stream(
						gui.layers.get(gui.getCurrentTab()).getHarmonicTable().getCell(clickedRow, clickedCol).getCell(), isPressed);
				gui.audioController.getAudioPlayerCurrentTab(gui.getCurrentTab()).stream(
						gui.layers.get(gui.getCurrentTab()).getHarmonicTable().getCell(prevClickRow, prevClickCol).getCell(), !isPressed);
				// println("1: "+ clickedRow + "," + clickedCol);
				prevClickRow = clickedRow;
				prevClickCol = clickedCol;
			}
			break;
		default:
			break;
		}
	}

	// helper function
	private boolean isMouseOverCounter(int n, int mouseX, int mouseY) {

		switch (n) {
		case GUI.START_COUNTER_ID:
			return (mouseX > counter_x && mouseX < COUNTER_RIGHT && mouseY > START_COUNTER_Y_TOP && mouseY < START_COUNTER_Y_BOTTOM);
		case GUI.RICOCHET_COUNTER_ID:
			return (mouseX > counter_x && mouseX < COUNTER_RIGHT && mouseY > RICOCHET_COUNTER_Y_TOP && mouseY < RICOCHET_COUNTER_Y_BOTTOM);
		case GUI.SPLITTER_COUNTER_ID:
			return (mouseX > counter_x && mouseX < COUNTER_RIGHT && mouseY > SPLITTER_COUNTER_Y_TOP && mouseY < SPLITTER_COUNTER_Y_BOTTOM);
		case GUI.STOP_COUNTER_ID:
			return (mouseX > counter_x && mouseX < COUNTER_RIGHT && mouseY > STOP_COUNTER_Y_TOP && mouseY < STOP_COUNTER_Y_BOTTOM);
		default:
			return false;
		}

	}

	// check if the counter is inside the table, using col and row attributes
	private boolean isInsideTable(int tempRow, int tempCol) {
		return tempCol >= 0
			&& tempCol < gui.table_col_num
			&& (tempRow >= 0 
				&& (HelperFunctions.isEven(tempCol) && tempRow <= gui.table_small_row_num * 2 -1) 
				|| (!HelperFunctions.isEven(tempCol) && tempRow <= gui.table_small_row_num * 2)) 
			&& gui.mouseX >= gui.table_bg_x && gui.mouseX <= gui.table_bg_x_right
			&& gui.mouseY >= gui.table_bg_y && gui.mouseY <= gui.table_bg_y_bottom;
	}

	// check if a cell position already exists in the hashmap
	private boolean cellExists(int tCol, int tRow) {
		boolean cellExists = false;
		Cell cell = null;
		for (Map.Entry<Cell, Counter> nc : gui.layers.get(gui.getCurrentTab()).getNoteCounter().entrySet()) {
			cell = (Cell) nc.getKey();
			if (cell.getCol() == tCol && cell.getRow() == tRow) {
				cellExists = true;
				break;
			}
		}

		return cellExists;
	}

	// check if is init change list
	private boolean isInitChange(){
		int x0 = (int) gui.changeButton.getButton().getPosition().x;
		int y0 = (int) gui.changeButton.getButton().getPosition().y;
		return gui.mouseX == x0 && gui.mouseY == y0;
	}
	
}