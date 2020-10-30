package io.thebitspud.isotactica.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Align;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.utils.JInputListener;
import io.thebitspud.isotactica.world.World;

public class GameScreen extends JScreenTemplate {
	private InputMultiplexer multiplexer;
	private World world;

	public GameScreen(Isotactica game) {
		super(game);

		multiplexer = new InputMultiplexer(stage);
		world = game.getWorld();
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

		world.tick(delta);
		world.render();

		stage.act();
		stage.draw();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(multiplexer);
	}
}