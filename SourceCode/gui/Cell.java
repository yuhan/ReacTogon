/** This class is for Cell, the hexagon in the harmonic table
 * @author: San Ha, Yuhan Luo
 */
package gui;

import gui.counters.Counter;

import java.util.HashSet;
import controllers.Direction;
import processing.core.*;

public class Cell {

    PApplet parent;
  
    static final int TYPE_EMPTY = -1;
    
    int col, row;   // Position 
    HashSet<Direction> direction = new HashSet<Direction>(); 
    int cell; 
    int type = -1; 			// Type, default value = -1, means normal cell.
    boolean hasCounter = false; 
    int c;					// Is c colour? c is not a good name.

    public Cell(PApplet p, int row, int col, int c) {
        this.parent = p;
        this.row = row;
        this.col = col;
        this.c = c;
    }

    // Method to display
    public void display(float x, float y, float r) {    	
    	
        float angle = 360.0f / 6;

        parent.beginShape();
        for (int i = 0; i < 6; i++) {
            parent.vertex(x + r * PApplet.cos(PApplet.radians(angle * i)),
                    y + r * PApplet.sin(PApplet.radians(angle * i)));
            if (type != -1) {
                parent.fill(parent.color(255, 255, 255, 0));
            } else {
                parent.fill(c);
            }
        }
        parent.endShape(PConstants.CLOSE);
    }

    // setters and getters
    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void setDirection(Direction direction) {
    	this.direction.add(direction);
    }

    public HashSet<Direction> getDirection() {
        return direction;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public int getCell() {
        return cell;
    }

    public void setType(int type) {
        /* start counter = 0
         * ricochet counter = 1
         * spliter counter = 2
         * stop counter = 3
         * normal cell = -1
         * temporary active cell = -2
         */
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setHasCounter(boolean state) {
        this.hasCounter = state;
    }

    public boolean hasCounter() {
        return hasCounter;
    }

    public void attachCounter(Counter counter) {
    	setType(counter.getType());
        setDirection(counter.getDirection());
        setHasCounter(true);
    }
    
	public void removeCounter() {
		setType(TYPE_EMPTY);
        getDirection().clear();
        hasCounter = false;
	}
}
