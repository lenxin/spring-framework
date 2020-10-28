package org.springframework.test.util.subpackage;

/**
 * Exception thrown by a {@link LegacyEntity}.
 *

 * @since 4.3.1
 */
@SuppressWarnings("serial")
public class LegacyEntityException extends RuntimeException {

	public LegacyEntityException(String message) {
		super(message);
	}

}
