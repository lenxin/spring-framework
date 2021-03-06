package org.springframework.web.socket.messaging;

import java.security.Principal;

import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;

/**
 * Event raised when a new WebSocket client using a Simple Messaging Protocol
 * (e.g. STOMP) sends a request to remove a subscription.
 *
 * @author Rossen Stoyanchev
 * @since 4.0.3
 */
@SuppressWarnings("serial")
public class SessionUnsubscribeEvent extends AbstractSubProtocolEvent {

	public SessionUnsubscribeEvent(Object source, Message<byte[]> message) {
		super(source, message);
	}

	public SessionUnsubscribeEvent(Object source, Message<byte[]> message, @Nullable Principal user) {
		super(source, message, user);
	}

}
