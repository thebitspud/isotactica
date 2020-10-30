package io.thebitspud.isotactica.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.utils.IsometricTileSelector;

import java.awt.*;

public class IsometricMapOverlay {
	private Isotactica game;
	private IsometricTileSelector tileSelector;
	private OrthographicCamera mapCamera;

	public IsometricMapOverlay(Isotactica game, OrthographicCamera mapCamera) {
		this.game = game;
		this.mapCamera = mapCamera;

		tileSelector = new IsometricTileSelector(game);
	}

	public void render() {
		game.getBatch().begin();
		highlightTiles();
		game.getBatch().end();
	}

	private void highlightTiles() {
		Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		Vector3 screenPos = mapCamera.unproject(mousePos);

		Point coord = tileSelector.getCoordinate(screenPos.x, screenPos.y);
		Vector2 highlightPos = tileSelector.getPixelPosition(coord, mapCamera);
		game.getBatch().draw(game.getAssets().highlights[0], highlightPos.x, highlightPos.y,
				game.TILE_WIDTH / mapCamera.zoom, game.TILE_HEIGHT / mapCamera.zoom);
	}
}
