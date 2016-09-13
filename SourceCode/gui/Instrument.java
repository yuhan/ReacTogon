/** This class is for instrument displaying and modifying
 * @author: Yuhan Luo, Ross
 */
package gui;

import controlP5.Slider;

public class Instrument {
	
	private GUI gui;
	private Slider[] instrumentVolume = new Slider[GUI.NUMBER_TAB];
	public static final String[] INSTRUMENT_NAMES = { "PIANO", "ACOUSTIC_BASS",
			"ACOUSTIC_GRAND", "ALTO_SAX", "BIRD_TWEET", "BRASS_SECTION",
			"BRIGHT_ACOUSTIC", "CELLO", "CHOIR", "CHURCH_ORGAN", "CLARINET",
			"CONTRABASS", "CRYSTAL", "DISTORTION_GUITAR",
			"ELECTRIC_BASS_FINGER", "ELECTRIC_CLEAN_GUITAR", "FLUTE",
			"FRENCH_HORN", "GUITAR", "HARMONICA", "HARPISCHORD", "HONKEY_TONK",
			"METALLIC", "MUSIC_BOX", "ORCHESTRAL_STRINGS", "PIZZICATO_STRINGS",
			"POLYSYNTH", "RAIN", "SITAR", "SOPRANO_SAX", "STRING_ENSEMBLE_1",
			"TANGO_ACCORDIAN", "TENOR_SAX", "TIMPANI", "TREMOLO_STRINGS",
			"TROMBONE", "TRUMPET", "TUBA", "VIOLA", "VIOLIN", "VOICE",
			"WHISTLE", "WOODBLOCK", "XYLOPHONE" };

	Instrument(GUI gui) {
		this.gui = gui;
	}

	public void init() {
		for (int i = 0; i < GUI.NUMBER_TAB; i++) {
			initInstrument(i);
		}
		instrumentVolume[0].setVisible(true);
	}

	// inits and displays instrument sliders
	public void initInstrument(int instrumentIndex) {
		instrumentVolume[instrumentIndex] = gui.cp5
				.addSlider("instrument" + instrumentIndex)
				.setScrollSensitivity(0.01f).setRange(0, 100)
				.setColorBackground(gui.color(99, 80, 103, 100))
				.setColorForeground(0xff8994cd).setColorActive(0xffb9c0e9)
				.setId(instrumentIndex + GUI.INSTRUMENT_OFFSET)
				.setVisible(false);

		instrumentVolume[instrumentIndex].getCaptionLabel()
				.setFont(gui.createFont("verdana", 12)).toUpperCase(true);
		instrumentVolume[instrumentIndex].getValueLabel().setVisible(false);
	}

	public void display(float width, float height) {
		int instrument_x = (int)  (gui.counter_x + gui.counter_diameter + (gui.counter_x - gui.table_bg_x - gui.table_bg_width));
		int instrument_y = (int) (0.05f * height);
		int instrument_bg_width = (int) (0.245f * width);
		int instrument_bg_height = (int) (0.33f * height);
		int instrument_width = (int) (0.225f * width);
		int instrument_height = (int) (0.05f * height);
		int instrument_x_gap = (int) (0.01f * width);
		int instrument_y_gap = (instrument_bg_height - GUI.NUMBER_TAB
				* instrument_height)
				/ (GUI.NUMBER_TAB + 1);
		
		// display background rectangle
		gui.fill(255, 255, 255, 25);
		gui.rect(instrument_x, instrument_y, instrument_bg_width,
				instrument_bg_height);

		// display components according to window size
		for (int i = 0; i < GUI.NUMBER_TAB; i++) {
			instrumentVolume[i].setPosition(
					instrument_x + instrument_x_gap,
					instrument_y + instrument_height * i + instrument_y_gap
							* (i + 1)).setSize(instrument_width,
					instrument_height);
			instrumentVolume[i].getCaptionLabel().getStyle().marginLeft = (int) (-0.9f * instrument_width);

		}
	}

	// getters and setters	
	public void setInstrumentController(int index, int id) {
		instrumentVolume[index].setCaptionLabel(INSTRUMENT_NAMES[id]);
		instrumentVolume[index].setVisible(true);
	}

	public float getInstrumentVolume(int n) {
		return instrumentVolume[n].getValue();
	}

	public void setInstrumentVolume(int n, float value) {
		instrumentVolume[n].setValue(value);
	}

	public void removeInstrument(int index) {
		instrumentVolume[index].setVisible(false);
	}

	// move the instrument forward
	public void moveForwardInstrument(int index) {
		String movedLabel = instrumentVolume[index].getCaptionLabel().getText();
		instrumentVolume[index].setVisible(false);
		instrumentVolume[index - 1].setVisible(true);
		instrumentVolume[index - 1].setCaptionLabel(movedLabel);

//		// for test
//				 for(Slider a : instrumentVolume){
//				 System.out.print(a.getLabel() + "\t");
//				 System.out.println(a.getName());
//				 }
//				 System.out.println("-------");
	}
}
