package org.springframework.messaging.rsocket;

import io.netty.buffer.ByteBuf;
import io.rsocket.DuplexConnection;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.ClientTransport;
import org.junit.Before;
import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DefaultRSocketRequesterBuilder}.
 */
public class DefaultRSocketRequesterBuilderTests {

	private ClientTransport transport;


	@Before
	public void setup() {
		this.transport = mock(ClientTransport.class);
		when(this.transport.connect(anyInt())).thenReturn(Mono.just(new MockConnection()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldApplyCustomizationsAtSubscription() {
		Consumer<RSocketFactory.ClientRSocketFactory> factoryConfigurer = mock(Consumer.class);
		Consumer<RSocketStrategies.Builder> strategiesConfigurer = mock(Consumer.class);
		RSocketRequester.builder()
				.rsocketFactory(factoryConfigurer)
				.rsocketStrategies(strategiesConfigurer)
				.connect(this.transport);
		verifyZeroInteractions(this.transport, factoryConfigurer, strategiesConfigurer);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldApplyCustomizations() {
		Consumer<RSocketFactory.ClientRSocketFactory> factoryConfigurer = mock(Consumer.class);
		Consumer<RSocketStrategies.Builder> strategiesConfigurer = mock(Consumer.class);
		RSocketRequester.builder()
				.rsocketFactory(factoryConfigurer)
				.rsocketStrategies(strategiesConfigurer)
				.connect(this.transport)
				.block();
		verify(this.transport).connect(anyInt());
		verify(factoryConfigurer).accept(any(RSocketFactory.ClientRSocketFactory.class));
		verify(strategiesConfigurer).accept(any(RSocketStrategies.Builder.class));
	}

	static class MockConnection implements DuplexConnection {

		@Override
		public Mono<Void> send(Publisher<ByteBuf> frames) {
			return Mono.empty();
		}

		@Override
		public Flux<ByteBuf> receive() {
			return Flux.empty();
		}

		@Override
		public Mono<Void> onClose() {
			return Mono.empty();
		}

		@Override
		public void dispose() {

		}
	}

}