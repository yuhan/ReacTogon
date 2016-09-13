/** This class is for displaying and updating performance control sliders
 * @author: Yuhan Luo
 */
package gui;

import controlP5.Slider;

public class PerfControl {

	private Slider[] controllers = new Slider[GUI.NUMBER_CONTROL];
	private String[] controllerName = { "volume", "timbre", "tempo", "interval" };
	private GUI gui;

	PerfControl(GUI gui) {
		this.gui = gui;
	}

	public void init() {
		for (int i = 0; i < controllers.length; i++) {
			controllers[i] = gui.cp5.addSlider(controllerName[i])
					.setScrollSensitivity(0.01f);

			switch (controllerName[i]) {
			case "volume":
				controllers[i].setRange(0, 16383).setValue(GUI.DEFAULT_VOL)
						.setColorBackground(gui.color(188, 224, 0, 50))
						.setColorForeground(gui.color(188, 224, 0))
						.setColorActive(0xffd5f432)
						.setId(GUI.PERFORMANCE_OFFSET + i);
				break;
				
			case "timbre":
				controllers[i].setRange(1, 8).setValue(GUI.DEFAULT_TIMBRE)
						.setNumberOfTickMarks(8).showTickMarks(false)
						.setColorBackground(gui.color(235, 191, 49, 50))
						.setColorForeground(gui.color(235, 191, 49))
						.setColorActive(0xfff2ce5a)
						.setId(GUI.PERFORMANCE_OFFSET + i);

				break;
				
			case "tempo":
				controllers[i].setRange(500, 100).setValue(GUI.DEFAULT_TEMPO)
						.setColorBackground(gui.color(255, 144, 92, 50))
						.setColorForeground(gui.color(255, 144, 92))
						.setColorActive(0xfff9aa85)
						.setId(GUI.PERFORMANCE_OFFSET + i);
				break;

			case "interval":
				controllers[i].setRange(1100, 0).setValue(GUI.DEFAULT_INTERVAL)
						.setNumberOfTickMarks(9).showTickMarks(false)
						.setColorBackground(gui.color(202, 105, 99, 50))
						.setColorForeground(gui.color(202, 105, 99))
						.setColorActive(0xffec908b)
						.setId(GUI.PERFORMANCE_OFFSET + i);
				break;

			default:
				break;
			}
		}
	}

	public void display(float width, float height) {

		int perf_control_x = (int) (gui.counter_x + gui.counter_diameter + (gui.counter_x - gui.table_bg_x - gui.table_bg_width));
		int perf_control_y = (int) (0.43f * height);
		int perf_control_bg_width = (int) (0.245f * width);
		int perf_control_bg_height = (int) (0.37f * height);
		int perf_control_x_gap = (int) (0.01f * width);
		int perf_control_width = (perf_control_bg_width - (GUI.NUMBER_CONTROL + 1)
				* perf_control_x_gap)
				/ GUI.NUMBER_CONTROL;
		int perf_control_height = (int) (0.30f * height);
		int perf_control_y_gap = (int) (0.03f * height);

		// display background rectangle
		gui.fill(255, 255, 255, 25);
		gui.rect(perf_control_x, perf_control_y, perf_control_bg_width,
				perf_control_bg_height);

		// display components according window size
		for (int i = 0; i < controllers.length; i++) {
			controllers[i].setPosition(
					perf_control_x + perf_control_x_gap * (i + 1)
							+ perf_control_width * i,
					perf_control_y + perf_control_y_gap).setSize(
					perf_control_width, perf_control_height);

			controllers[i].getCaptionLabel()
					.setFont(gui.createFont("verdana", 10)).toUpperCase(true);
			controllers[i].getCaptionLabel().getStyle().marginLeft = (int) (perf_control_width * 0.05f);
			controllers[i].getValueLabel().setVisible(false);
		}
	}

	public void update() {
		// volumeSlider.setValue(gui.layers.get(gui.getCurrentTab()).getVolume());
		// for (Layer l : gui.layers){
		// System.out.println(l.getOrder() +": "+ l.getTempo());
		// }
		// System.out.println("---qian---");
		//

		controllers[1]
				.setValue(gui.layers.get(gui.getCurrentTab()).getTimbre());
		controllers[2].setValue(gui.layers.get(gui.getCurrentTab()).getTempo());
		controllers[3].setValue(gui.layers.get(gui.getCurrentTab())
				.getInterval());
		// System.out.println("current: " + gui.getCurrentTab() + ": " +
		// gui.layers.get(gui.getCurrentTab()).getTempo());
		// for (Slider s : controllers){
		// System.out.println(s.getName() +": "+s.getValue());
		// }
		// System.out.println("----------------");
	}

	public float getVolume() {
		return controllers[0].getValue();
	}

	public void setVolume(float volume) {
		controllers[0].setValue(volume);
	}

	public float getController(int n) {
		return controllers[n].getValue();
	}

	public void setController(int n, float value) {
		controllers[n].setValue(value);
	}
}