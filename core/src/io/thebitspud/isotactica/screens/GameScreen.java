package io.thebitspud.isotactica.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Align;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.utils.JInputListener;

public class GameScreen extends JScreenTemplate {
	public GameScreen(Isotactica game) {
		super(game);
	}

	/* Inherited Functions */

	@Override
	protected void initStageComponents(int width, int height) {
		addImageButton(14, Align.topRight, width - 25, height - 25, new JInputListener() {
			@Override
			public void onActivation() {
				game.setScreen(Isotactica.ScreenKey.PAUSE);
			}
		});
		addImageButton(11, Align.bottomRight, width - 25, 25, new JInputListener() {
			@Override
			public void onActivation() {
				// world.nextPlayer();
			}
		});
	}

	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) game.setScreen(Isotactica.ScreenKey.PAUSE);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
	}
}
