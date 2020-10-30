package io.thebitspud.isotactica.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.thebitspud.isotactica.Isotactica;

import java.awt.*;

public class IsometricTileSelector {
	private Isotactica game;

	public IsometricTileSelector(Isotactica game) {
		this.game = game;
	}

	/**
	 * Retrieves the selected coordinate based on the pixel position of a given pointer.
	 * x and y should be relative to the top left corner of the map.
	 * @param x the x position of the pointer
	 * @param y the y position of the pointer
	 */
	public Point getCoordinate(float x, float y) {
		int gridX = Math.round(y / game.TILE_HEIGHT + x / game.TILE_WIDTH) - 1;
		int gridY = Math.round(y / game.TILE_HEIGHT - x / game.TILE_WIDTH);

		return new Point(gridX, gridY);
	}

	public Vector2 getPixelPosition(Point coord, OrthographicCamera camera) {
		float pixelY = game.TILE_HEIGHT * (coord.x + coord.y) / 2f;
		float pixelX = game.TILE_WIDTH * (pixelY / game.TILE_HEIGHT - coord.y);

		Vector3 screenPos = new Vector3(pixelX, pixelY, 0);
		screenPos = camera.project(screenPos);
		return new Vector2(screenPos.x, screenPos.y);
	}
}
