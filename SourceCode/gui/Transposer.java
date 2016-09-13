/** This class is for displaying and updating transposer
 * @author: Yuhan Luo
 */
package gui;

import controlP5.RadioButton;
import controlP5.Toggle;

//transposer bar
class Transposer{

    private GUI gui;
    private int pitch_num;
    private int[] pitch;
    private int pitch_gap;
    private int pitch_width;
    private int pitch_height;
    private int pitch_x;
    private int pitch_y;
    RadioButton pitch_button;

    Transposer(GUI gui) {
    	this.gui = gui;
    	
        pitch_num = 16;
        pitch = new int[pitch_num];
        pitch_gap = 5;
    }

    public void init() {
        pitch_button = gui.cp5.addRadioButton("radioButton")
                .setColorBackground(gui.color(255, 255, 255, 25))
                .setColorForeground(gui.color(120, 127, 194, 200))
                .setColorActive(gui.color(120, 127, 194))
                .setColorLabel(gui.color(255))
                .setItemsPerRow(pitch_num)
                .setSpacingColumn(pitch_gap);

        for(int i = 0; i < pitch_num; i++){
            pitch_button.addItem(new String(-16+i*2+""), -16+i*2);
        }
         
         // set default 0 to be on
        pitch_button.getItem(8).setState(true);
         
        pitch_button.hideLabels();

        for (Toggle t : pitch_button.getItems()) {
            t.getCaptionLabel().setColorBackground(gui.color(255, 255, 255, 25));
        }
    }
    
    public void display(float width, float height){
        pitch_x = 0;
        pitch_y = (int) (gui.table_bg_y + gui.table_bg_height + 0.02f * height);
        pitch_width = (int) (0.72f * width / pitch_num - pitch_gap);
        pitch_height = (int) height - pitch_y;
        
        pitch_button
        .setPosition(pitch_x, pitch_y)
        .setSize(pitch_width, pitch_height);
    	
    }

    public void update() {
        pitch_button.getItem((gui.layers.get(gui.getCurrentTab()).getTransposer() + 16) / 2).setState(true);
	}
    
    public RadioButton getPitchButton() {
        return pitch_button;
    }
    
}
