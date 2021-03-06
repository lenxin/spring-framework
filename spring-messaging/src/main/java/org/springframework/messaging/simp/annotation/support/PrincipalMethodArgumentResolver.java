package org.springframework.messaging.simp.annotation.support;

import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.security.Principal;

/**
 * {@link HandlerMethodArgumentResolver} to a {@link Principal}.
 *
 * @since 4.0
 */
public class PrincipalMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> paramType = parameter.getParameterType();
		return Principal.class.isAssignableFrom(paramType);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, Message<?> message) throws Exception {
		Principal user = SimpMessageHeaderAccessor.getUser(message.getHeaders());
		if (user == null) {
			throw new MissingSessionUserException(message);
		}
		return user;
	}

}
