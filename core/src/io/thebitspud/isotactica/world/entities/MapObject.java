package io.thebitspud.isotactica.world.entities;

import io.thebitspud.isotactica.Isotactica;

import java.awt.*;

/**
 * Any non-controllable environmental object
 */

public class MapObject extends Entity {
	/** An enum of all map objects and their stats */
	public enum ID {
		ROCK (10, false),
		CRACKED_ROCK (10, false);

		private final int maxHealth;
		private final boolean pushable;

		ID(int maxHealth, boolean pushable) {
			this.maxHealth = maxHealth;
			this.pushable = pushable;
		}
	}

	private ID id;

	public MapObject(Point coord, ID id, Isotactica game) {
		super(coord, game.getAssets().mapObjects[id.ordinal()], game, id.pushable);

		this.id = id;

		if (id == ID.CRACKED_ROCK) currentHealth = id.maxHealth / 2;
		else currentHealth = id.maxHealth;
		maxHealth = id.maxHealth;
	}

	/* Getters and Setters */

	@Override
	public void adjustHealth(int value) {
		super.adjustHealth(value);

		if (currentHealth <= 5 && id == ID.ROCK) {
			id = ID.CRACKED_ROCK;
			setRegion(game.getAssets().mapObjects[id.ordinal()]);
		}
	}

	@Override
	public ID getID() {
		return id;
	}

	@Override
	public String getIDText() {
		return "MapObject." + id;
	}

	@Override
	public String getInfoText() {
		String healthText = (id.maxHealth > 0) ? "\nHP: " + currentHealth + "/" + id.maxHealth : "";
		String pushableText = "\nPushable: " + (pushable ? "TRUE" : "FALSE");
		return getIDText() + healthText + "\nPushable: " + pushableText;
	}
}
