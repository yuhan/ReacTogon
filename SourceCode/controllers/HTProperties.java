/* This class is the object to temporarily store information of the workspace in order to recover it
 * @author: Vu San Ha Huynh, Yuhan Luo
 */
package controllers;

import java.util.ArrayList;

public class HTProperties {

    private int numberOfLayers;
    private ArrayList<ParseCell> cellCounterList[];
    private float mainValue;
    private int insID[];
    private float insVolume[];
    private int timbre[];
    private float tempo[];
    private int interval[];
    private int transposer[];

    public HTProperties(int numberOfLayers, ArrayList<ParseCell> cellCounterList[], float mainValue, int insID[], float insVolume[], int timbre[],
            float tempo[], int interval[], int transposer[]) {
        this.numberOfLayers = numberOfLayers;
        this.cellCounterList = cellCounterList;
        this.mainValue = mainValue;
        this.insID = insID;
        this.insVolume = insVolume;
        this.timbre = timbre;
        this.tempo = tempo;
        this.interval = interval;
        this.transposer = transposer;
    }

    public int getNumberOfLayers() {
        return numberOfLayers;
    }

    public ArrayList<ParseCell>[] getCellCounterList() {
        return cellCounterList;
    }

    public float getMainValue() {
        return mainValue;
    }

    public int[] getInsID() {
        return insID;
    }

    public float[] getInsVolume() {
        return insVolume;
    }

    public int[] getTimbre() {
        return timbre;
    }

    public float[] getTempo() {
        return tempo;
    }

    public int[] getInterval() {
        return interval;
    }

    public int[] getTransposer() {
        return transposer;
    }
}
