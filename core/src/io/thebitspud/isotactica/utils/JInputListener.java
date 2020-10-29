package io.thebitspud.isotactica.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * An extension of InputListener that calls <em>onActivation()</em> when clicked
 */

public abstract class JInputListener extends InputListener implements Activatable {
	public boolean down = false;

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		down = true;
		return true;
	}

	@Override
	public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
		down = false;
	}

	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		if (this.down) onActivation();
	}
}