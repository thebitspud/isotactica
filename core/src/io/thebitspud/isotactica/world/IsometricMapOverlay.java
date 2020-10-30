package io.thebitspud.isotactica.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.screens.GameScreen;

import java.awt.*;

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
		game.getBatch().begin();
		highlightTiles();
		game.getBatch().end();
	}

	/**
	 * Highlights the tile the player's mouse is currently hovering over.
	 * More will be added soon
	 */

	private void highlightTiles() {
		GameScreen gScreen = ((GameScreen) game.getScreen(Isotactica.ScreenKey.GAME));
		Point coord = getCoordinateFromPointer(Gdx.input.getX(), Gdx.input.getY());
		if (coord == null) {
			gScreen.setTileInfoText("");
			return;
		}

		// displaying the info of the tile being hovered over
		gScreen.setTileInfoText("(" + coord.x + ", " + coord.y + ")");

		// drawing the highlight texture
		Vector2 highlightPos = getPointerPosition(coord);
		game.getBatch().draw(game.getAssets().highlights[0], highlightPos.x, highlightPos.y,
				game.TILE_WIDTH / mapCamera.zoom, game.TILE_HEIGHT / mapCamera.zoom);
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
	public Vector2 getPointerPosition(Point coord) {
		int coordSum = coord.x + coord.y + 1;
		float worldY = game.TILE_HEIGHT * (world.getWidth() - coordSum) / 2f;
		float worldX = game.TILE_WIDTH * (worldY / game.TILE_HEIGHT + coord.y);

		Vector3 worldPos = new Vector3(worldX, worldY, 0);
		Vector3 screenPos = mapCamera.project(worldPos);
		return new Vector2(screenPos.x, screenPos.y);
	}
}
