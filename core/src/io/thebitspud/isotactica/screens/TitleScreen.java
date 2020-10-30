package io.thebitspud.isotactica.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Align;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.utils.JInputListener;

public class TitleScreen extends JScreenTemplate {
	public TitleScreen(Isotactica game) {
		super(game);
	}

	/* Inherited Functions */

	@Override
	protected void initStageComponents(int width, int height) {
		addTitleLabel("Isotactica", width, height);

		addImageButton(0, Align.center, width * 0.5f, height * 0.55f, new JInputListener() {
			@Override
			public void onActivation() {
				game.setScreen(Isotactica.ScreenKey.GAME);
				// game.world.init("testlevel.tmx");
			}
		});
		addImageButton(5, Align.center, width * 0.5f, height * 0.3f, new JInputListener() {
			@Override
			public void onActivation() {
				Gdx.app.exit();
			}
		});
	}

	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) game.setScreen(Isotactica.ScreenKey.GAME);
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();

		Gdx.gl.glClearColor(0.2f, 0.6f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
	}
}
