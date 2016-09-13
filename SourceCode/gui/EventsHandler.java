/** This class is a handler dealing with the controlling events
 * @author: Yuhan Luo, San Ha
 */
package gui;

import controlP5.ControlEvent;
import controllers.AnimationController;
import controllers.AudioController;
import gui.buttons.Buttons;
import gui.buttons.PlayButton;

import java.util.ArrayList;

import org.jfugue.Pattern;

public class EventsHandler {

	private static EventsHandler instance;
	private GUI gui;

	// private ArrayList<Layer> layers;
	private EventsHandler(GUI gui) {
		this.gui = gui;
		// this.layers = layers;
	}

	public static EventsHandler getInstance(GUI gui) {
		if (instance == null) {
			instance = new EventsHandler(gui);
		}
		return instance;
	}

	// play and pause music
	public void controlPlay(ArrayList<Layer> layers, PlayButton addButton) {
		addButton.getButton().setSwitch(true);

		// ##########################
		if (addButton.getButton().isOn()) {
			for (AnimationController ac : gui.animationController) {
				new Thread(ac).start();
			}
			// streamPlayer.setIsRecording(true);
		} else {
			for (AnimationController ac : gui.animationController) {
				ac.stopAnimation();
			}
			// streamPlayer.close();
			/*
			 * Player player = new Player(); Pattern pattern = new
			 * Pattern(streamPlayer.getSavedPattern()); player.saveMidi(pattern,
			 * new File("music-file.mid"));
			 */
		}
	}

	// clear counters
	public void controlClear(ArrayList<Layer> layers) {
		layers.get(gui.getCurrentTab()).getHarmonicTable().initCells();
		layers.get(gui.getCurrentTab()).getNoteCounter().clear();
	}

	// add layer - add tab, instrument volume control slider
	public void controlAdd(ControlEvent theEvent, ArrayList<Layer> layers,
			Buttons buttons, Instrument instrument, TopTabs topTabs) {
		// the max num of layers is 4
		if (layers.size() < 4) {
			int insId = (int) theEvent.getGroup().getValue();
			int insIndex = layers.size();

			// add selected instrument into 'layers'
			layers.add(new Layer(gui, layers.size(), topTabs.getButton(), insId));
			Layer l = layers.get(layers.size() - 1);
			System.out.println("add id: " + l.getInsId());
			l.getHarmonicTable().initCells();
			l.setOn();

			// set instrument volume
			gui.audioController.setAudioPlayerOfCurrentTab(gui.getCurrentTab(),
					insId);
			// instrument.initInstrument(insIndex, insId);
			instrument.setInstrumentVolume(insIndex, l.getVolume());
		}
	}

	// change instrument
	public void controlChangeInstrument(ControlEvent theEvent,
			ArrayList<Layer> layers, AudioController audioController,
			Buttons buttons, Instrument instrument) {
		int currentTab = gui.getCurrentTab();
		// get instrument id
		int insId = (int) theEvent.getGroup().getValue();
		// change tab id
		layers.get(currentTab).setInsId(insId);
		// change instrument controller name
		instrument.setInstrumentController(gui.getCurrentTab(), insId);

		audioController.getAudioPlayerCurrentTab(currentTab).setInstrument(
				insId);

		System.out.println(gui.getCurrentTab() + ": " + insId);

		gui.changeButton.hide();
	}

	// control transposer
	public void controlTransposer(ControlEvent theEvent, ArrayList<Layer> layers) {
		int value = (int) theEvent.getValue();
		gui.audioController.getAudioPlayerCurrentTab(gui.getCurrentTab())
				.setPitch(value);
		layers.get(gui.getCurrentTab()).setTransposer(value);
	}

	// control tab
	public void controlTab(ControlEvent theEvent, ArrayList<Layer> layers) {
		int value = (int) theEvent.getValue();
		if (value != -1 && value < layers.size()) {

			// control right click on tabs
			if (gui.mouseButton == GUI.RIGHT) {
				gui.changeButton.show(gui.mouseX, gui.mouseY);
			}
			gui.setCurrentTab(value);
			System.out.println("currentTab: " + gui.getCurrentTab());
			
			// set the changing tab flag to be true
			gui.isChangingTabFlag = true;
		}
	}

	// control remove [x] button
	public int controlRemove(ControlEvent theEvent, ArrayList<Layer> layers,
			Instrument instrument) {
		int index = -1;

		// min num of layers is 1
		if (layers.size() > 1) {
			index = theEvent.getId() - GUI.REMOVE_OFFSET;

			System.out.println("removed index: " + index + "++"
					+ theEvent.getId());
			removeTabAtIndex(index, layers, instrument);
		}
		return index;
	}

	// remove tab and rearrange, when click on the [x]
	private void removeTabAtIndex(int index, ArrayList<Layer> layers,
			Instrument instrument) {
		// clear the notes-counters in the removed layer
		layers.get(index).getNoteCounter().clear();
		// reorder the list 'layers' and the instruments
		rearrangeList(index, layers, instrument);

		// set the first one on as default, if the current tab is removed
		if (gui.getCurrentTab() == index || index >= layers.size()) {
			layers.get(0).setOn();
		} else {
			layers.get(index).setOn();
		}
	}

	// change instrument volume
	public void controlInstrumentVolume(ControlEvent theEvent,
			ArrayList<Layer> layers) {
		int index = theEvent.getId() - GUI.INSTRUMENT_OFFSET;
		float value = theEvent.getValue();

		if (index >= layers.size()) {
			return;
		}

		gui.audioController.getAudioPlayerCurrentTab(index)
				.setInstrumentVolume(value);
		layers.get(index).setVolume(value);
	}

	// change performance: volume, timbre, tempo and interval
	public void controlPerformance(ControlEvent theEvent,
			ArrayList<Layer> layers, AudioController audioController,
			ArrayList<AnimationController> animationController) {
		int value = (int) theEvent.getValue();
		int currentTab = gui.getCurrentTab();

		/**
		 * a flag to notice this function: 
		 * if the tab is changing, do not set the performance values to layers
		 */

		if (gui.isChangingTabFlag)
			return;
		switch (theEvent.getName()) {
		case "volume":
			audioController.setVolume(value);
			break;
		case "timbre":
			layers.get(currentTab).setTimbre(value);
			audioController.getAudioPlayerCurrentTab(currentTab).setDuration(
					value);
			break;
		case "tempo":
			layers.get(currentTab).setTempo(value);
			animationController.get(currentTab).setAnimationSpeed(value);
			break;
		case "interval":
			layers.get(currentTab).setInterval(value);
			animationController.get(currentTab).setInterval(value);
			break;
		default:
			break;
		}
	}

	// record MIDI
	public Pattern controlRecord(Buttons buttons) {
		/*
		 * buttons.getRecordButton().setSwitch(true); if
		 * (buttons.getRecordButton().isOn()) {
		 * gui.audioController.startRecording(); } else {
		 * gui.audioController.stopRecording();
		 * gui.selectOutput("Save Midi File", "recordMusic"); return
		 * gui.audioController.getRecordedPattern(); }
		 */
		return null;
	}
      
	// display tutorial video
	public void playTutorial() {
		gui.link("https://www.youtube.com/watch?v=JASApSuaLlA&feature=youtu.be");
    }

	// rearrange elements in list after removing
	private void rearrangeList(int n, ArrayList<Layer> layers,
			Instrument instrument) {
		for (int i = 0; i < layers.size(); i++) {
			if (i == n) {
				layers.get(i).remove();
				instrument.removeInstrument(i);
			} else if (i > n) {
				// move the tab forward
				layers.get(i).moveForwardTab(i);
				layers.set(i - 1, layers.get(i));

				// move the instrument controller forward
				instrument.moveForwardInstrument(i);
				// set back the volume
				instrument.setInstrumentVolume(i - 1, layers.get(i - 1)
						.getVolume());
			}
		}
		layers.remove(layers.size() - 1);

		// for test
		// for (Layer l : layers){
		// System.out.println(l.getTabLabel());
		// }
	}
}