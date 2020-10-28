package org.springframework.messaging.rsocket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.codec.Decoder;
import org.springframework.core.codec.Encoder;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Default, package-private {@link RSocketStrategies} implementation.
 *

 * @since 5.2
 */
final class DefaultRSocketStrategies implements RSocketStrategies {

	private final List<Encoder<?>> encoders;

	private final List<Decoder<?>> decoders;

	private final ReactiveAdapterRegistry adapterRegistry;

	private final DataBufferFactory bufferFactory;


	private DefaultRSocketStrategies(List<Encoder<?>> encoders, List<Decoder<?>> decoders,
			ReactiveAdapterRegistry adapterRegistry, DataBufferFactory bufferFactory) {

		this.encoders = Collections.unmodifiableList(encoders);
		this.decoders = Collections.unmodifiableList(decoders);
		this.adapterRegistry = adapterRegistry;
		this.bufferFactory = bufferFactory;
	}


	@Override
	public List<Encoder<?>> encoders() {
		return this.encoders;
	}

	@Override
	public List<Decoder<?>> decoders() {
		return this.decoders;
	}

	@Override
	public ReactiveAdapterRegistry reactiveAdapterRegistry() {
		return this.adapterRegistry;
	}

	@Override
	public DataBufferFactory dataBufferFactory() {
		return this.bufferFactory;
	}


	/**
	 * Default RSocketStrategies.Builder implementation.
	 */
	static class DefaultRSocketStrategiesBuilder implements RSocketStrategies.Builder {

		private final List<Encoder<?>> encoders = new ArrayList<>();

		private final List<Decoder<?>> decoders = new ArrayList<>();

		private ReactiveAdapterRegistry adapterRegistry = ReactiveAdapterRegistry.getSharedInstance();

		@Nullable
		private DataBufferFactory dataBufferFactory;

		public DefaultRSocketStrategiesBuilder() {
		}

		public DefaultRSocketStrategiesBuilder(RSocketStrategies other) {
			this.encoders.addAll(other.encoders());
			this.decoders.addAll(other.decoders());
			this.adapterRegistry = other.reactiveAdapterRegistry();
			this.dataBufferFactory = other.dataBufferFactory();
		}

		@Override
		public Builder encoder(Encoder<?>... encoders) {
			this.encoders.addAll(Arrays.asList(encoders));
			return this;
		}

		@Override
		public Builder decoder(Decoder<?>... decoder) {
			this.decoders.addAll(Arrays.asList(decoder));
			return this;
		}

		@Override
		public Builder encoders(Consumer<List<Encoder<?>>> consumer) {
			consumer.accept(this.encoders);
			return this;
		}

		@Override
		public Builder decoders(Consumer<List<Decoder<?>>> consumer) {
			consumer.accept(this.decoders);
			return this;
		}

		@Override
		public Builder reactiveAdapterStrategy(ReactiveAdapterRegistry registry) {
			Assert.notNull(registry, "ReactiveAdapterRegistry is required");
			this.adapterRegistry = registry;
			return this;
		}

		@Override
		public Builder dataBufferFactory(DataBufferFactory bufferFactory) {
			this.dataBufferFactory = bufferFactory;
			return this;
		}

		@Override
		public RSocketStrategies build() {
			return new DefaultRSocketStrategies(this.encoders, this.decoders, this.adapterRegistry,
					this.dataBufferFactory != null ? this.dataBufferFactory : new DefaultDataBufferFactory());
		}
	}

}
