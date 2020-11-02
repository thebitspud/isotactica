package io.thebitspud.isotactica.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.utils.JInputListener;
import io.thebitspud.isotactica.world.World;

public class GameScreen extends JScreenTemplate {
	private InputMultiplexer multiplexer;
	private World world;
	private Label tileInfo, turnInfo;

	public GameScreen(Isotactica game) {
		super(game);

		world = game.getWorld();
		multiplexer = new InputMultiplexer(stage, world.getInput());
	}

	/* Inherited Functions */

	@Override
	protected void initStageComponents(int width, int height) {
		turnInfo = addLabel(2, Align.bottomLeft, 25, 25);
		tileInfo = addLabel(2, Align.topLeft, 25, height - 25);

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

		stage.act();
		world.tick(delta);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		world.render();
		stage.draw();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(multiplexer);
	}

	/* Getters and Setters */

	public void setTileInfoText(String text) {
		tileInfo.setText(text);
	}

	public void setTurnInfoText(String text) {
		turnInfo.setText(text);
	}
}
