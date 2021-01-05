package io.thebitspud.isotactica.world.abilities;

import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.world.Direction;
import io.thebitspud.isotactica.world.entities.Entity;
import io.thebitspud.isotactica.world.entities.EntityManager;
import io.thebitspud.isotactica.world.entities.Unit;

import java.awt.*;

/**
 * Any ability that does not have a windup delay and completes its on-hit instantly
 */

public class InstantAbility extends Ability {
	/** An enum of all instant abilities and their stats/effects */
	public enum ID {
		DOUBLE_STRIKE (1, 3),
		KINETIC_BLOW (2, 3),
		BLADE_SPIN(3, 5);

		public final int cost, cooldown;

		ID(int cost, int cooldown) {
			this.cost = cost;
			this.cooldown = cooldown;
		}
	}

	private ID id;

	public InstantAbility(Isotactica game, Unit caster, ID id) {
		super(game, caster, null);
		this.id = id;

		cooldown = id.cooldown;
		cost = id.cost;
	}

	@Override
	protected void trackTargets(Point coord) {
		EntityManager em = game.getWorld().getEntityManager();

		targets.clear();

		if (id == ID.BLADE_SPIN) {
			coord = caster.getCoord();

			addTarget(em.getEntity(Direction.WEST.to(coord)));
			addTarget(em.getEntity(Direction.EAST.to(coord)));
			addTarget(em.getEntity(Direction.SOUTH.to(coord)));
			addTarget(em.getEntity(Direction.NORTH.to(coord)));
		} else {
			addTarget(em.getEntity(coord));
		}
	}

	@Override
	protected void onHit(Entity e) {
		switch (id) {
			case DOUBLE_STRIKE:
				e.adjustHealth(-caster.getID().attack * 2);
				break;
			case KINETIC_BLOW:
				e.adjustHealth(-caster.getID().attack);
				for (Direction dir : Direction.values())
					if (e.getCoord().equals(dir.to(caster.getCoord())))
						e.push(dir); // Pushing enemy away from caster
				break;
			case BLADE_SPIN:
				e.adjustHealth(-caster.getID().attack);
				break;
			default: break;
		}
	}

	@Override
	public ID getID() {
		return id;
	}

	@Override
	public String getIDText() {
		return "InstantAbility." + id;
	}

	@Override
	public String getInfoText() {
		return getIDText() + "\nbottom text";
	}
}
