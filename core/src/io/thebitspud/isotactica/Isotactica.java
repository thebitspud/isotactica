package io.thebitspud.isotactica;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.thebitspud.isotactica.screens.GameScreen;
import io.thebitspud.isotactica.screens.PauseScreen;
import io.thebitspud.isotactica.screens.TitleScreen;

import java.util.HashMap;

/**
 * The Game class from which all other classes emerge
 * Global variables and common functions go here
 */

public class Isotactica extends Game {
	private SpriteBatch batch;

	public enum ScreenID {
		TITLE,
		GAME,
		PAUSE
	}

	private HashMap<ScreenID, Screen> screens;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		// finish asset loading here

		screens = new HashMap<>();
		screens.put(ScreenID.TITLE, new TitleScreen());
		screens.put(ScreenID.GAME, new GameScreen());
		screens.put(ScreenID.PAUSE, new PauseScreen());

		setScreen(getScreen(ScreenID.TITLE));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}

	/* Getters and Setters */

	public Screen getScreen(ScreenID id) {
		return screens.get(id);
	}

	public void setScreen(ScreenID id) {
		setScreen(screens.get(id));
	}

	public SpriteBatch getBatch() {
		return batch;
	}
}