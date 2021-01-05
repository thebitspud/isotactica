package io.thebitspud.isotactica.world.abilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.utils.Identifiable;
import io.thebitspud.isotactica.world.entities.Entity;
import io.thebitspud.isotactica.world.entities.Unit;

import java.awt.*;
import java.util.ArrayList;

/**
 * A base template for special actions used by units
 */

public abstract class Ability implements Identifiable {
	protected Isotactica game;

	protected TextureRegion icon;
	protected ArrayList<Entity> targets;
	protected Unit caster;

	protected int cooldown, currentCooldown, cost;

	public Ability(Isotactica game, Unit caster, TextureRegion icon) {
		this.game = game;
		this.caster = caster;
		this.icon = icon;

		targets = new ArrayList<>();
	}

	/** Casts the ability, applying on-hit effects to all targets */
	public void cast(Point coord) {
		trackTargets(coord);

		// Logging
		String castText = "Cast " + getIDText() + " at";
		String coordText = "[" + coord.x + ", " + coord.y + "]";
		Gdx.app.log("Action", castText + coordText);

		for (Entity target : targets) onHit(target);

		currentCooldown = cooldown;
	}

	/** Progresses the current cooldown by 1 turn if possible */
	public void update() {
		if (currentCooldown > 0) currentCooldown -= 1;
	}

	/**
	 * Checks if the specified entity is a valid target
	 * If it is, it is added to the list of affected targets
	 */
	public void addTarget(Entity e) {
		if (e == null) return;
		if (!e.isActive()) return;
		if (e instanceof Unit && ((Unit) e).getPlayer() == caster.getPlayer()) return;

		targets.add(e);
	}

	/* Abstract Methods */

	/** Determines which units are to be hit by the ability */
	protected abstract void trackTargets(Point coord);
	/** Applies all on-hit effects to the specified entity */
	protected abstract void onHit(Entity e);
}
