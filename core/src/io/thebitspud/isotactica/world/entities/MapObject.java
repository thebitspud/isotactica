package io.thebitspud.isotactica.world.entities;

import io.thebitspud.isotactica.Isotactica;

public class MapObject extends Entity {
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

	public MapObject(int x, int y, ID id, Isotactica game) {
		super(x, y, game.getAssets().mapObjects[id.ordinal()], game);

		this.id = id;
	}

	/* Getters and Setters */

	@Override
	public String getInfo() {
		return "MapObject." + id;
	}

	public ID getID() {
		return id;
	}
}
