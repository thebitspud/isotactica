package io.thebitspud.isotactica.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class WorldInputHandler implements InputProcessor {
	private World world;
	private OrthographicCamera mapCamera;
	private boolean[] keyPressed;
	private boolean leftDown, rightDown;

	public WorldInputHandler(World world) {
		this.world = world;
		mapCamera = world.getMapCamera();

		keyPressed = new boolean[256];
	}

	public void tick(float delta) {
		getCameraInput(delta);
	}

	private void getCameraInput(float delta) {
		int xVel = 0, yVel = 0;

		if (keyPressed[Input.Keys.W] || keyPressed[Input.Keys.UP]) yVel += 500;
		if (keyPressed[Input.Keys.A] || keyPressed[Input.Keys.LEFT]) xVel -= 500;
		if (keyPressed[Input.Keys.S] || keyPressed[Input.Keys.DOWN]) yVel -= 500;
		if (keyPressed[Input.Keys.D] || keyPressed[Input.Keys.RIGHT]) xVel += 500;

		mapCamera.position.x += xVel * delta * mapCamera.zoom;
		mapCamera.position.y += yVel * delta * mapCamera.zoom;
	}

	public void render() {

	}

	@Override
	public boolean keyDown(int keycode) {
		keyPressed[keycode] = true;

		if (keycode == Input.Keys.Q) mapCamera.zoom *= Math.sqrt(2);
		if (keycode == Input.Keys.E) mapCamera.zoom *= Math.sqrt(0.5);

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		keyPressed[keycode] = false;
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		switch (button) {
			case Input.Buttons.LEFT: leftDown = true; break;
			case Input.Buttons.RIGHT: rightDown = true; break;
			default: return false;
		} return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		switch (button) {
			case Input.Buttons.LEFT: leftDown = false; break;
			case Input.Buttons.RIGHT: rightDown = false; break;
			default: return false;
		} return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (Gdx.input.getX() > Gdx.graphics.getWidth() - 143) return false;

		if (rightDown) {
			float x = Gdx.input.getDeltaX() * mapCamera.zoom;
			float y = Gdx.input.getDeltaY() * mapCamera.zoom;

			mapCamera.translate(-x,y);
		}

		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		mapCamera.zoom *= Math.sqrt((amount > 0) ? 2 : 0.5);
		return true;
	}
}
