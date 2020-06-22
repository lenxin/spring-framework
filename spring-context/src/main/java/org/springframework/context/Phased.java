package org.springframework.context;

/**
 * Interface for objects that may participate in a phased
 * process such as lifecycle management.
 *
 * @see SmartLifecycle
 * @since 3.0
 */
public interface Phased {
	/**
	 * Return the phase value of this object.
	 */
	int getPhase();
}