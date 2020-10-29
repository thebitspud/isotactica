package io.thebitspud.isotactica.utils;

/**
 * A utility class that calls <em>onActivation()</em> after a set duration
 * Designed to be used with LibGDX
 */

public abstract class JTimerUtil implements Activatable {
	private double timeElapsed, timerDuration;
	private boolean autoRepeat, active;

	protected JTimerUtil(double duration, boolean autoRepeat, boolean active) {
		this.timerDuration = duration;
		this.autoRepeat = autoRepeat;

		this.active = active;
	}

	/**
	 * A JTimerUtil which counts endlessly and never activates
	 * Can be used as a stopwatch
	 */

	protected JTimerUtil(boolean active) {
		this.timerDuration = Double.MAX_VALUE;
		this.autoRepeat = true;

		this.active = active;
	}

	public void tick(float delta) {
		if(!active) return;

		timeElapsed += delta;
		if(timeElapsed >= timerDuration) {
			if(autoRepeat) timeElapsed -= timerDuration;
			else {
				timeElapsed = 0;
				active = false;
			}

			onActivation();
		}
	}

	public double getTimeElapsed() {
		return timeElapsed;
	}

	public double getTimerDuration() {
		return timerDuration;
	}

	public boolean isAutoRepeating() {
		return autoRepeat;
	}

	public boolean isActive() {
		return active;
	}

	public void setTimeElapsed(double time) {
		this.timeElapsed = time;
	}

	public void setAutoRepeat(boolean repeat) {
		this.autoRepeat = repeat;
	}

	public void setTimerDuration(double duration) {
		this.timerDuration = duration;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}