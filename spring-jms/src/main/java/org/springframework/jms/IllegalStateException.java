package org.springframework.jms;

/**
 * Runtime exception mirroring the JMS IllegalStateException.
 *

 * @since 1.1
 * @see javax.jms.IllegalStateException
 */
@SuppressWarnings("serial")
public class IllegalStateException extends JmsException {

	public IllegalStateException(javax.jms.IllegalStateException cause) {
		super(cause);
	}

}
