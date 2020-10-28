package org.springframework.jms;

/**
 * Runtime exception mirroring the JMS InvalidDestinationException.
 *

 * @since 1.1
 * @see javax.jms.InvalidDestinationException
 */
@SuppressWarnings("serial")
public class InvalidDestinationException extends JmsException {

	public InvalidDestinationException(javax.jms.InvalidDestinationException cause) {
		super(cause);
	}

}
