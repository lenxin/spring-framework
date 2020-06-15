
package org.springframework.messaging.handler.invocation.reactive;

import java.util.List;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.codec.Encoder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.support.DataBufferTestUtils;
import org.springframework.messaging.Message;

import static java.nio.charset.StandardCharsets.*;

/**
 * Implementation of {@link AbstractEncoderMethodReturnValueHandler} for tests.
 * "Handles" by storing encoded return values.
 *
 * @author Rossen Stoyanchev
 */
public class TestEncoderMethodReturnValueHandler extends AbstractEncoderMethodReturnValueHandler {

	private Flux<DataBuffer> encodedContent;


	public TestEncoderMethodReturnValueHandler(List<Encoder<?>> encoders, ReactiveAdapterRegistry registry) {
		super(encoders, registry);
	}


	public Flux<DataBuffer> getContent() {
		return this.encodedContent;
	}

	public Flux<String> getContentAsStrings() {
		return this.encodedContent.map(buffer -> DataBufferTestUtils.dumpString(buffer, UTF_8));
	}

	@Override
	protected Mono<Void> handleEncodedContent(
			Flux<DataBuffer> encodedContent, MethodParameter returnType, Message<?> message) {

		this.encodedContent = encodedContent.cache();
		return this.encodedContent.then();
	}

	@Override
	protected Mono<Void> handleNoContent(MethodParameter returnType, Message<?> message) {
		this.encodedContent = Flux.empty();
		return Mono.empty();
	}
}
