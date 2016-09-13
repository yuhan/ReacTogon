/** This class is for containing attributes in different instrument layers
 * @author: Yuhan Luo
 */
package gui;

import controlP5.Button;
import controlP5.RadioButton;
import gui.counters.Counter;

import java.util.concurrent.ConcurrentHashMap;

public class Layer {

	private GUI gui;
	private Instrument instrument;
	private RadioButton radioButton;

	private Button removeLayer; // This is the 'x' button to remove a layer
	public controlP5.Toggle item;

	// variables for different instrument layers
	private int order;
	private int insId;
	private HarmonicTable harmonicTable;
	private ConcurrentHashMap<Cell, Counter> noteCounter;
	private float volume;
	private int timbre;
	private float tempo;
	private int interval;
	private int transposer;

	public Layer(GUI gui, int n, controlP5.RadioButton radioButton, int insId) {
		this.gui = gui;

		this.order = n;
		this.insId = insId;
		this.radioButton = radioButton;
		this.harmonicTable = new HarmonicTable(gui);
		this.noteCounter = new ConcurrentHashMap<>();

		// show the item
		this.item = radioButton.getItem(order);
		setItem(insId);

		// show the corresponding instrument volume controller
		gui.instrument.setInstrumentController(order, insId);
		// init the remove button [x]
		this.removeLayer = initRemoveButton(order);

		// set the default volume, timbre and tempo
		setVolume(GUI.DEFAULT_INSTRUMENT_VOL);
		setTimbre(GUI.DEFAULT_TIMBRE);
		setTempo(GUI.DEFAULT_TEMPO);
		setInterval(GUI.DEFAULT_INTERVAL);

	}

	private Button initRemoveButton(int n) {
		Button remove = gui.cp5.addButton("remove" + n).setCaptionLabel("x")
				.setColorBackground(gui.color(99, 80, 103, 100))
				.setColorForeground(gui.color(202, 105, 99, 100))
				.setColorActive(gui.color(99, 80, 103, 100))
				.setId(n + GUI.REMOVE_OFFSET);

		remove.getCaptionLabel().setFont(gui.createFont("da", 15))
				.toUpperCase(true);

		return remove;
	}

	public void displayRemove(float width, float height) {
		
		int tab_x = (int) (0.02f * width);
		int tab_y = (int) (0.05f * height);
		int tab_width = (int) (0.13f * width);
		int button_width = (int) (0.03f * height);
		
		removeLayer.setPosition(tab_x + tab_width * (order + 1) - button_width,
				tab_y)
				.setSize(button_width, button_width);
	}

	public void remove() {
		item.setVisible(false); // remove item, by setting it invisible
		removeLayer.remove(); // remove the [x] button
	}

	public void moveForwardTab(int index) {
		this.order = index - 1;
		changeOrder(order);
	}

	private void setItem(int id) {
		item.setCaptionLabel(instrument.INSTRUMENT_NAMES[id]);
		item.setVisible(true);
	}

	private void changeOrder(int order) {
		this.item = radioButton.getItem(order);
		setItem(insId);
		radioButton.getItem(order + 1).setVisible(false);
		this.removeLayer.remove();
		this.removeLayer = initRemoveButton(order);
	}

	public void setOn() {
		item.setState(true);
	}

	// setter
	public void setInsId(int insId) {
		this.insId = insId;
		setItem(insId);
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public void setHarmonicTable(HarmonicTable harmonicTable) {
		this.harmonicTable = harmonicTable;
	}

	public void setNoteCounter(ConcurrentHashMap<Cell, Counter> noteCounter) {
		this.noteCounter = noteCounter;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public void setTimbre(int timbre) {
		this.timbre = timbre;
	}

	public void setTempo(float tempo) {
		this.tempo = tempo;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public void setTransposer(int transposer) {
		this.transposer = transposer;
	}

	// getter
	public int getOrder() {
		return this.order;
	}

	public int getInsId() {
		return this.insId;
	}

	public HarmonicTable getHarmonicTable() {
		return this.harmonicTable;
	}

	public float getVolume() {
		return this.volume;
	}

	public int getTimbre() {
		return this.timbre;
	}

	public float getTempo() {
		return this.tempo;
	}

	public int getInterval() {
		return this.interval;
	}

	public int getTransposer() {
		return this.transposer;
	}

	public ConcurrentHashMap<Cell, Counter> getNoteCounter() {
		return this.noteCounter;
	}

	public void setAllAttributes(int insID, float insVolume, int timbre,
			float tempo, int interval, int transposer) {
		/* recover tab id */
		setInsId(insID);
		/* recover volume for each tabs */
		setVolume(insVolume);
		/* recover timbre for each tabs */
		setTimbre(timbre);
		/* recover tempo for each tabs */
		setTempo(tempo);
		/* recover interval for each tabs */
		setInterval(interval);
		/* recover interval for each tabs */
		setTransposer(transposer);

	}
}