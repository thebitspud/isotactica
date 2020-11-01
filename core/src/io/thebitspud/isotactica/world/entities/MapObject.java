package io.thebitspud.isotactica.world.entities;

import io.thebitspud.isotactica.Isotactica;

public class MapObject extends Entity {
	public enum ID {
		ROCK (5);

		private final int maxHealth;

		ID(int maxHealth) {
			this.maxHealth = maxHealth;
		}

		public int getMaxHealth() {
			return maxHealth;
		}
	}

	private Unit.ID id;

	public MapObject(int x, int y, ID id, Isotactica game) {
		super(x, y, game.getAssets().mapObjects[id.ordinal()], game);
	}

	public Unit.ID getId() {
		return id;
	}
}
