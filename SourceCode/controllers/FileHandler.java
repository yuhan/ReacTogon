/* This class handles the input and output functionalities
 * External library: JSON
 * @author: Vu San Ha Huynh, Yuhan Luo
 */
package controllers;

import gui.Cell;
import gui.GUI;
import gui.Instrument;
import gui.Layer;
import gui.PerfControl;
import gui.TopTabs;
import gui.counters.Counter;
import gui.counters.RicochetCounter;
import gui.counters.SplitterCounter;
import gui.counters.StartCounter;
import gui.counters.StopCounter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileHandler{
    GUI gui;
    ArrayList<Layer> layers;
    PerfControl perfControl;
    Instrument instrument;
    TopTabs topTabs;
    ArrayList<AnimationController> animationController;
    AudioController audioController;

    public FileHandler(GUI gui, ArrayList<Layer> layers, PerfControl perfControl, Instrument instrument, TopTabs topTabs, ArrayList<AnimationController> animationController, AudioController audioController) {
        this.gui = gui;
        this.layers = layers;
        this.perfControl = perfControl;
        this.instrument = instrument;
        this.topTabs = topTabs;
        this.animationController = animationController;
        this.audioController = audioController;
    }
    
    public FileHandler(ArrayList<Layer> layers) {
        this.layers = layers;
        
    }

    public void run(File inputFile) {
        HTProperties properties = input(inputFile);
        if ((properties = input(inputFile)) != null) {
            if (JOptionPane.showConfirmDialog(gui, "Do you want to save current work?", "Save File", JOptionPane.YES_NO_OPTION) == 0) {
                gui.selectOutput("Save File", "outputFile");
            }
            executeInputFile(properties);
        } else {
            JOptionPane.showMessageDialog(null, "ERROR: Improper File");
        }
    }

    public HTProperties input(File inputFile) {
        if (!checkFile(inputFile)) {
            return null;
        }
        JSONParser parser = new JSONParser();
        //int oriLayersSize = layers.size();
        try {
            Object obj = parser.parse(new FileReader(inputFile.getAbsolutePath()));
            System.out.println(obj);
            JSONObject jsonObject = (JSONObject) obj;
            float mainValue = ((Number) jsonObject.get("mainVolume")).floatValue();
            JSONArray tabs = (JSONArray) jsonObject.get("tabs");
            int numberOfLayers = tabs.size();

            int insID[] = new int[numberOfLayers];
            float insVolume[] = new float[numberOfLayers];
            int timbre[] = new int[numberOfLayers];
            float tempo[] = new float[numberOfLayers];
            int interval[] = new int[numberOfLayers];
            int transposer[] = new int[numberOfLayers];
            ArrayList<ParseCell> cellCounterList[] = new ArrayList[numberOfLayers];

            for (int i = 0; i < numberOfLayers; i++) {
                JSONObject tab = (JSONObject) tabs.get(i);
                insID[i] = ((Number) tab.get("insId")).intValue();
                insVolume[i] = ((Number) tab.get("volume")).intValue();
                timbre[i] = ((Number) tab.get("timbre")).intValue();
                tempo[i] = ((Number) tab.get("tempo")).intValue();
                interval[i] = ((Number) tab.get("interval")).intValue();
                transposer[i] = ((Number) tab.get("transposer")).intValue();

                JSONArray counters = ((JSONArray) tab.get("counters"));
                ArrayList<ParseCell> parseCells = new ArrayList();

                for (int j = 0; j < counters.size(); j++) {
                    JSONObject singleCounter = (JSONObject) counters.get(j);
                    int type = ((Number) singleCounter.get("type")).intValue();
                    controllers.Direction direction = controllers.Direction.valueOf((String) singleCounter.get("direction"));
                    JSONObject position = (JSONObject) singleCounter.get("position");
                    int row = ((Number) position.get("row")).intValue();
                    int col = ((Number) position.get("col")).intValue();
                    parseCells.add(new ParseCell(type, direction, row, col));
                }
                cellCounterList[i] = parseCells;
            }
            return new HTProperties(numberOfLayers, cellCounterList, mainValue, insID, insVolume, timbre, tempo, interval, transposer);
        } catch (IOException | ParseException e) {
            return null;
        }
    }

    public void executeInputFile(HTProperties properties) {

        gui.noLoop();

        waitingForDrawRecognizeChanges();

        deleteTabs();

        recoverTabProperties(properties);

        recoverTabHarmonicTable(properties);

        gui.loop();

        layers.get(0).setOn();
    }

    private void deleteTabs() {
        for (int i = 0; i < animationController.size();) {
            animationController.get(0).stopAnimation();
            animationController.remove(0);
            audioController.remove(0);
            layers.get(0).remove();
            layers.remove(0);
        }
    }

    private void recoverTabHarmonicTable(HTProperties properties) {
        for (int i = 0; i < properties.getNumberOfLayers(); i++) {
            /* recover cell-counter hash map for each tabs */
            for (ParseCell n : properties.getCellCounterList()[i]) {
                int row = n.getRow();
                int col = n.getCol();
                int type = n.getType();
                controllers.Direction direction = n.getDirection();
                layers.get(i).getHarmonicTable().getCell(row, col)
                        .setDirection(direction);
                layers.get(i).getHarmonicTable().getCell(row, col)
                        .setType(type);

                switch (type) {
                    case GUI.START_COUNTER_ID:
                        layers.get(i).getNoteCounter().put(layers.get(i).getHarmonicTable().getCell(row, col), new StartCounter(gui, row, col, type, direction));
                        break;
                    case GUI.RICOCHET_COUNTER_ID:
                        layers.get(i).getNoteCounter().put(layers.get(i).getHarmonicTable().getCell(row, col), new RicochetCounter(gui, row, col, type, direction));
                        break;
                    case GUI.SPLITTER_COUNTER_ID:
                        layers.get(i).getNoteCounter().put(layers.get(i).getHarmonicTable().getCell(row, col), new SplitterCounter(gui, row, col, type, direction));
                        break;
                    case GUI.STOP_COUNTER_ID:
                        layers.get(i).getNoteCounter().put(layers.get(i).getHarmonicTable().getCell(row, col), new StopCounter(gui, row, col, type, direction));
                        break;

                    default:
                        break;
                }
            }
        }
    }

    private void recoverTabProperties(HTProperties properties) {

        /* recover main volume */
        perfControl.setVolume(properties.getMainValue());
        for (int i = 0; i < properties.getNumberOfLayers(); i++) {

            int tabNum = i;
            /* recover tabs */
            //if (i >= oriLayersSize) {
            layers.add(i, new Layer(gui, i, topTabs.getButton(), i));
            //}
            // init note[][]
            layers.get(i).getHarmonicTable().initCells();
            /**
             * *************
             */
            /* recover audio controller */
            audioController.setAudioPlayerOfCurrentTab(i, properties.getInsID()[i]);
            /**
             * *************************
             */
            /* recover animation controller */
            animationController.add(new AnimationController(layers
                    .get(tabNum).getHarmonicTable().getCellArray(),
                    (audioController.getAudioPlayerCurrentTab(i))));
            /**
             * *****************************
             */
            // change instrument controller name
            instrument.setInstrumentController(i, properties.getInsID()[i]);
            instrument.setInstrumentVolume(i, properties.getInsVolume()[i]);

            /* recover tab attributes: ins id, timbre, tempo, interval, transposer */
            layers.get(i).setAllAttributes(properties.getInsID()[i], properties.getInsVolume()[i], properties.getTimbre()[i],
                    properties.getTempo()[i], properties.getInterval()[i], properties.getTransposer()[i]);


        }
    }

    private void waitingForDrawRecognizeChanges() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void output(File outputFile, float mainVolume) {
        String output = "{\"mainVolume\":" + mainVolume;
        output += ",\"tabs\":[\n{";

        for (Layer l : layers) {

            // if not the first tab
            if (l.getOrder() != 0) {
                output += "\n,{";
            }

            output += "\n\"order\":" + l.getOrder() + "\n,\"insId\":"
                    + l.getInsId() + "\n,\"insName\":\""
                    + Instrument.INSTRUMENT_NAMES[l.getInsId()] + "\"";

            output += "\n,\"volume\":" + l.getVolume() + "\n,\"timbre\":"
                    + l.getTimbre() + "\n,\"tempo\":" + l.getTempo() + "\n,\"interval\":" + l.getInterval()
                    + "\n,\"transposer\":" + l.getTransposer();
            output += "\n,\"counters\":[";
            int count = 0;
            for (Map.Entry<Cell, Counter> nc : l.getNoteCounter().entrySet()) {
                Counter counter = (Counter) nc.getValue();
                Cell cell = (Cell) nc.getKey();
                if (count == 0) {
                    output += "\n";
                } else {
                    output += "\n,";
                }
                count++;
                output += "{\"type\":" + counter.getType() + ",\"direction\":\""
                        + counter.getDirection() + "\",\"position\":{\"row\":"
                        + cell.getRow() + ",\"col\":" + cell.getCol() + "}}";
            }
            output += "\n]}";
        }
        output += "\n]}";
        System.out.println(output);

        try {																		//+ ".dat"
            try (FileOutputStream fop = new FileOutputStream(outputFile.getAbsolutePath())) {
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                }

                // write the content in bytes
                fop.write(output.getBytes());
                fop.flush();
            }

        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public boolean checkFile(File inputFile) {
        if (inputFile.exists()) {
            if (inputFile.canRead()) {
                if (inputFile.isFile()) {
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "ERROR: File is a directory");
                }
            } else {
                JOptionPane.showMessageDialog(null, "ERROR: Access denied");
            }
        } else {
            JOptionPane.showMessageDialog(null, "ERROR: No file!");
        }
        // Return

        return (false);
    }
}
