package org.springframework.messaging.handler.annotation.support;

import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;

import java.lang.reflect.Method;

/**
 * A factory for {@link InvocableHandlerMethod} that is suitable to process
 * an incoming {@link org.springframework.messaging.Message}
 *
 * <p>Typically used by listener endpoints that require a flexible method
 * signature.
 *
 * @since 4.1
 */
public interface MessageHandlerMethodFactory {

	/**
	 * Create the {@link InvocableHandlerMethod} that is able to process the specified
	 * method endpoint.
	 *
	 * @param bean   the bean instance
	 * @param method the method to invoke
	 * @return an {@link InvocableHandlerMethod} suitable for that method
	 */
	InvocableHandlerMethod createInvocableHandlerMethod(Object bean, Method method);

}
