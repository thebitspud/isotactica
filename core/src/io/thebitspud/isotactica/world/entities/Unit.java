package io.thebitspud.isotactica.world.entities;

import com.badlogic.gdx.graphics.Color;
import io.thebitspud.isotactica.Isotactica;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Unit extends Entity {
	public enum ID {
		WARRIOR (10, 2);

		private final int maxHealth, agility;

		ID(int maxHealth, int agility) {
			this.maxHealth = maxHealth;
			this.agility = agility;
		}

		public int getMaxHealth() {
			return maxHealth;
		}

		public int getAgility() {
			return agility;
		}
	}

	private ID id;
	private int currentHealth;
	private boolean canMove, canAct;

	public Unit(int x, int y, ID id, Isotactica game) {
		super(x, y, game.getAssets().units[id.ordinal()], game);

		this.id = id;
		currentHealth = id.maxHealth;

		nextTurn();
	}

	@Override
	public void render() {
		if (canMove || canAct) {
			float scale = game.TILE_HEIGHT / world.getMapCamera().zoom;
			game.getBatch().draw(game.getAssets().highlights[4], getX(), getY() + scale, scale * 2, scale);
		}

		super.render();
		drawHealthBar();
	}

	/**
	 * Draws a dynamic health bar above the unit.
	 */
	private void drawHealthBar() {
		float healthPercent = (float) currentHealth / id.maxHealth * 100;
		float width = getWidth() * getScaleX();
		float height = 2 * getScaleY();
		float xPos = getX() + width / 4;
		float yPos = getY() + (getHeight() - 10) * getScaleY();

		ShapeDrawer drawer = game.getAssets().getDrawer();
		drawer.filledRectangle(xPos, yPos, width / 2, height, Color.BLACK);
		drawer.setColor((100 - healthPercent) / 100f, healthPercent / 100f, 0, 1);
		drawer.filledRectangle(xPos, yPos, width * healthPercent / 200,  height);
	}

	/* Unit Action Functions */

	private void move() {
		canMove = false;
	}

	private void act() {
		canMove = false;
		canAct = false;
	}

	public void nextTurn() {
		canAct = true;
		canMove = true;
	}

	public void adjustHealth(int value) {
		currentHealth += value;

		if (currentHealth > id.maxHealth) currentHealth = id.maxHealth;
		else if (currentHealth <= 0) {
			currentHealth = 0;
			active = false;
		}
	}

	/* Getters and Setters */

	@Override
	public String getInfo() {
		String healthText = "\nHP: " + currentHealth + "/" + id.maxHealth;
		String statsText = "\nAgility: " + id.agility;
		return "Unit." + id + healthText + statsText;
	}

	public boolean moveAvailable() {
		return canMove;
	}

	public boolean actionAvailable() {
		return canAct;
	}

	public ID getID() {
		return id;
	}
}
