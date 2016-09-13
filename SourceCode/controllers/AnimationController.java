/* This class is the most important class handling the animation of music events
 * and send note to AutomatedAudioPlayer in order to make a sound.
 * @author: Vu San Ha Huynh, Ross Alan Michael Putman
 */
package controllers;

import gui.Cell;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AnimationController implements Runnable {

    Cell[][] cells;
    AutomatedAudioPlayer audioPlayer;
    boolean isRunning = false;
    private int speedOfAnimation = 140;
    private int timeToNextIteration = 0;
    private int longestTimeLimitation = 1000;
    int rowStartCounter;
    int colStartCounter;
    int deadEnd;

    public AnimationController(Cell[][] cell, StreamingPlayer audioPlayer) {
        this.cells = cell;
        this.audioPlayer = (AutomatedAudioPlayer) audioPlayer;
    }

    @Override
    public void run() {
        isRunning = true;
        deadEnd = 0;
        List<Cell> activatedCells = new ArrayList<>();
        initializeFromStartCounter(activatedCells);     
        AnimationExecution(activatedCells);
    }
    
    private void initializeFromStartCounter(List<Cell> activatedCells){
        for (int x = 0; x < cells.length - 1; x++) {
            for (int y = 0; y < cells[0].length; y++) {	// TODO: x % 2 != y % 2 can be optimised out. Could be worth it given how often this runs.
                if (x % 2 != y % 2 && cells[x][y].getType() == 0) { // x % 2 != y % 2 is the condition for a cell existing.
                    rowStartCounter = x;
                    colStartCounter = y;
                    activatedCells.add(cells[x][y]);
                    audioPlayer.playHarmonicTable(cells[x][y].getCell());
                }
            }
        }
        sleep(speedOfAnimation);

    }

    private void AnimationExecution(List<Cell> activatedCells) {
        int row, col;
        HashSet<Cell> nextActivatedCells = new HashSet<>();
        while (isRunning) {
            while (!activatedCells.isEmpty()) {
                audioPlayer.incrementClock();
                audioPlayer.incrementTime(speedOfAnimation);
                System.out.println("Current flows: " + activatedCells.size());
                for (Cell cell : activatedCells) {

                    row = cell.getRow();
                    col = cell.getCol();

                    for (Direction direction : cell.getDirection()) {
                        if (direction == Direction.ALL) {
                            splitterCounterHandler(row, col, nextActivatedCells);
                        } else {
                            move(row, col, direction, nextActivatedCells);
                        }
                    }
                }
               
                deactivateCells(activatedCells);
                activatedCells = new ArrayList<Cell>(nextActivatedCells);
                nextActivatedCells.clear();
            }

            if (isRunning && timeToNextIteration <= longestTimeLimitation) {
                sleep(timeToNextIteration);
                initializeFromStartCounter(activatedCells);
            }
        }
        System.out.println("Good bye");
    }

    private void move(int row, int col, Direction direction, HashSet<Cell> nextActivatedCells) {
        int x = row + direction.getChangeInXCoord() + deadEnd;
        int y = col + direction.getChangeInYCoord() + deadEnd;
        if (cellExists(x, y)) {
            changeCell(cells[x][y], direction);
            nextActivatedCells.add(cells[x][y]);
        }
    }

    private boolean cellExists(int x, int y) {
        try {
            Cell cell = cells[x][y];	// throws out of bounds exception
            cell.toString();			// throws null pointer exception
            return true;
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            return false;
        }
    }

    private void deactivateCells(List<Cell> activatedCells) {
        for (Cell cell : activatedCells) {
            if (!cell.hasCounter()) { // Is note is temporary note or held by counter
                cell.setType(-1);
                cell.getDirection().clear();
            }  else if (cell.getType() == 2) { // spliterCounter
                cell.getDirection().clear();
                cell.setDirection(Direction.ALL);
            }
                
        }

        sleep(speedOfAnimation);
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public void setTimeToNextIteration(int time) {
        this.timeToNextIteration = time;
    }

    private void changeCell(Cell cell, Direction direction) {
        if (cell.getDirection().isEmpty() || cell.getType() == -2) {
            cell.setType(-2); // type = -2 : temporary active cell
            cell.setDirection(direction); //
        } else {
            if (cell.getType() == 2) {
                cell.setDirection(direction);
            }
            audioPlayer.playHarmonicTable(cell.getCell());
            audioPlayer.resetTime();
        }

    }

    private void splitterCounterHandler(int row, int col, HashSet<Cell> nextActivatedCells) {
        Direction values[] = new Direction[]{Direction.N, Direction.NE, Direction.SE, Direction.S, Direction.SW, Direction.NW};
        for (int i = 0; i < values.length; i++) {
            if (!cells[row][col].getDirection().contains(i + 3 < 6 ? values[i+3] : values[(i + 3) % 3])) {
                move(row, col, values[i], nextActivatedCells);
            }
        }
    }
    
    public void setAnimationSpeed(int tempo) {
        this.speedOfAnimation = tempo;
    }
    
    public void setInterval(int interval) {
        this.timeToNextIteration = interval;
    }
    public void stopAnimation() {
        isRunning = false; 
        deadEnd = 999;
        audioPlayer.resetClock();
    }
}
