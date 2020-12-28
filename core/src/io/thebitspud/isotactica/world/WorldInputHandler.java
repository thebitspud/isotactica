package io.thebitspud.isotactica.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.world.entities.Entity;
import io.thebitspud.isotactica.world.entities.Unit;

import java.awt.*;

/**
 * A utility class that handles input events for the world map
 */

public class WorldInputHandler implements InputProcessor {
	private Isotactica game;
	private World world;
	private OrthographicCamera mapCamera;
	private boolean[] keyPressed;
	private boolean leftDown, rightDown;
	private Entity selectedEntity;

	public WorldInputHandler(Isotactica game, World world) {
		this.game = game;
		this.world = world;
		mapCamera = world.getMapCamera();

		keyPressed = new boolean[256];
	}

	public void init() {
		selectedEntity = null;
	}

	public void tick(float delta) {
		getCameraInput(delta);
		clampMapBounds();
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

	/** Keeps the camera's view properties within acceptable bounds */
	private void clampMapBounds() {
		Vector3 pos = mapCamera.position;

		// Containing the camera's zoom
		mapCamera.zoom = (float) MathUtils.clamp(mapCamera.zoom, 0.25, 1);

		int pixelWidth = world.getWidth() * game.TILE_WIDTH;
		int pixelHeight = world.getHeight() * game.TILE_HEIGHT;
		float clampedX = MathUtils.clamp(pos.x, 0, pixelWidth);
		float clampedY = MathUtils.clamp(pos.y, -pixelHeight / 2f, pixelHeight / 2f);

		// Rounding the camera's position to fit integer pixel offsets based on the camera's zoom
		pos.x = Math.round(clampedX / mapCamera.zoom) * mapCamera.zoom;
		pos.y = Math.round(clampedY / mapCamera.zoom) * mapCamera.zoom;
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
		if (button == Input.Buttons.LEFT) {
			Point coord = world.getMapOverlay().getCoordinateFromPointer(screenX, screenY);
			Entity hoveredEntity = world.getEntityManager().getEntity(coord);

			if (selectedEntity == null) selectedEntity = hoveredEntity;
			else if (hoveredEntity != null) {
				if (selectedEntity == hoveredEntity) selectedEntity = null;
				else if (selectedEntity instanceof Unit) {
					Unit selectedUnit = (Unit) selectedEntity;
					if (selectedUnit.getPlayer() == world.getUser() && selectedUnit.canAttackEntity(hoveredEntity))
						selectedUnit.attack(hoveredEntity);
					else selectedEntity = hoveredEntity;
				} else selectedEntity = hoveredEntity;
			} else if (selectedEntity instanceof Unit) {
				Unit selectedUnit = (Unit) selectedEntity;
				if (selectedUnit.getPlayer() == world.getUser() && selectedUnit.canMoveToTile(coord)) {
					selectedUnit.move(coord);
					if (!selectedUnit.actionAvailable()) selectedEntity = null;
				} else selectedEntity = null;
			} else selectedEntity = null;

			return leftDown = true;
		}

		if (button == Input.Buttons.RIGHT) {
			return rightDown = true;
		}

		return false;
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

	public Entity getSelectedEntity() {
		return selectedEntity;
	}

	public void deselectUnit() {
		selectedEntity = null;
	}
}
