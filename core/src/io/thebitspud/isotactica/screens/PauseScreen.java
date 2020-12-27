package io.thebitspud.isotactica.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Align;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.utils.JInputListener;

/**
 * The screen that displays the game's pause menu
 */

public class PauseScreen extends JScreenTemplate {
	public PauseScreen(Isotactica game) {
		super(game);
	}

	/* Inherited Functions */

	@Override
	protected void initStageComponents(int width, int height) {
		addTitleLabel("Game Paused", width, height);

		addImageButton(3, Align.center, width * 0.5f, height * 0.6f, new JInputListener() {
			@Override
			public void onActivation() {
				game.setScreen(game.getScreen(Isotactica.ScreenKey.GAME));
			}
		});
		addImageButton(4, Align.center, width * 0.5f, height * 0.4f, new JInputListener() {
			@Override
			public void onActivation() {
				game.setScreen(game.getScreen(Isotactica.ScreenKey.GAME));
				game.getWorld().load("isotest");
			}
		});
		addImageButton(5, Align.center, width * 0.5f, height * 0.2f, new JInputListener() {
			@Override
			public void onActivation() {
				game.setScreen(game.getScreen(Isotactica.ScreenKey.TITLE));
			}
		});
	}

	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) game.setScreen(Isotactica.ScreenKey.TITLE);
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) game.setScreen(Isotactica.ScreenKey.GAME);

		stage.act();

		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.draw();
	}
}
