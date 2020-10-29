package io.thebitspud.isotactica.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.utils.JInputListener;

public class TitleScreen extends JScreenTemplate {
	public TitleScreen(Isotactica game) {
		super(game);
	}

	/* Inherited Functions */

	@Override
	protected void initStageComponents(int width, int height) {
		Label title = new Label("Isotactica", assets.montserrat[0]);
		title.setPosition((width - title.getPrefWidth()) * 0.5f, height * 0.75f);
		stage.addActor(title);

		ImageButton playButton = new ImageButton(assets.getButtonStyle(assets.buttons[0]));
		playButton.addListener(new JInputListener() {
			@Override
			public void onActivation() {
				game.setScreen(Isotactica.ScreenKey.GAME);
				// game.world.init("testlevel.tmx");
			}
		});
		playButton.setPosition(width * 0.5f - 200, height * 0.55f);
		stage.addActor(playButton);

		ImageButton quitButton = new ImageButton(assets.getButtonStyle(assets.buttons[5]));
		quitButton.addListener(new JInputListener() {
			@Override
			public void onActivation() {
				Gdx.app.exit();
			}
		});
		quitButton.setPosition(width * 0.5f - 200, height * 0.3f);
		stage.addActor(quitButton);
	}

	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();

		Gdx.gl.glClearColor(0.2f, 0.6f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
	}
}
