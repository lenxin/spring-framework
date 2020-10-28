package org.springframework.jms;

/**
 * Runtime exception mirroring the JMS MessageNotWriteableException.
 *

 * @since 1.1
 * @see javax.jms.MessageNotWriteableException
 */
@SuppressWarnings("serial")
public class MessageNotWriteableException extends JmsException {

	public MessageNotWriteableException(javax.jms.MessageNotWriteableException cause) {
		super(cause);
	}

}
