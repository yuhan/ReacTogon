/** This class is for Harmonic table(initializing, displaying and set cell's sound)
 * @author: Yuhan Luo, San Ha
 */
package gui;

import main.HelperFunctions;
import processing.core.PApplet;

public class HarmonicTable {
	static final int TABLE_COL_NUM = 14;
	static final int TABLE_SMALL_ROW_NUM = 7;
	int table_row_num = (TABLE_SMALL_ROW_NUM + 1) * 2;
	Cell[][] cell = new Cell[table_row_num][TABLE_COL_NUM];
	private GUI gui;	


	public HarmonicTable(GUI gui) {
		this.gui = gui;
	}

	// init cell array
	public void initCells() {
		for (int row = 0; row < table_row_num - 1; row++) {
			if (HelperFunctions.isEven(row)) {
				for (int col = 1; col < TABLE_COL_NUM; col += 2) {
					cell[row][col] = new Cell(gui, row, col, gui.color(212, 212, 248, 255));
				}
			} else {
				for (int col = 0; col < TABLE_COL_NUM; col += 2) {
					cell[row][col] = new Cell(gui, row, col, gui.color(255, 255, 255, 255));
				}
			}
		} // each row finishes
		setCell(); // for MusicController
	}

	// display note array
	public void display(float width, float height) {

		float cell_start_down_x = gui.radius;
		float cell_start_down_y = gui.radius * PApplet.sqrt(3);
		float cell_start_up_x = gui.radius * 5 / 2;
		float cell_start_up_y = gui.radius * PApplet.sqrt(3) / 2;
		float cell_x = gui.table_bg_x;
		float cell_y = gui.table_bg_y;
		
		// display table background
        gui.fill(255, 255, 255, 25);
        gui.rect(gui.table_bg_x, gui.table_bg_y, gui.table_bg_width, gui.table_bg_height);
        
        // display cells in harmonic table
		for (int row = 0; row < table_row_num; row++) {
			for (int col = 0; col < TABLE_COL_NUM; col++) {
				if (getCell(row, col) != null) {
					
			    	float x,y,r;
					r = gui.radius;
			    	
			    	if (HelperFunctions.isEven(row)) {
						x = cell_x + cell_start_up_x + (col / 2) * 3 * gui.radius;
						y = cell_y + cell_start_up_y + row / 2 * gui.note_height;
					}else {
						x = cell_x + cell_start_down_x + (col / 2) * 3 * gui.radius;
						y = cell_y + cell_start_down_y + row / 2 * gui.note_height;
					}
				
					getCell(row, col).display(x,y,r);
				}
			}
		}
	}
	
	// set cell's sound
	public void setCell() {
		int startingIndexEvenRow = 84;
		int startingIndexOddRow = 80;
		int index = 0;
		for (int i = 0; i < cell.length - 1; i++) {
			if (i % 2 == 0) {
				index = startingIndexEvenRow;
				startingIndexEvenRow -= 7;
			} else {
				index = startingIndexOddRow;
				startingIndexOddRow -= 7;
			}
			for (int j = 0; j < cell[0].length; j++) {
				if (i % 2 != j % 2) {
					cell[i][j].setCell(index);
					index++;
				}
			}
		}
	}

	// getters
	public Cell[][] getCellArray() {
		return cell;
	}

	public Cell getCell(int row, int col) {
		return cell[row][col];
	}
}
