package org.springframework.jms;

/**
 * Runtime exception mirroring the JMS MessageNotReadableException.
 *

 * @since 1.1
 * @see javax.jms.MessageNotReadableException
 */
@SuppressWarnings("serial")
public class MessageNotReadableException extends JmsException {

	public MessageNotReadableException(javax.jms.MessageNotReadableException cause) {
		super(cause);
	}

}
