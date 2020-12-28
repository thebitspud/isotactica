package io.thebitspud.isotactica.world.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import io.thebitspud.isotactica.Isotactica;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.awt.*;

/**
 * Any non-controllable environmental object
 * This class is outdated and will be updated in the near future
 */

public class MapObject extends Entity {
	/**
	 * An enum of all map objects and their stats
	 * This will be replaced with a more dynamic system in the future
	 */
	public enum ID {
		ROCK (10),
		CRACKED_ROCK (10);

		private final int maxHealth;

		ID(int maxHealth) {
			this.maxHealth = maxHealth;
		}

		public int getMaxHealth() {
			return maxHealth;
		}
	}

	private ID id;

	public MapObject(Point coord, ID id, Isotactica game) {
		super(coord, game.getAssets().mapObjects[id.ordinal()], game);

		this.id = id;

		if (id == ID.CRACKED_ROCK) currentHealth = id.maxHealth / 2;
		else currentHealth = id.maxHealth;

		final String coordText = " at [" + coord.x + ", " + coord.y + "]";
		Gdx.app.log("MapObject spawned", "ID." + id + coordText);
	}

	@Override
	public void render() {
		super.render();
		drawHealthBar();
	}

	/** Draws a dynamic health bar above the unit. */
	private void drawHealthBar() {
		float healthPercent = (float) currentHealth / id.maxHealth * 100;
		float width = getWidth() * getScaleX();
		float height = 2 * getScaleY();
		float xPos = getX() + width / 4;
		float yPos = getY() + (getHeight() - 25) * getScaleY();

		ShapeDrawer drawer = game.getAssets().getDrawer();
		drawer.filledRectangle(xPos, yPos, width / 2, height, Color.BLACK);
		drawer.setColor((100 - healthPercent) / 100f, healthPercent / 100f, 0, 1);
		drawer.filledRectangle(xPos, yPos, width * healthPercent / 200,  height);
	}

	/* Getters and Setters */

	@Override
	public void adjustHealth(int value) {
		currentHealth += value;

		if (currentHealth > id.maxHealth) currentHealth = id.maxHealth;
		else if (currentHealth <= 0) {
			currentHealth = 0;
			active = false;
		} else if (id == ID.ROCK && currentHealth <= 5) {
			id = ID.CRACKED_ROCK;
			setRegion(game.getAssets().mapObjects[id.ordinal()]);
		}
	}

	@Override
	public String getInfo() {
		String healthText = (id.maxHealth > 0) ? "\nHP: " + currentHealth + "/" + id.maxHealth : "";
		return "MapObject." + id + healthText;
	}
}
