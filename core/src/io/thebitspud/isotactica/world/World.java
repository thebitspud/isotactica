package io.thebitspud.isotactica.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import io.thebitspud.isotactica.Isotactica;

public class World {
	private Isotactica game;
	private TiledMap map;
	private TiledMapRenderer mapRenderer;
	private OrthographicCamera mapCamera;

	private int width, height;

	public World(Isotactica game) {
		this.game = game;

		mapCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public void load(String levelName) {
		map = game.getAssets().get(levelName + ".tmx", TiledMap.class);
		mapRenderer = new IsometricTiledMapRenderer(map);
		width = map.getProperties().get("width", Integer.class);
		height = map.getProperties().get("height", Integer.class);

		mapCamera.zoom = 1;
		mapCamera.position.x = 0;
		mapCamera.position.y = 0;
	}

	public void tick(float delta) {
		// worldInput.tick(delta);
		// clampMap();
		mapCamera.update();
	}

	public void render() {
		mapRenderer.setView(mapCamera);
		mapRenderer.render();
	}

	public void dispose() {

	}
}
