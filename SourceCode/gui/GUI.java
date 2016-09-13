/** This class is the main entrance of the user interface
 * the methods labeled by @override is derived from the contronP5 and Processing 2.0 libraries
 * @author: Yuhan Luo, San Ha
 */
package gui;

import controlP5.*;
import controllers.AnimationController;
import controllers.AudioController;
import controllers.FileHandler;
import gui.buttons.AddButton;
import gui.buttons.Buttons;
import gui.buttons.ChangeButton;
import gui.buttons.ClearButton;
import gui.buttons.OpenButton;
import gui.buttons.PlayButton;
import gui.buttons.RecordButton;
import gui.buttons.SaveButton;
import gui.buttons.TutorialButton;
import gui.counters.Counter;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JOptionPane;
import org.jfugue.Pattern;
import org.jfugue.Player;
import processing.core.*;

public class GUI extends PApplet {

    // What is this?
    private static final long serialVersionUID = 1L;
    public static final int PLAY_ID = 1;
    public static final int CLEAR_ID = 2;
    public static final int OPEN_ID = 3;
    public static final int SAVE_ID = 4;
    public static final int RECORD_ID = 5;
    public static final int ADD_ID = 6;
    public static final int CHANGE_ID = 7;
    public static final int TUTORIAL_ID = 8;
    public static final int REMOVE_OFFSET = 100;
    public static final int INSTRUMENT_OFFSET = 200;
    public static final int PERFORMANCE_OFFSET = 300;
    public static final int START_COUNTER_ID = 0;
    public static final int RICOCHET_COUNTER_ID = 1;
    public static final int SPLITTER_COUNTER_ID = 2;
    public static final int STOP_COUNTER_ID = 3;
    public static final int DEFAULT_VOL = 11111;
    public static final int DEFAULT_INSTRUMENT_VOL = 100;
    public static final int DEFAULT_TIMBRE = 4;
    public static final int DEFAULT_TEMPO = 140;
    public static final int DEFAULT_INTERVAL = 0;
    public static final int NUMBER_TAB = 4;
    public static final int NUMBER_CONTROL = 4;
    // import cp5 library
    public ControlP5 cp5; // cp5
    AudioController audioController = new AudioController();
    ArrayList<AnimationController> animationController = new ArrayList<>();
    public int currentTab = 0;
    public boolean isChangingTabFlag = false;
    private Pattern recordedPattern;
    // instance
    Counter tempCounter;
    InitCounter initCounter;
    Instrument instrument;
    PerfControl perfControl;
    Transposer transposer;
    Buttons buttons;
    TopTabs topTabs;
    PlayButton playButton;
    ClearButton clearButton;
    OpenButton openButton;
    SaveButton saveButton;
    RecordButton recordButton;
    AddButton addButton;
    ChangeButton changeButton;
    TutorialButton tutorialButton;
    ArrayList<Layer> layers = new ArrayList<>();
    // // user interface elements
    // int width = 1000;
    // int height = 600;
    // table
    public int table_col_num, table_small_row_num, table_row_num;
    public float table_bg_x, table_bg_y, table_bg_width, radius, note_width,
            note_height, table_bg_height, table_bg_x_right, table_bg_y_bottom;
    // counter
    public int counter_x, counter_y, counter_diameter, counter_gap, counter_fix_x;
    public PShape start_counter, ricochet_counter, splitter_counter, stop_counter;

    @Override
    public void setup() {
        width = displayWidth;
        height = displayHeight;
        
//        size(width, height, P2D);
        size(width, height);

        System.out.println(frame);
        if (frame != null) {
        	frame.setTitle("Thunderbolts");
            frame.setResizable(true);
            frame.setMinimumSize(new Dimension(1000,600));
        }
        
        // set each component's size
        updateTableVariable();
        updateCounterVariable();
        
        //smooth();
        //noStroke();

        // load counter svg files
        start_counter = loadShape("pic/svg_files/start_counter.svg");
        ricochet_counter = loadShape("pic/svg_files/ricochet_counter.svg");
        splitter_counter = loadShape("pic/svg_files/splitter_counter.svg");
        stop_counter = loadShape("pic/svg_files/stop_counter.svg");

        // init object
        cp5 = new ControlP5(this);
        cp5.setFont(createFont("verdana", 10));
        initCounter = new InitCounter(this);
        instrument = new Instrument(this);
        transposer = new Transposer(this);
        perfControl = new PerfControl(this);
        buttons = new Buttons(this);
        topTabs = new TopTabs(this, 4);
        playButton = new PlayButton(this);
        clearButton = new ClearButton(this);
        openButton = new OpenButton(this);
        saveButton = new SaveButton(this);
        recordButton = new RecordButton(this);
        addButton = new AddButton(this);
        changeButton = new ChangeButton(this);
        tutorialButton = new TutorialButton(this);
        // init instrument component
        instrument.init();

        layers.add(0, new Layer(this, 0, topTabs.getButton(), 0));
        layers.get(0).setOn();

        // init note[][]
        layers.get(0).getHarmonicTable().initCells();
        // init audio player for current tab
        audioController.initializeNewController();
        // init animation controller for current tab
        animationController.add(new AnimationController(layers.get(currentTab)
                .getHarmonicTable().getCellArray(), (audioController
                .getAudioPlayerCurrentTab(currentTab))));

        // init transposer and performance control panel
        transposer.init();
        perfControl.init();

        playButton.init();
        clearButton.init();
        openButton.init();
        saveButton.init();
        recordButton.init();
        addButton.init();
        changeButton.init();
        tutorialButton.init();

        // set the first volume controller
        instrument.setInstrumentVolume(0, layers.get(0).getVolume());
        
        // set frame rate
        frameRate(75);
    }
    
    @Override
    public boolean sketchFullScreen() {
        return false; // true = fullScreen.
    }
    
    @Override
    public void draw() {
    	width = this.getWidth();
    	height = this.getHeight();
    	
        // set background
        setAppBackgroud(); // program background

        // display resizable components
        componentResize(width, height);

        // display table
        layers.get(currentTab).getHarmonicTable().display(width,height);

        // display init counters
        initCounter.display();

	    // update transposer and perf value when tab switches
		if(isChangingTabFlag){
		    transposer.update();
		    perfControl.update();
		    // set the flag back to false
		    isChangingTabFlag = false;
		}

        // if there is a tempCounter
        if (tempCounter != null) {
            tempCounter.rollover(mouseX, mouseY);
            tempCounter.drag(mouseX, mouseY);
            tempCounter.display();
        }


        for (Map.Entry<Cell, Counter> nc : layers.get(currentTab)
                .getNoteCounter().entrySet()) {
            Counter counter = (Counter) nc.getValue();
            counter.rollover(mouseX, mouseY);
            counter.drag(mouseX, mouseY);
            counter.display();

            Cell cell = (Cell) nc.getKey();

            cell.attachCounter(counter);

        }

    }

    // update harmonic table related variables according to window size
    private void updateTableVariable(){
        // table
        table_col_num = 14;
        table_small_row_num = 7;
        table_row_num = (table_small_row_num + 1) * 2;
        table_bg_x = 0.02f * width;
        table_bg_y = 0.1f * height;
        table_bg_width = 0.62f * width;
        radius = table_bg_width / (3 * table_col_num / 2 + 0.5f);
        note_width = radius * 2;
        note_height = radius * sqrt(3);
        table_bg_height = note_height * (table_small_row_num + 1);
        table_bg_x_right = table_bg_x + table_bg_width;
        table_bg_y_bottom = table_bg_y + table_bg_height;        
    }

    // update counter related variables according to window size
    private void updateCounterVariable(){
        // counter
        counter_x = (int) (0.66f * width);
        counter_y = (int) (0.1f * height);
        counter_diameter = (int) note_height;
        counter_gap = (int) (0.02f * height);
        counter_fix_x = (int) (note_width - counter_diameter) / 2;
    }
    
    // resize all components according to window size
    private void componentResize(float width, float height){
    	updateTableVariable();
    	updateCounterVariable();

        transposer.display(width, height);
        topTabs.display(width, height);
        perfControl.display(width, height);
        instrument.display(width, height);
        
    	playButton.display(width, height);
    	clearButton.display(width, height);
        openButton.display(width, height);
        saveButton.display(width, height);
        recordButton.display(width, height);
        addButton.display(width, height);
        tutorialButton.display(width, height);
        
        for (Layer l: layers){
        	l.displayRemove(width, height);
        }

        MouseHandler.getInstance(this).updateVariable();

    }


    // set the whole program background
    private void setAppBackgroud() {
        int c1 = color(20, 17, 39);
        int c2 = color(145, 131, 149);

        //noFill();

        for (int i = 0; i <= width; i++) {
            float inter = map(i, 0, width, 0, 0.6f);
            int c = lerpColor(c1, c2, inter);
            stroke(c);
            //line(i, 0, i, height);
        }
        background(51);
    }

    /*** mouse event***/
    @Override
    public void mousePressed() {
        tempCounter = MouseHandler.getInstance(this).pressed();
    }

    @Override
    public void mouseDragged() {
        MouseHandler.getInstance(this).dragged();
    }

    @Override
    public void mouseReleased() {
        tempCounter = MouseHandler.getInstance(this).released();
    }

    /*** other event handlers***/
    public void controlEvent(ControlEvent theEvent) throws IOException {

        switch (theEvent.getId()) {
            case (PLAY_ID): // press play
                EventsHandler.getInstance(this).controlPlay(layers, playButton);
                break;
            case (CLEAR_ID): // press clear
                if (JOptionPane.showConfirmDialog(this, "Do you want to erase current work?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
                    EventsHandler.getInstance(this).controlClear(layers);
            }
               
                break;
            case (OPEN_ID): // press open
                selectInput("Open File", "inputFile");
                break;
            case (SAVE_ID): // press save
                selectOutput("Save File", "outputFile");
                break;
            case (RECORD_ID): // press record
                // Pattern pattern =
                // EventsHandler.getInstance(this).controlRecord(buttons);
                recordButton.getButton().setSwitch(true);
                if (recordButton.getButton().isOn()) {
                    audioController.startRecording();
                } else {
                    audioController.stopRecording();
                    recordedPattern = audioController.getRecordedPattern();
                    selectOutput("Save Midi File", "recordMusic");
                }

                // player.saveMidi(pattern, new File("test.mid"));
                break;
            case (ADD_ID): // press add
                EventsHandler.getInstance(this).controlAdd(theEvent, layers,
                        buttons, instrument, topTabs);
                // audioController.setAudioPlayerOfCurrentTab(currentTab,
                // layers.get(currentTab).getInsId());
                animationController.add(new AnimationController(layers
                        .get(currentTab).getHarmonicTable().getCellArray(),
                        (audioController.getAudioPlayerCurrentTab(currentTab))));

                break;
            case (CHANGE_ID): // press add
                EventsHandler.getInstance(this).controlChangeInstrument(theEvent,
                        layers, audioController, buttons, instrument);
                break;
            case (TUTORIAL_ID): // press show tutorial
               EventsHandler.getInstance(this).playTutorial();
                break;
        }

        if (theEvent.isFrom(transposer.getPitchButton())) {
            EventsHandler.getInstance(this).controlTransposer(theEvent, layers);
        }

        // change 'currentTab', when set the top tab on
        if (theEvent.isFrom(topTabs.getButton())) {
            EventsHandler.getInstance(this).controlTab(theEvent, layers);
        }
        // press top right of each tab - [x] remove
        if (theEvent.getId() >= GUI.REMOVE_OFFSET
                && theEvent.getId() < GUI.INSTRUMENT_OFFSET) {
            int removedTabIndex = EventsHandler.getInstance(this)
                    .controlRemove(theEvent, layers, instrument);
            if (removedTabIndex != -1) {
                animationController.get(removedTabIndex).stopAnimation();
                animationController.remove(removedTabIndex);
                audioController.remove(removedTabIndex);
            }
        }

        // press top right - change instrument volume
        if (theEvent.getId() >= GUI.INSTRUMENT_OFFSET
        		&& theEvent.getId() < GUI.PERFORMANCE_OFFSET) {
            EventsHandler.getInstance(this).controlInstrumentVolume(theEvent,
                    layers);
        }
        
        // press right hand side - change performance
        if (theEvent.getId() >= GUI.PERFORMANCE_OFFSET) {
        	EventsHandler.getInstance(this).controlPerformance(theEvent, layers, audioController, animationController);
        }
    }
    
    // file chooser for opening file
    public void inputFile(File selection) {
        if (selection != null) {
            FileHandler fileHandler = new FileHandler(this, layers, perfControl, instrument, topTabs, animationController, audioController);
            fileHandler.run(selection);
        }

    }
    
    // file chooser for saving file
    public void outputFile(File selection) {
        if (selection != null) {
            FileHandler fileHandler = new FileHandler(layers);
            fileHandler.output(selection, perfControl.getVolume());
        }
    }

    // record MIDI output
    public String recordMusic(File selection) throws IOException {
        if (selection != null) {
            Player player = new Player();
            player.saveMidi(recordedPattern,
                    new File(selection.getAbsolutePath() + ".mid"));
        }
        return null;
    }

    // getter and setter
    public int getCurrentTab() {
        return this.currentTab;
    }

    public void setCurrentTab(int n) {
        this.currentTab = n;
    }

    public ConcurrentHashMap<Cell, Counter> getNoteCounter() {
        return layers.get(currentTab).getNoteCounter();
    }
}
