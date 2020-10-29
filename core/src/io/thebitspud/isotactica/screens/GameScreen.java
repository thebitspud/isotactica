package io.thebitspud.isotactica.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.utils.JInputListener;

public class GameScreen extends JScreenTemplate {
	public GameScreen(Isotactica game) {
		super(game);
	}

	@Override
	protected void initStageComponents(int width, int height) {
		ImageButton pauseButton = new ImageButton(assets.getButtonStyle(assets.buttons[14]));
		pauseButton.addListener(new JInputListener() {
			@Override
			public void onActivation() {
				game.setScreen(Isotactica.ScreenKey.PAUSE);
			}
		});
		pauseButton.setPosition(width - 115, height - 115);
		stage.addActor(pauseButton);

		ImageButton endTurnButton = new ImageButton(assets.getButtonStyle(assets.buttons[11]));
		endTurnButton.addListener(new JInputListener() {
			@Override
			public void onActivation() {
				// world.nextPlayer();
			}
		});
		endTurnButton.setPosition(width - 115, 25);
		stage.addActor(endTurnButton);
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
