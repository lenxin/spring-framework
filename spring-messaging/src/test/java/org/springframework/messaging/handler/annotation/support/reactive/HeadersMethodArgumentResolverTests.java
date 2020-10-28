package org.springframework.messaging.handler.annotation.support.reactive;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.invocation.ResolvableMethod;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;

import static org.junit.Assert.*;

/**
 * Test fixture for {@link HeadersMethodArgumentResolver} tests.

 */
public class HeadersMethodArgumentResolverTests {

	private final HeadersMethodArgumentResolver resolver = new HeadersMethodArgumentResolver();

	private Message<byte[]> message =
			MessageBuilder.withPayload(new byte[0]).copyHeaders(Collections.singletonMap("foo", "bar")).build();

	private final ResolvableMethod resolvable = ResolvableMethod.on(getClass()).named("handleMessage").build();


	@Test
	public void supportsParameter() {

		assertTrue(this.resolver.supportsParameter(
				this.resolvable.annotPresent(Headers.class).arg(Map.class, String.class, Object.class)));

		assertTrue(this.resolver.supportsParameter(this.resolvable.arg(MessageHeaders.class)));
		assertTrue(this.resolver.supportsParameter(this.resolvable.arg(MessageHeaderAccessor.class)));
		assertTrue(this.resolver.supportsParameter(this.resolvable.arg(TestMessageHeaderAccessor.class)));

		assertFalse(this.resolver.supportsParameter(this.resolvable.annotPresent(Headers.class).arg(String.class)));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void resolveArgumentAnnotated() {
		MethodParameter param = this.resolvable.annotPresent(Headers.class).arg(Map.class, String.class, Object.class);
		Map<String, Object> headers = resolveArgument(param);
		assertEquals("bar", headers.get("foo"));
	}

	@Test(expected = IllegalStateException.class)
	public void resolveArgumentAnnotatedNotMap() {
		resolveArgument(this.resolvable.annotPresent(Headers.class).arg(String.class));
	}

	@Test
	public void resolveArgumentMessageHeaders() {
		MessageHeaders headers = resolveArgument(this.resolvable.arg(MessageHeaders.class));
		assertEquals("bar", headers.get("foo"));
	}

	@Test
	public void resolveArgumentMessageHeaderAccessor() {
		MessageHeaderAccessor headers = resolveArgument(this.resolvable.arg(MessageHeaderAccessor.class));
		assertEquals("bar", headers.getHeader("foo"));
	}

	@Test
	public void resolveArgumentMessageHeaderAccessorSubclass() {
		TestMessageHeaderAccessor headers = resolveArgument(this.resolvable.arg(TestMessageHeaderAccessor.class));
		assertEquals("bar", headers.getHeader("foo"));
	}

	@SuppressWarnings({"unchecked", "ConstantConditions"})
	private <T> T resolveArgument(MethodParameter param) {
		return (T) this.resolver.resolveArgument(param, this.message).block(Duration.ofSeconds(5));
	}


	@SuppressWarnings("unused")
	private void handleMessage(
			@Headers Map<String, Object> param1,
			@Headers String param2,
			MessageHeaders param3,
			MessageHeaderAccessor param4,
			TestMessageHeaderAccessor param5) {
	}


	public static class TestMessageHeaderAccessor extends NativeMessageHeaderAccessor {

		TestMessageHeaderAccessor(Message<?> message) {
			super(message);
		}

		public static TestMessageHeaderAccessor wrap(Message<?> message) {
			return new TestMessageHeaderAccessor(message);
		}
	}

}
