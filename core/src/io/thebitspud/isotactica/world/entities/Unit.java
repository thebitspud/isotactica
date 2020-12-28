package io.thebitspud.isotactica.world.entities;

import com.badlogic.gdx.graphics.Color;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.players.Player;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.awt.*;
import java.util.HashMap;

/**
 * Any controllable unit that can move and act as directed by a player
 */

public class Unit extends Entity {
	/**
	 * An enum of all game units and their stats
	 * This will be replaced with a more dynamic system in the future
	 */
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
	private Player player;

	private boolean canMove, canAct;
	private HashMap<Point, Integer> moves;

	public Unit(Point coord, ID id, Player player, Isotactica game) {
		super(coord, game.getAssets().units[id.ordinal()], game);

		this.id = id;
		this.player = player;
		currentHealth = id.maxHealth;
		moves = new HashMap<>();

		nextTurn();
	}

	@Override
	public void render() {
		if (canMove || canAct) {
			float scale = game.TILE_HEIGHT / world.getMapCamera().zoom;
			game.getBatch().draw(game.getAssets().highlights[5], getX(), getY() + scale, scale * 2, scale);
		}

		super.render();
		drawHealthBar();
	}

	/** Draws a dynamic health bar above the unit. */
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

	/** Resets the unit's movement and action tokens and begins its next turn */
	public void nextTurn() {
		canAct = true;
		canMove = true;
		findMoves();
	}

	/* Movement-Related Functions */

	/** Highlights all positions this unit can currently move to */
	public void drawAvailableMoves() {
		float scale = game.TILE_HEIGHT / world.getMapCamera().zoom;

		for (Point move: moves.keySet()) {
			Point drawPos = world.getMapOverlay().getPointerPosition(move);
			game.getBatch().draw(game.getAssets().highlights[1], drawPos.x, drawPos.y, scale * 2, scale);
		}
	}

	/** Attempts to move this unit to the specified grid location */
	public void move(Point coord) {
		if (!canMoveToTile(coord)) return;

		canMove = false;
		canAct = false;
		this.coord = coord;
		entityManager.requireSort();
		player.assessActions();
	}

	/** Checks whether this unit can be moved to the specified grid location */
	public boolean canMoveToTile(Point coord) {
		if (!canMove) return false;
		return moves.containsKey(coord);
	}

	/** Uses a modified flood fill algorithm to determine which grid locations the unit can move to. */
	public void findMoves() {
		moves.clear();
		if (!canMove) return;
		findMoves(coord, id.agility);
	}

	private void findMoves(Point coord, int movesLeft) {
		if (!player.tileAvailable(coord) && movesLeft < id.agility) return;
		if (moves.containsKey(coord) && moves.get(coord) >= movesLeft) return;

		moves.put(coord, movesLeft);
		if (movesLeft <= 0) return;

		findMoves(new Point(coord.x + 1, coord.y), movesLeft - 1);
		findMoves(new Point(coord.x - 1, coord.y), movesLeft - 1);
		findMoves(new Point(coord.x, coord.y + 1), movesLeft - 1);
		findMoves(new Point(coord.x, coord.y - 1), movesLeft - 1);
	}

	/* Action-Related Functions */

	private void act() {
		canMove = false;
		canAct = false;
	}

	/** Increments or decrements the unit's health by the specified value */
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

	public Player getPlayer() {
		return player;
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
