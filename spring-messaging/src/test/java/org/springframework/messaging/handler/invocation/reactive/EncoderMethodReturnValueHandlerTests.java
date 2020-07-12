package org.springframework.messaging.handler.invocation.reactive;

import io.reactivex.Completable;
import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.codec.CharSequenceEncoder;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.springframework.messaging.handler.invocation.ResolvableMethod.on;

/**
 * Unit tests for {@link AbstractEncoderMethodReturnValueHandler}.
 */
public class EncoderMethodReturnValueHandlerTests {

	private final TestEncoderMethodReturnValueHandler handler = new TestEncoderMethodReturnValueHandler(
			Collections.singletonList(CharSequenceEncoder.textPlainOnly()),
			ReactiveAdapterRegistry.getSharedInstance());

	private final Message<?> message = new GenericMessage<>("shouldn't matter");


	@Test
	public void stringReturnValue() {
		MethodParameter parameter = on(TestController.class).resolveReturnType(String.class);
		this.handler.handleReturnValue("foo", parameter, this.message).block();
		Flux<String> result = this.handler.getContentAsStrings();

		StepVerifier.create(result).expectNext("foo").verifyComplete();
	}

	@Test
	public void objectReturnValue() {
		MethodParameter parameter = on(TestController.class).resolveReturnType(Object.class);
		this.handler.handleReturnValue("foo", parameter, this.message).block();
		Flux<String> result = this.handler.getContentAsStrings();

		StepVerifier.create(result).expectNext("foo").verifyComplete();
	}

	@Test
	public void fluxStringReturnValue() {
		MethodParameter parameter = on(TestController.class).resolveReturnType(Flux.class, String.class);
		this.handler.handleReturnValue(Flux.just("foo", "bar"), parameter, this.message).block();
		Flux<String> result = this.handler.getContentAsStrings();

		StepVerifier.create(result).expectNext("foo").expectNext("bar").verifyComplete();
	}

	@Test
	public void fluxObjectReturnValue() {
		MethodParameter parameter = on(TestController.class).resolveReturnType(Flux.class, Object.class);
		this.handler.handleReturnValue(Flux.just("foo", "bar"), parameter, this.message).block();
		Flux<String> result = this.handler.getContentAsStrings();

		StepVerifier.create(result).expectNext("foo").expectNext("bar").verifyComplete();
	}

	@Test
	public void voidReturnValue() {
		testVoidReturnType(null, on(TestController.class).resolveReturnType(void.class));
		testVoidReturnType(Mono.empty(), on(TestController.class).resolveReturnType(Mono.class, Void.class));
		testVoidReturnType(Completable.complete(), on(TestController.class).resolveReturnType(Completable.class));
	}

	private void testVoidReturnType(@Nullable Object value, MethodParameter bodyParameter) {
		this.handler.handleReturnValue(value, bodyParameter, this.message).block();
		Flux<String> result = this.handler.getContentAsStrings();
		StepVerifier.create(result).expectComplete().verify();
	}

	@Test
	public void noEncoder() {
		MethodParameter parameter = on(TestController.class).resolveReturnType(Object.class);
		StepVerifier.create(this.handler.handleReturnValue(new Object(), parameter, this.message))
				.expectErrorMessage("No encoder for java.lang.Object, current value type is class java.lang.Object")
				.verify();
	}


	@SuppressWarnings({"unused", "ConstantConditions"})
	private static class TestController {

		String string() {
			return null;
		}

		Object object() {
			return null;
		}

		Flux<String> fluxString() {
			return null;
		}

		Flux<Object> fluxObject() {
			return null;
		}

		void voidReturn() {
		}

		Mono<Void> monoVoid() {
			return null;
		}

		Completable completable() {
			return null;
		}
	}

}
