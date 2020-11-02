package io.thebitspud.isotactica.world.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.world.World;

import java.awt.Point;

/**
 * The catch-all template for non-tile game objects
 */

public abstract class Entity extends Sprite {
	protected Isotactica game;
	protected World world;
	protected Point coord;

	protected boolean active;

	public Entity(Point coord, TextureRegion texture, Isotactica game) {
		super(texture);
		this.game = game;
		this.coord = coord;

		world = game.getWorld();
		active = true;
	}

	public void tick(float delta) {
		Point renderPosition = world.getMapOverlay().getPointerPosition(coord);
		setPosition(renderPosition.x, renderPosition.y);
		setOrigin(0, 0);
		setScale(1 / world.getMapCamera().zoom);
	}

	public void render() {
		draw(game.getBatch());
	}

	public abstract String getInfo();

	public Point getCoord() {
		return coord;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
