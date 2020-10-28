package org.springframework.jms;

/**
 * Runtime exception mirroring the JMS InvalidSelectorException.
 *

 * @since 1.1
 * @see javax.jms.InvalidSelectorException
 */
@SuppressWarnings("serial")
public class InvalidSelectorException extends JmsException {

	public InvalidSelectorException(javax.jms.InvalidSelectorException cause) {
		super(cause);
	}

}
