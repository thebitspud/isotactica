package io.thebitspud.isotactica.world.entities;

import io.thebitspud.isotactica.Isotactica;

import java.awt.*;

/**
 * Any non-controllable environmental object
 */

public class MapObject extends Entity {
	/**
	 * An enum of all map objects and their stats
	 * This will be replaced with a more dynamic system in the future
	 */
	public enum ID {
		ROCK (10),
		CRACKED_ROCK (5);

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
		currentHealth = id.maxHealth;
	}

	/* Getters and Setters */

	@Override
	public String getInfo() {
		String healthText = (id.maxHealth > 0) ? "\nHP: " + currentHealth + "/" + id.maxHealth : "";
		return "MapObject." + id + healthText;
	}

	public ID getID() {
		return id;
	}
}
