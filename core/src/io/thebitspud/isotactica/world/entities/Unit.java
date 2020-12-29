package io.thebitspud.isotactica.world.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.players.Player;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.awt.*;
import java.util.ArrayList;
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
		WARRIOR (10, 2, 1, 2),
		GOBLIN_BRUTE (15, 2, 1, 1);

		private final int maxHealth, agility, range, attack;

		ID(int maxHealth, int agility, int range, int attack) {
			this.maxHealth = maxHealth;
			this.agility = agility;
			this.range = range;
			this.attack = attack;
		}

		public int getMaxHealth() {
			return maxHealth;
		}

		public int getAgility() {
			return agility;
		}

		public int getRange() {
			return range;
		}

		public int getAttack() {
			return attack;
		}
	}

	private ID id;
	private Player player;

	private boolean canMove, canAct;
	/**
	 * A HashMap of open locations this unit can move to
	 * The integer value indicates the max number of remaining moves from a location
	 */
	private HashMap<Point, Integer> moves;
	/** A list of entities this unit can attack */
	private ArrayList<Entity> targets;

	public Unit(Point coord, ID id, Player player, Isotactica game) {
		super(coord, game.getAssets().units[id.ordinal()], game);

		this.id = id;
		this.player = player;
		currentHealth = id.maxHealth;
		moves = new HashMap<>();
		targets = new ArrayList<>();

		nextTurn();

		String coordText = " at [" + coord.x + ", " + coord.y + "]";
		Gdx.app.log("Entity spawned", getID() + coordText);
	}

	/** Resets the unit's movement and action tokens and begins its next turn */
	public void nextTurn() {
		canAct = true;
		canMove = true;

		findMoves();
		findTargets();
	}

	/* Widget Rendering */

	@Override
	public void render() {
		super.render();
		drawHealthBar();
		drawActionIndicator();
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

	/** Draws an indicator above a user unit to indicate an action is available. */
	private void drawActionIndicator() {
		if (player != world.getUser()) return;
		if (!canMove && !canAct) return;

		float scale = game.TILE_HEIGHT / world.getMapCamera().zoom;
		game.getBatch().draw(game.getAssets().highlights[5], getX(), getY() + scale, scale * 2, scale);
	}

	/** Highlights all moves and actions available to this unit */
	public void drawAvailableActions() {
		float scale = game.TILE_HEIGHT / world.getMapCamera().zoom;

		for (Point move: moves.keySet()) {
			Point drawPos = world.getMapOverlay().getPointerPosition(move);
			game.getBatch().draw(game.getAssets().highlights[1], drawPos.x, drawPos.y, scale * 2, scale);
		}

		for (Entity target: targets) {
			Point drawPos = world.getMapOverlay().getPointerPosition(target.coord);
			game.getBatch().draw(game.getAssets().highlights[4], drawPos.x, drawPos.y, scale * 2, scale);
		}
	}

	/* Movement-Related Functions */

	/** Attempts to move this unit to the specified grid location */
	public void move(Point coord) {
		if (!canMoveToTile(coord)) return;

		String lastCoordText = " moved from [" + this.coord.x + ", " + this.coord.y + "]";
		String newCoordText = " to [" + coord.x + ", " + coord.y + "]";
		Gdx.app.log("Action", getID() + lastCoordText + newCoordText);

		canMove = false;
		this.coord = coord;

		entityManager.requireSort();
		player.assessActions();
	}

	/** Checks whether this unit can be moved to the specified grid location */
	public boolean canMoveToTile(Point coord) {
		if (!canMove) return false;
		return moves.containsKey(coord);
	}

	/** Compiles a list of grid locations this unit can currently move to. */
	public void findMoves() {
		moves.clear();
		if (!canMove) return;
		findMoves(coord, id.agility);
	}

	/**
	 * Uses a modified flood fill algorithm to determine which grid locations the unit can move to.
	 * @param coord The starting coordinate for the fill
	 * @param movesLeft The number of remaining move cycles to be computed
	 */
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

	/** This unit attempts to attack a specified entity */
	public void attack(Entity e) {
		if (!canAttackEntity(e)) return;

		String coordText = " [" + coord.x + ", " + coord.y + "]";
		String targetCoordText = " [" + e.coord.x + ", " + e.coord.y + "]";
		String attackText = getID() + coordText + " attacked " + e.getID() + targetCoordText;
		Gdx.app.log("Action",  attackText);

		canMove = false;
		canAct = false;
		e.adjustHealth(-id.attack);

		player.assessActions();
	}

	/** Checks whether this unit is able to attack the specified entity */
	public boolean canAttackEntity(Entity e) {
		if (!canAct) return false;
		return targets.contains(e);
	}

	/** Compiles a list of all entities this unit is able to attack */
	public void findTargets() {
		targets.clear();
		if (!canAct) return;

		for (Entity e: entityManager.getEntityList()) {
			if (!e.isActive()) continue;
			if (e instanceof Unit && player == ((Unit) e).getPlayer()) continue;

			int diffX = Math.abs(coord.x - e.coord.x);
			int diffY = Math.abs(coord.y - e.coord.y);

			if (diffX + diffY <= id.range) targets.add(e);
		}
	}

	@Override
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
		return getID() + healthText + statsText;
	}

	@Override
	public String getID() {
		return "Unit." + id;
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
}
