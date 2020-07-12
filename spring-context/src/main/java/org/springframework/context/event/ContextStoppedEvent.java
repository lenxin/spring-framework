package org.springframework.context.event;

import org.springframework.context.ApplicationContext;

/**
 * Event raised when an {@code ApplicationContext} gets stopped.
 *
 * @see ContextStartedEvent
 * @since 2.5
 */
@SuppressWarnings("serial")
public class ContextStoppedEvent extends ApplicationContextEvent {

	/**
	 * Create a new ContextStoppedEvent.
	 *
	 * @param source the {@code ApplicationContext} that has been stopped
	 *               (must not be {@code null})
	 */
	public ContextStoppedEvent(ApplicationContext source) {
		super(source);
	}

}
