package io.thebitspud.isotactica.world.entities;

import com.badlogic.gdx.Gdx;
import io.thebitspud.isotactica.Isotactica;

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
	}

	private ID id;

	public MapObject(Point coord, ID id, Isotactica game) {
		super(coord, game.getAssets().mapObjects[id.ordinal()], game);

		this.id = id;

		if (id == ID.CRACKED_ROCK) currentHealth = id.maxHealth / 2;
		else currentHealth = id.maxHealth;
		maxHealth = id.maxHealth;

		String coordText = " at [" + coord.x + ", " + coord.y + "]";
		Gdx.app.log("Entity spawned", getIDText() + coordText);
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
	public String getInfo() {
		String healthText = (id.maxHealth > 0) ? "\nHP: " + currentHealth + "/" + id.maxHealth : "";
		return getIDText() + healthText;
	}

	@Override
	public String getIDText() {
		return "MapObject." + id;
	}
}
