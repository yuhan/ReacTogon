
package main;

import gui.GUI;

public class Main {
	
	private static final String GUI_PACKAGE_NAME = GUI.class.getPackage().toString().substring(8);	// substr(8) to avoid storing the String "package "
	
    public static void main(String args[]) {
        
    	GUI.main(GUI_PACKAGE_NAME + ".GUI"); // PApplet.main takes a String argument in the form: package.classname
    }
}
