package io.thebitspud.isotactica.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.utils.AssetHandler;

/**
 * The screen template that most other screens inherit
 * Also contains some convenience functions for adding certain stage components
 */

public abstract class JScreenTemplate implements Screen {
	protected Isotactica game;
	protected Stage stage;
	protected OrthographicCamera camera;
	protected AssetHandler assets;

	public JScreenTemplate(Isotactica game) {
		this.game = game;

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage = new Stage(new ScreenViewport(camera));
		assets = game.getAssets();

		initStageComponents(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	protected abstract void initStageComponents(int width, int height);

	/* Inherited Functions */

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void resize(int width, int height) {
		initStageComponents(width, height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
