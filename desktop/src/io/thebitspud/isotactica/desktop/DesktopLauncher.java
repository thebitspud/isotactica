package io.thebitspud.isotactica.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.thebitspud.isotactica.Isotactica;

/**
 * Launches Isotactica as a desktop application
 */

public class DesktopLauncher {
	public static void main (String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setTitle("Isotactica");
		config.setWindowedMode(1366, 768);
		config.setResizable(false);

		new Lwjgl3Application(new Isotactica(), config);
	}
}
