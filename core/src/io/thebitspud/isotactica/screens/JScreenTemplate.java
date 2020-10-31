package io.thebitspud.isotactica.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.utils.AssetHandler;
import io.thebitspud.isotactica.utils.JInputListener;

/**
 * The screen template that most other screens inherit.
 * Also contains convenience functions for adding actors.
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

	/** Initializes all relevant actors on a screen
	 * @param height the current width of the client window
	 * @param width the current width of the client window
	 */
	protected abstract void initStageComponents(int width, int height);

	/* Actor Creation Utilities */

	/** Generates an empty label according to the specified parameters and adds it to the stage */
	protected Label addLabel(int size, int alignment, float x, float y) {
		Label label = new Label("", assets.montserrat[size]);
		label.setPosition(x, y);
		label.setAlignment(alignment);
		stage.addActor(label);

		return label;
	}

	/** Generates a title label according to the specified parameters and adds it to the stage */
	protected void addTitleLabel(String text, int width, int height) {
		Label title = new Label(text, assets.montserrat[0]);
		title.setPosition(width / 2f, height * 0.8f, Align.center);
		stage.addActor(title);
	}

	/** Generates an ImageButton according to the specified parameters and adds it to the stage */
	protected ImageButton addImageButton(int style, int alignment, float x, float y, JInputListener listener) {
		ImageButton button = new ImageButton(assets.getButtonStyle(assets.buttons[style]));
		button.addListener(listener);
		button.setPosition(x, y, alignment);
		stage.addActor(button);

		return button;
	}

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
