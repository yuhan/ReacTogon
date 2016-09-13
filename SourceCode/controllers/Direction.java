/* This enum class defines new Direction type associated with coordinate to determine cell's neighbors
 * @author: Vu San Ha Huynh, Ross Alan Michael Putman
 */
package controllers;


public enum Direction {
	// x, y.   These values are used to move to different cells. eg. N moves [-2][0] in the array to move 'up' one cell.
	N (-2, 0), 
	NE (-1, 1), 
	SE (1, 1), 
	S (2, 0), 
	SW (1, -1), 
	NW (-1, -1),
	ALL (0, 0),
	END (999,999),
	NULL (0, 0);
	
	private int changeInXCoord;
	private int changeInYCoord;
	
	private static Direction[] mainValues = {N,NE,SE,S,SW,NW};	// Sorry for hard coding this: couldn't find a way to do it with arrayCopy
	
	private Direction(int x, int y) {
		changeInXCoord = x;
		changeInYCoord = y;
	}
	
	public int getChangeInXCoord() {
		return changeInXCoord;
	}
	
	public int getChangeInYCoord() {
		return changeInYCoord;
	}
	
	/** Use this instead of <code>values();</code>
	 * @return values(), but without special Directions. (such as NULL)
	 */
	public static Direction[] mainValues() {
		return mainValues;
	}
    
	public Direction next() {
		return mainValues[ (this.ordinal()+1) % mainValues.length];
		// modulo used to avoid returning ALL, END and NULL
	}
	

}
