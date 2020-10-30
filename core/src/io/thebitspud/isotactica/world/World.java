package io.thebitspud.isotactica.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import io.thebitspud.isotactica.Isotactica;

public class World {
	private Isotactica game;
	private TiledMap map;
	private TiledMapRenderer mapRenderer;
	private IsometricMapOverlay mapOverlay;
	private OrthographicCamera mapCamera;

	private WorldInputHandler input;

	private int width, height;

	public World(Isotactica game) {
		this.game = game;

		mapCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		mapOverlay = new IsometricMapOverlay(game, mapCamera);
		input = new WorldInputHandler(this);
	}

	public void load(String levelName) {
		map = game.getAssets().get(levelName + ".tmx", TiledMap.class);
		mapRenderer = new IsometricTiledMapRenderer(map);
		width = map.getProperties().get("width", Integer.class);
		height = map.getProperties().get("height", Integer.class);

		mapCamera.zoom = 0.5f;
		mapCamera.position.x = 0;
		mapCamera.position.y = 0;
	}

	public void tick(float delta) {
		input.tick(delta);
		clampMapBounds();
		mapCamera.update();
	}

	/**
	 * Keeps camera properties within acceptable bounds
	 */

	private void clampMapBounds() {
		Vector3 pos = mapCamera.position;

		mapCamera.zoom = (float) MathUtils.clamp(mapCamera.zoom, 0.25, 1);

		float pixelWidth = width * game.TILE_WIDTH;
		float pixelHeight = height * game.TILE_HEIGHT;

		pos.x = MathUtils.clamp(pos.x, 0, pixelWidth);
		pos.y = MathUtils.clamp(pos.y, -pixelHeight / 2, pixelHeight / 2);
	}

	public void render() {
		mapRenderer.setView(mapCamera);
		mapRenderer.render();
		mapOverlay.render();
	}

	public void dispose() {
		map.dispose();
	}

	public WorldInputHandler getInput() {
		return input;
	}

	public OrthographicCamera getMapCamera() {
		return mapCamera;
	}
}
