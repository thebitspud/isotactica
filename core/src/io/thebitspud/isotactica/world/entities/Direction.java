package io.thebitspud.isotactica.world.entities;

import java.awt.*;

public enum Direction {
	WEST (1, 0),
	EAST (-1, 0),
	SOUTH (0, 1),
	NORTH (0, -1);

	private final int dx, dy;

	Direction(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}

	/** Retrieves the coord of the given cardinal direction next to the specified coord */
	public Point to(Point coord) {
		return new Point(coord.x + dx, coord.y + dy);
	}
}
