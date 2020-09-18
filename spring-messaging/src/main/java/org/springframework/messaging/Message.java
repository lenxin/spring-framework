package org.springframework.messaging;

/**
 * A generic message representation with headers and body.
 *
 * @param <T> the payload type
 * @see org.springframework.messaging.support.MessageBuilder
 * @since 4.0
 */
public interface Message<T> {
	/**
	 * Return the message payload.
	 */
	T getPayload();

	/**
	 * Return message headers for the message (never {@code null} but may be empty).
	 */
	MessageHeaders getHeaders();
}
