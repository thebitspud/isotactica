package io.thebitspud.isotactica.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.screens.GameScreen;
import io.thebitspud.isotactica.world.entities.Entity;
import io.thebitspud.isotactica.world.entities.Unit;

import java.awt.*;

/**
 * The in-game Tiled map overlay that enables tile selection and highlighting.
 * Also contains functions for retrieving tile coordinates and render position.
 */

public class IsometricMapOverlay {
	private Isotactica game;
	private World world;
	private OrthographicCamera mapCamera;

	public IsometricMapOverlay(Isotactica game, World world) {
		this.game = game;
		this.world = world;
		this.mapCamera = world.getMapCamera();
	}

	public void render() {
		highlightTiles();
	}

	/** Highlights relevant units and tiles. */
	private void highlightTiles() {
		GameScreen gameScreen = ((GameScreen) game.getScreen(Isotactica.ScreenKey.GAME));
		Point coord = getCoordinateFromPointer(Gdx.input.getX(), Gdx.input.getY());

		// Note that the hovered entity and selected entity are not always the same
		Entity hoveredEntity = world.getEntity(coord);
		Entity selectedEntity = world.getInput().getSelectedEntity();

		if (coord == null) gameScreen.setTileInfoText("");
		else {
			// displaying the info of the tile being hovered over
			String coordText = "[" + coord.x + "," + coord.y + "]";
			String tileText = world.getTileID(coord).getTileInfo();
			String entityInfo = (hoveredEntity == null) ? "" : "\n\n" + hoveredEntity.getInfo();
			gameScreen.setTileInfoText(coordText + tileText + entityInfo);

			// drawing the highlight texture
			Point highlightPos = getPointerPosition(coord);
			game.getBatch().draw(game.getAssets().highlights[0], highlightPos.x, highlightPos.y,
					game.TILE_WIDTH / mapCamera.zoom, game.TILE_HEIGHT / mapCamera.zoom);
		}

		 // Highlighting the currently selected unit
		if (selectedEntity != null) {
			int index = 3;

			if (selectedEntity.getClass() == Unit.class) {
				Unit selectedUnit = (Unit) selectedEntity;
				if (selectedUnit.getPlayer().equals(world.getUser())) {
					if (selectedUnit.actionAvailable()) {
						index = 2;
						selectedUnit.drawAvailableMoves();
					}
				} else index = 4;
			}

			game.getBatch().draw(game.getAssets().highlights[index], selectedEntity.getX(), selectedEntity.getY(),
					game.TILE_WIDTH / mapCamera.zoom, game.TILE_HEIGHT / mapCamera.zoom);
		}
	}

	/**
	 * Retrieves the selected coordinate based on the pixel position of a given pointer.
	 * Returns (-1, -1) if the coordinate is out of map bounds.
	 * @param x the x-position of the pointer
	 * @param y the y-position of the pointer
	 */
	public Point getCoordinateFromPointer(float x, float y) {
		Vector3 screenPos = new Vector3(x, y, 0);
		Vector3 worldPos = mapCamera.unproject(screenPos);

		float scaledX = worldPos.x / game.TILE_WIDTH;
		float scaledY = worldPos.y / game.TILE_HEIGHT;
		int gridX = world.getWidth() - Math.round(scaledY + scaledX);
		int gridY = -Math.round(scaledY - scaledX);

		if (gridX < 0 || gridX >= world.getWidth() || gridY < 0 || gridY >= world.getHeight()) return null;
		return new Point(gridX, gridY);
	}

	/**
	 * Retrieves the exact pixel location (relative to the screen) of a given coordinate.
	 * @param coord the grid coordinate of the given tile
	 */
	public Point getPointerPosition(Point coord) {
		int coordSum = coord.x + coord.y + 1;
		float worldY = game.TILE_HEIGHT * (world.getWidth() - coordSum) / 2f;
		float worldX = game.TILE_WIDTH * (worldY / game.TILE_HEIGHT + coord.y);

		Vector3 worldPos = new Vector3(worldX, worldY, 0);
		Vector3 screenPos = mapCamera.project(worldPos);
		return new Point(Math.round(screenPos.x), Math.round(screenPos.y));
	}
}
