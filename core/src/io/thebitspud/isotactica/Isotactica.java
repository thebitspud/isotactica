package io.thebitspud.isotactica;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.thebitspud.isotactica.screens.GameScreen;
import io.thebitspud.isotactica.screens.PauseScreen;
import io.thebitspud.isotactica.screens.TitleScreen;
import io.thebitspud.isotactica.utils.AssetHandler;

import java.util.EnumMap;

/**
 * The Game class from which all other classes emerge
 * Global variables and common functions go here
 */

public class Isotactica extends Game {
	private AssetHandler assets;
	private SpriteBatch batch;

	/** An enum of all accessible game screens */
	public enum ScreenKey {
		TITLE, GAME, PAUSE
	}

	private EnumMap<ScreenKey, Screen> screens;
	
	@Override
	public void create () {
		assets = new AssetHandler();
		batch = new SpriteBatch();

		assets.loadAll();

		screens = new EnumMap<>(ScreenKey.class);
		screens.put(ScreenKey.TITLE, new TitleScreen());
		screens.put(ScreenKey.GAME, new GameScreen());
		screens.put(ScreenKey.PAUSE, new PauseScreen());

		setScreen(getScreen(ScreenKey.TITLE));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose() {
		assets.dispose();
		batch.dispose();
	}

	/* Getters and Setters */

	public AssetHandler getAssets() {
		return assets;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	/** Retrieves the screen which the specified key is mapped to */
	public Screen getScreen(ScreenKey key) {
		return screens.get(key);
	}

	/** Sets the active screen to the one which the specified key is mapped to */
	public void setScreen(ScreenKey key) {
		setScreen(screens.get(key));
	}
}