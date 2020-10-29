package io.thebitspud.isotactica.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.utils.JInputListener;

public class PauseScreen extends JScreenTemplate {
	public PauseScreen(Isotactica game) {
		super(game);
	}

	@Override
	protected void initStageComponents(int width, int height) {
		Label title = new Label("Game Paused", assets.montserrat[0]);
		title.setPosition((width - title.getPrefWidth()) * 0.5f, height * 0.75f);
		stage.addActor(title);

		ImageButton resumeButton = new ImageButton(assets.getButtonStyle(assets.buttons[3]));
		resumeButton.addListener(new JInputListener() {
			@Override
			public void onActivation() {
				game.setScreen(game.getScreen(Isotactica.ScreenKey.GAME));
			}
		});
		resumeButton.setPosition(width * 0.5f - 200, height * 0.55f);
		stage.addActor(resumeButton);

		ImageButton restartButton = new ImageButton(assets.getButtonStyle(assets.buttons[4]));
		restartButton.addListener(new JInputListener() {
			@Override
			public void onActivation() {
				game.setScreen(game.getScreen(Isotactica.ScreenKey.GAME));
				// game.world.init("testlevel.tmx");
			}
		});
		restartButton.setPosition(width * 0.5f - 200, height * 0.35f);
		stage.addActor(restartButton);

		ImageButton quitButton = new ImageButton(assets.getButtonStyle(assets.buttons[5]));
		quitButton.addListener(new JInputListener() {
			@Override
			public void onActivation() {
				game.setScreen(game.getScreen(Isotactica.ScreenKey.TITLE));
			}
		});
		quitButton.setPosition(width * 0.5f - 200, height * 0.15f);
		stage.addActor(quitButton);
	}

	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) game.setScreen(Isotactica.ScreenKey.TITLE);
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) game.setScreen(Isotactica.ScreenKey.GAME);

		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
	}
}
