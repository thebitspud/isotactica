package io.thebitspud.isotactica.utils;

/**
 * Implements common methods for objects with unique IDs
 */

public interface Identifiable {
	Object getID();
	String getIDText();
	String getInfoText();
}
