package io.thebitspud.isotactica.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.players.*;
import io.thebitspud.isotactica.screens.GameScreen;
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
	private TiledMapTileLayer groundLayer;
	private OrthographicCamera mapCamera;

	private int width, height, gameTurn, maxTurns, currentPlayerIndex;

	private ArrayList<Player> players;
	private User user;
	private ArrayList<MapObject> mapObjects;

	public World(Isotactica game) {
		this.game = game;

		mapCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		mapOverlay = new IsometricMapOverlay(game, this);
		input = new WorldInputHandler(game, this);

		mapObjects = new ArrayList<>();
		players = new ArrayList<>();
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

		players.clear();
		players.add(user = new User(game));
		players.add(new EnemyAI(game));
		mapObjects.add(new MapObject(new Point(3, 4), MapObject.ID.ROCK, game));
		mapObjects.add(new MapObject(new Point(4, 4), MapObject.ID.CRACKED_ROCK, game));

		gameTurn = 1;
		maxTurns = -1;
		currentPlayerIndex = -1;

		nextPlayer();
		updateTurnInfo();
	}

	public void tick(float delta) {
		input.tick(delta);
		mapCamera.update();
		for (Player p: players) p.tick(delta);
		for (MapObject o: mapObjects) o.tick(delta);
	}

	public void render() {
		mapRenderer.setView(mapCamera);
		mapRenderer.render();

		game.getBatch().begin();
		mapOverlay.render();
		for (Player p: players) p.render();
		for (MapObject o: mapObjects) o.render();
		game.getBatch().end();
	}

	public void dispose() {
		map.dispose();
	}

	/* Turn Management Functions */

	/**
	 * Ends the current player's turn and starts the next player's turn.
	 * Starts the next round if all players have completed their turns.
	 */
	public void nextPlayer() {
		currentPlayerIndex++;

		// Starting the next round
		if (currentPlayerIndex >= players.size()) {
			currentPlayerIndex = 0;
			gameTurn += 1;
			input.deselectUnit();
			updateTurnInfo();
		}

		players.get(currentPlayerIndex).playTurn();
	}

	/** Updates the info that players see */
	public void updateTurnInfo() {
		GameScreen gameScreen = (GameScreen) game.getScreen(Isotactica.ScreenKey.GAME);
		String turnText = "\nTurn " + gameTurn + ((maxTurns > 0) ? "/" + maxTurns : "");

		gameScreen.setTurnInfoText(turnText);
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

	public Entity getEntity(Point coord) {
		for (Player p: players)
			for (Unit u: p.getUnits())
				if (u.getCoord().equals(coord))
					return u;

		for (MapObject o: mapObjects)
			if (o.getCoord().equals(coord))
				return o;

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

	public User getUser() {
		return user;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
