package io.thebitspud.isotactica.world.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.utils.Identifiable;
import io.thebitspud.isotactica.utils.JTimerUtil;
import io.thebitspud.isotactica.world.Direction;
import io.thebitspud.isotactica.world.World;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.awt.Point;

/**
 * The catch-all template for non-tile game objects
 */

public abstract class Entity extends Sprite implements Identifiable {
	protected Isotactica game;
	protected World world;
	protected EntityManager entityManager;

	protected JTimerUtil moveTween;
	protected Point coord, lastCoord;
	protected int currentHealth, maxHealth;
	protected boolean active, pushable;

	public Entity(Point coord, TextureRegion texture, Isotactica game, boolean pushable) {
		super(texture);
		this.game = game;
		this.coord = coord;
		lastCoord = new Point(coord);

		world = game.getWorld();
		entityManager = world.getEntityManager();
		active = true;
		this.pushable = pushable;
		setOrigin(0, 0);

		moveTween = new JTimerUtil(0.4f, false, false) {
			@Override
			public void onActivation() {
				entityManager.requireSort();
				checkTile();
			}
		};
	}

	private void checkTile() {
		if (coord.x < 0 || coord.x > world.getWidth() - 1) active = false;
		if (coord.y < 0 || coord.y > world.getHeight() - 1) active = false;
		if (world.getTileID(coord).isEmpty()) active = false;
	}

	public void tick(float delta) {
		Point offset = world.getMapOverlay().getPointerPosition(coord);

		if (moveTween.isActive()) {
			// Calculating the render location of the entity during a tween
			Point lastOffset = world.getMapOverlay().getPointerPosition(lastCoord);
			float completion = (float) (moveTween.getTimeElapsed() / moveTween.getTimerDuration());
			float tweenOffsetX = offset.x * completion + lastOffset.x * (1 - completion);
			float tweenOffsetY = offset.y * completion + lastOffset.y * (1 - completion);

			if (moveTween.getTimeElapsed() < 0) setPosition(lastOffset.x, lastOffset.y);
			else setPosition(tweenOffsetX, tweenOffsetY);
		} else {
			// Calculating the render location of a non-moving entity
			// This is done once per frame to account for camera movement
			setPosition(offset.x, offset.y);
		}

		moveTween.tick(delta);

		setScale(1 / world.getMapCamera().zoom);
	}

	public void push(Direction dir) {
		if (!pushable) return;
		if (entityManager.getEntity(dir.to(coord)) != null) return;

		lastCoord = coord;
		coord = dir.to(coord);

		moveTween.setTimeElapsed(0);
		moveTween.setActive(true);

		// Logging
		String pushText = " pushed " + dir + " to [" + coord.x + ", " + coord.y + "]";
		Gdx.app.log("Action", getIDText() + pushText);
	}

	public void render() {
		draw(game.getBatch());
		drawHealthBar();
	}

	/** Draws a dynamic health bar above the unit. */
	protected void drawHealthBar() {
		float healthPercent = (float) currentHealth / maxHealth * 100;
		float width = getWidth() * getScaleX();
		float height = 2 * getScaleY();
		float xPos = getX() + width / 4;
		float yPos = getY() + (getHeight() - 10) * getScaleY();

		ShapeDrawer drawer = game.getAssets().getDrawer();
		drawer.filledRectangle(xPos, yPos, width / 2, height, Color.BLACK);
		drawer.setColor((100 - healthPercent) / 100f, healthPercent / 100f, 0, 1);
		drawer.filledRectangle(xPos, yPos, width * healthPercent / 200,  height);
	}

	/** Increments or decrements the entity's health by the specified value */
	public void adjustHealth(int value) {
		currentHealth += value;

		// Logging
		String healText = "Healed " + value + " to " + getIDText();
		String damageText = "Dealt " + -value + " to " + getIDText();
		Gdx.app.log("Health", value > 0 ? healText : damageText);

		if (currentHealth > maxHealth) currentHealth = maxHealth;
		else if (currentHealth <= 0) {
			currentHealth = 0;
			active = false;
		}
	}

	/* Getters and Setters */

	public boolean isActive() {
		return active;
	}

	public Point getCoord() {
		return coord;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	public int getMaxHealth() {
		return maxHealth;
	}
}
