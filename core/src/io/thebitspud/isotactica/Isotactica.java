package io.thebitspud.isotactica;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.thebitspud.isotactica.screens.*;
import io.thebitspud.isotactica.utils.AssetHandler;
import io.thebitspud.isotactica.world.World;

import java.util.EnumMap;

/**
 * The Game class from which all other classes emerge
 * Global variables and common functions go here
 */

public class Isotactica extends Game {
	/* Constants */

	public final int TILE_WIDTH = 64;
	public final int TILE_HEIGHT = 32;

	/* Data */

	private AssetHandler assets;
	private SpriteBatch batch;
	private World world;

	/** An enum of all accessible game screens */
	public enum ScreenKey {
		TITLE, GAME, PAUSE
	}

	private EnumMap<ScreenKey, JScreenTemplate> screens;

	/* Inherited Functions */
	
	@Override
	public void create() {
		assets = new AssetHandler(this);
		batch = new SpriteBatch();
		world = new World(this);

		assets.loadAll();

		screens = new EnumMap<>(ScreenKey.class);
		screens.put(ScreenKey.TITLE, new TitleScreen(this));
		screens.put(ScreenKey.GAME, new GameScreen(this));
		screens.put(ScreenKey.PAUSE, new PauseScreen(this));

		world.load("isotest");
		setScreen(getScreen(ScreenKey.TITLE));
	}

	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void dispose() {
		assets.dispose();
		batch.dispose();
		world.dispose();
	}

	/* Getters and Setters */

	public AssetHandler getAssets() {
		return assets;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	/** Retrieves the screen which the specified key is mapped to */
	public JScreenTemplate getScreen(ScreenKey key) {
		return screens.get(key);
	}

	public World getWorld() {
		return world;
	}

	/** Sets the active screen to the one which the specified key is mapped to */
	public void setScreen(ScreenKey key) {
		setScreen(screens.get(key));
	}
}