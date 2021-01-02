package io.thebitspud.isotactica.world.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.utils.JTimerUtil;
import io.thebitspud.isotactica.world.World;

import java.awt.Point;

/**
 * The catch-all template for non-tile game objects
 */

public abstract class Entity extends Sprite {
	protected Isotactica game;
	protected World world;
	protected EntityManager entityManager;

	protected JTimerUtil moveTween;
	protected Point coord, lastCoord;
	protected int currentHealth;
	protected boolean active;

	public Entity(Point coord, TextureRegion texture, Isotactica game) {
		super(texture);
		this.game = game;
		this.coord = coord;
		lastCoord = new Point(coord);

		world = game.getWorld();
		entityManager = world.getEntityManager();
		active = true;
		setOrigin(0, 0);

		moveTween = new JTimerUtil(0.5f, false, false) {
			@Override
			public void onActivation() {

			}
		};
	}

	public void tick(float delta) {
		Point offset = world.getMapOverlay().getPointerPosition(coord);
		moveTween.tick(delta);

		if (moveTween.isActive()) {
			// Calculating the render location of the entity during a tween
			Point lastOffset = world.getMapOverlay().getPointerPosition(lastCoord);
			float completion = (float) (moveTween.getTimeElapsed() / moveTween.getTimerDuration());
			float tweenOffsetX = offset.x * completion + lastOffset.x * (1 - completion);
			float tweenOffsetY = offset.y * completion + lastOffset.y * (1 - completion);

			setPosition(tweenOffsetX, tweenOffsetY);
		} else {
			// Calculating the render location of a non-moving entity
			// This is done once per frame to account for camera movement
			setPosition(offset.x, offset.y);
		}

		setScale(1 / world.getMapCamera().zoom);
	}

	public void render() {
		draw(game.getBatch());
	}

	/* Getters and Setters */

	public abstract String getInfo();
	public abstract String getID();

	/** Increments or decrements the entity's health by the specified value */
	public abstract void adjustHealth(int value);

	public boolean isActive() {
		return active;
	}

	public Point getCoord() {
		return coord;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
