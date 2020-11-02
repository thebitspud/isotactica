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
import io.thebitspud.isotactica.world.entities.Entity;
import io.thebitspud.isotactica.world.entities.MapObject;
import io.thebitspud.isotactica.world.entities.Unit;

import java.awt.*;
import java.util.ArrayList;

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
	TiledMapTileLayer groundLayer;
	private OrthographicCamera mapCamera;

	private int width, height;

	// EXPERIMENTAL
	private ArrayList<Entity> entities;

	public World(Isotactica game) {
		this.game = game;

		mapCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		mapOverlay = new IsometricMapOverlay(game, this);
		input = new WorldInputHandler(this);

		entities = new ArrayList<>();
	}

	public void load(String levelName) {
		map = game.getAssets().loadMap(levelName);
		mapRenderer = new IsometricTiledMapRenderer(map);
		groundLayer = (TiledMapTileLayer) map.getLayers().get("Ground");
		width = map.getProperties().get("width", Integer.class);
		height = map.getProperties().get("height", Integer.class);

		mapCamera.zoom = 0.5f;
		mapCamera.position.x = width * game.TILE_WIDTH / 2f;
		mapCamera.position.y = 0;

		entities.clear();
		entities.add(new Unit(4, 2, Unit.ID.WARRIOR, game));
		entities.add(new MapObject(3, 6, MapObject.ID.ROCK, game));
	}

	public void tick(float delta) {
		input.tick(delta);
		clampMapBounds();
		mapCamera.update();
		for (Entity e: entities) e.tick(delta);
	}

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

	public void render() {
		mapRenderer.setView(mapCamera);
		mapRenderer.render();

		game.getBatch().begin();
		mapOverlay.render();
		for (Entity e: entities) e.render();
		game.getBatch().end();
	}

	public void dispose() {
		map.dispose();
	}

	/* Data Retrieval Functions */

	/** Retrieves the ground tile (if any) corresponding to the given coordinates */
	public TiledMapTile getTile(Point coord) {
		// Note: x and y must be switched for some reason
		int adjX = MathUtils.clamp(coord.y, 0, height - 1);
		int adjY = width - MathUtils.clamp(coord.x, 0, width - 1) - 1;

		TiledMapTileLayer.Cell cell = groundLayer.getCell(adjX, adjY);
		if (cell != null) return cell.getTile();
		else return null;
	}

	// USAGE: if (world.getTileID(coord) != null) world.setTile(coord, TileID.BARREN);
	/** Sets the tile at the given coordinates to match the specified TileID */
	public void setTile(Point coord, TileID id) {
		int adjX = MathUtils.clamp(coord.y, 0, height - 1);
		int adjY = width - MathUtils.clamp(coord.x, 0, width - 1) - 1;

		TiledMapTileLayer.Cell cell = groundLayer.getCell(adjX, adjY);
		if (cell == null) groundLayer.setCell(adjX, adjY, cell = new TiledMapTileLayer.Cell());
		cell.setTile(map.getTileSets().getTile(id.getIndex() == 6 ? 7 : id.getIndex()));
	}

	/** Retrieves the TileID corresponding to the given tile */
	public TileID getTileID(TiledMapTile tile) {
		int index = tile.getId();
		if (index >= 8) index = 7;
		return TileID.values()[index];
	}

	/** Retrieves the TileID corresponding to the given coordinates */
	public TileID getTileID(Point coord) {
		if (getTile(coord) != null) return getTileID(getTile(coord));
		else return TileID.VOID;
	}

	public Entity getUnit(Point coord) {
		for (Entity e: entities)
			if (e.getCoord().equals(coord))
				return e;
		return null;
	}

	/* Getters and Setters */

	public WorldInputHandler getInput() {
		return input;
	}

	public OrthographicCamera getMapCamera() {
		return mapCamera;
	}

	public IsometricMapOverlay getMapOverlay() {
		return mapOverlay;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
