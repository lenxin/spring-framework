package org.springframework.messaging.handler.annotation.support;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import org.springframework.core.MethodParameter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.invocation.ResolvableMethod;
import org.springframework.messaging.support.MessageBuilder;

import static org.junit.Assert.*;
import static org.springframework.messaging.handler.annotation.MessagingPredicates.*;

/**
 * Test fixture for {@link DestinationVariableMethodArgumentResolver} tests.
 *
 * @author Brian Clozel
 */
public class DestinationVariableMethodArgumentResolverTests {

	private final DestinationVariableMethodArgumentResolver resolver =
			new DestinationVariableMethodArgumentResolver(new DefaultConversionService());

	private final ResolvableMethod resolvable =
			ResolvableMethod.on(getClass()).named("handleMessage").build();


	@Test
	public void supportsParameter() {
		assertTrue(resolver.supportsParameter(this.resolvable.annot(destinationVar().noValue()).arg()));
		assertFalse(resolver.supportsParameter(this.resolvable.annotNotPresent(DestinationVariable.class).arg()));
	}

	@Test
	public void resolveArgument() throws Exception {

		Map<String, Object> vars = new HashMap<>();
		vars.put("foo", "bar");
		vars.put("name", "value");

		Message<byte[]> message = MessageBuilder.withPayload(new byte[0]).setHeader(
			DestinationVariableMethodArgumentResolver.DESTINATION_TEMPLATE_VARIABLES_HEADER, vars).build();

		MethodParameter param = this.resolvable.annot(destinationVar().noValue()).arg();
		Object result = this.resolver.resolveArgument(param, message);
		assertEquals("bar", result);

		param = this.resolvable.annot(destinationVar("name")).arg();
		result = this.resolver.resolveArgument(param, message);
		assertEquals("value", result);
	}

	@Test(expected = MessageHandlingException.class)
	public void resolveArgumentNotFound() throws Exception {
		Message<byte[]> message = MessageBuilder.withPayload(new byte[0]).build();
		this.resolver.resolveArgument(this.resolvable.annot(destinationVar().noValue()).arg(), message);
	}

	@SuppressWarnings("unused")
	private void handleMessage(
			@DestinationVariable String foo,
			@DestinationVariable(value = "name") String param1,
			String param3) {
	}

}
