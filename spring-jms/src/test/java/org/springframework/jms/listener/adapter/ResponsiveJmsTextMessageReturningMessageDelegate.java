package org.springframework.jms.listener.adapter;

import javax.jms.TextMessage;

/**
 * See the MessageListenerAdapterTests class for usage.
 *

 */
public interface ResponsiveJmsTextMessageReturningMessageDelegate {

	TextMessage handleMessage(TextMessage message);

}
