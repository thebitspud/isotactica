package io.thebitspud.isotactica.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.thebitspud.isotactica.Isotactica;
import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * An extension of the AssetManager class that loads and assigns all asset files
 */

public class AssetHandler extends AssetManager {
	private Isotactica game;
	private ShapeDrawer drawer;

	private TextureRegion pixel;
	public TextureRegion[] highlights, units, mapObjects;
	public TextureRegionDrawable[][] buttons;
	public Label.LabelStyle[] montserrat;

	public AssetHandler(Isotactica game) {
		this.game = game;

		buttons = new TextureRegionDrawable[16][3];
		montserrat = new Label.LabelStyle[4];
		highlights = new TextureRegion[8];
		units = new TextureRegion[8];
		mapObjects =  new TextureRegion[8];
	}

	/* Loader Functions */

	/** Loads and assigns all game assets in the specified order */
	public void loadAll() {
		loadFiles();
		generateFonts();

		finishLoading();
		assignTextures();
		assignAudio();

		Gdx.app.log("Assets loaded successfully", "");
	}

	private void loadFiles() {
		load("buttons.png", Texture.class);
		load("common_iso.png", Texture.class);
		load("highlights.png", Texture.class);
		load("mapObjects.png", Texture.class);
		load("pixel.png", Texture.class);
		load("units.png", Texture.class);

		setLoader(TiledMap.class, new TmxMapLoader());
	}

	private String lastLevel;

	/**
	 * Loads an incoming Tiled map and unloads the last one
	 * @param levelName the file name of the level without the file extension
	 * @return the Tiled map that was just loaded
	 */
	public TiledMap loadMap(String levelName) {
		if (lastLevel != null) {
			game.getAssets().unload(lastLevel + ".tmx");
			Gdx.app.log("Map unloaded", lastLevel + ".tmx");
		}

		lastLevel = levelName;
		game.getAssets().load(levelName + ".tmx", TiledMap.class);
		game.getAssets().finishLoading();
		Gdx.app.log("Map loaded", levelName + ".tmx");

		return game.getAssets().get(levelName + ".tmx", TiledMap.class);
	}

	private void generateFonts() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Montserrat-Regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.incremental = true;

		parameter.size = 96;
		montserrat[0] = new Label.LabelStyle(generator.generateFont(parameter), Color.WHITE);
		parameter.size = 48;
		montserrat[1] = new Label.LabelStyle(generator.generateFont(parameter), Color.WHITE);
		parameter.size = 24;
		montserrat[2] = new Label.LabelStyle(generator.generateFont(parameter), Color.WHITE);
		parameter.size = 14;
		montserrat[3] = new Label.LabelStyle(generator.generateFont(parameter), Color.WHITE);

		generator.dispose();
	}

	private void assignTextures() {
		pixel = new TextureRegion(this.get("pixel.png", Texture.class));
		drawer = new ShapeDrawer(game.getBatch(), pixel);

		// Retrieving sheets
		final Texture buttonSheet = this.get("buttons.png", Texture.class);
		final Texture highlightSheet = this.get("highlights.png", Texture.class);
		final Texture unitSheet = this.get("units.png", Texture.class);
		final Texture mapObjectSheet = this.get("mapObjects.png", Texture.class);

		// Assigning textures
		for (int i = 0; i < 15; i++) {
			if (i < 6) buttons[i] = getButton(buttonSheet, 0, i * 90, 400, 90);
			else buttons[i] = getButton(buttonSheet, 1200, (i - 6) * 90, 90, 90);
		}
		buttons[15] = getButton(buttonSheet, 0, 710, 90, 90);

		for (int i = 0; i < highlights.length; i++) {
			highlights[i] = new TextureRegion(highlightSheet, i * 64, 0, 64, 32);
		}

		for (int i = 0; i < units.length; i++) {
			units[i] = new TextureRegion(unitSheet, i % 4 * 64, i / 4 * 64, 64, 64);
		}

		for (int i = 0; i < mapObjects.length; i++) {
			mapObjects[i] = new TextureRegion(mapObjectSheet, i % 4 * 64, i / 4 * 64, 64, 64);
		}
	}

	private void assignAudio() {

	}

	/* Asset Retrieval Functions */

	private TextureRegionDrawable[] getButton(Texture sheet, int x, int y, int width, int height) {
		final TextureRegion iconUp = new TextureRegion(sheet, x, y, width, height);
		final TextureRegion iconHover = new TextureRegion(sheet, x + width, y, width, height);
		final TextureRegion iconDown = new TextureRegion(sheet, x + width * 2, y, width, height);

		TextureRegionDrawable[] button = new TextureRegionDrawable[3];

		button[0] = new TextureRegionDrawable(iconUp);
		button[1] = new TextureRegionDrawable(iconHover);
		button[2] = new TextureRegionDrawable(iconDown);

		return button;
	}

	public ImageButton.ImageButtonStyle getButtonStyle(TextureRegionDrawable[] button) {
		ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();

		style.imageUp = button[0];
		style.imageOver = button[1];
		style.imageDown = button[2];

		return style;
	}

	public ShapeDrawer getDrawer() {
		return drawer;
	}
}
