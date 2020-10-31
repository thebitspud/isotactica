package io.thebitspud.isotactica.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import io.thebitspud.isotactica.Isotactica;

/**
 * The game world in which combat encounters are processed and resolved.
 * Consists of a Tiled map, player-controlled camera, and map overlay.
 */

public class World {
	private Isotactica game;
	private IsometricMapOverlay mapOverlay;
	private WorldInputHandler input;

	private TiledMap map;
	private TiledMapRenderer mapRenderer;
	private OrthographicCamera mapCamera;

	private int width, height;

	public World(Isotactica game) {
		this.game = game;

		mapCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		mapOverlay = new IsometricMapOverlay(game, this);
		input = new WorldInputHandler(this);
	}

	public void load(String levelName) {
		map = game.getAssets().get(levelName + ".tmx", TiledMap.class);
		mapRenderer = new IsometricTiledMapRenderer(map);
		width = map.getProperties().get("width", Integer.class);
		height = map.getProperties().get("height", Integer.class);

		mapCamera.zoom = 0.5f;
		mapCamera.position.x = width * game.TILE_WIDTH / 2f;
		mapCamera.position.y = 0;
	}

	public void tick(float delta) {
		input.tick(delta);
		clampMapBounds();
		mapCamera.update();
	}

	public void render() {
		mapRenderer.setView(mapCamera);
		mapRenderer.render();
		mapOverlay.render();
	}

	public void dispose() {
		map.dispose();
	}

	/* Utility Functions */

	/** Keeps the camera's view properties within acceptable bounds */
	private void clampMapBounds() {
		Vector3 pos = mapCamera.position;

		// Containing the camera's zoom
		mapCamera.zoom = (float) MathUtils.clamp(mapCamera.zoom, 0.25, 1);

		int pixelWidth = width * game.TILE_WIDTH;
		int pixelHeight = height * game.TILE_HEIGHT;
		float clampedX = MathUtils.clamp(pos.x, 0, pixelWidth);
		float clampedY = MathUtils.clamp(pos.y, -pixelHeight / 2f, pixelHeight / 2f);

		// Rounding the camera's position to fit integer pixel offsets based on the camera's zoom
		pos.x = Math.round(clampedX / mapCamera.zoom) * mapCamera.zoom;
		pos.y = Math.round(clampedY / mapCamera.zoom) * mapCamera.zoom;
	}

	/** Retrieves the ground tile (if any) corresponding to the given coordinates */
	public TiledMapTile getTile(int x, int y) {
		// Note: x and y must be switched for some reason
		int adjX = MathUtils.clamp(y, 0, height - 1);
		int adjY = width - 1 - MathUtils.clamp(x, 0, width - 1);

		TiledMapTileLayer.Cell cell = ((TiledMapTileLayer) map.getLayers().get("Ground")).getCell(adjX, adjY);
		if (cell != null) return cell.getTile();
		else return null;
	}


	public void setTile(int x, int y, TileID id) {
		getTile(x, y).setId(id.getIndex() == 7 ? id.getIndex() : 8);
	}

	/** Retrieves the TileID corresponding to the given tile */
	public TileID getTileID(TiledMapTile tile) {
		int index = tile.getId();
		if (index >= 8) index = 7;
		return TileID.values()[index];
	}

	/** Retrieves the TileID corresponding to the given coordinates */
	public TileID getTileID(int x, int y) {
		if(getTile(x, y) != null) return getTileID(getTile(x, y));
		else return TileID.VOID;
	}

	/* Getters and Setters */

	public WorldInputHandler getInput() {
		return input;
	}

	public OrthographicCamera getMapCamera() {
		return mapCamera;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
