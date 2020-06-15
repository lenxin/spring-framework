

package org.springframework.http.codec;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.core.ResolvableType;
import org.springframework.core.codec.AbstractEncoder;
import org.springframework.core.codec.Encoder;
import org.springframework.core.codec.Hints;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.PooledDataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpLogging;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * {@code HttpMessageWriter} that wraps and delegates to an {@link Encoder}.
 *
 * <p>Also a {@code HttpMessageWriter} that pre-resolves encoding hints
 * from the extra information available on the server side such as the request
 * or controller method annotations.
 *
 * @author Arjen Poutsma
 * @author Sebastien Deleuze
 * @author Rossen Stoyanchev
 * @author Brian Clozel
 * @since 5.0
 * @param <T> the type of objects in the input stream
 */
public class EncoderHttpMessageWriter<T> implements HttpMessageWriter<T> {

	private final Encoder<T> encoder;

	private final List<MediaType> mediaTypes;

	@Nullable
	private final MediaType defaultMediaType;


	/**
	 * Create an instance wrapping the given {@link Encoder}.
	 */
	public EncoderHttpMessageWriter(Encoder<T> encoder) {
		Assert.notNull(encoder, "Encoder is required");
		initLogger(encoder);
		this.encoder = encoder;
		this.mediaTypes = MediaType.asMediaTypes(encoder.getEncodableMimeTypes());
		this.defaultMediaType = initDefaultMediaType(this.mediaTypes);
	}

	private static void initLogger(Encoder<?> encoder) {
		if (encoder instanceof AbstractEncoder &&
				encoder.getClass().getName().startsWith("org.springframework.core.codec")) {
			Log logger = HttpLogging.forLog(((AbstractEncoder) encoder).getLogger());
			((AbstractEncoder) encoder).setLogger(logger);
		}
	}

	@Nullable
	private static MediaType initDefaultMediaType(List<MediaType> mediaTypes) {
		return mediaTypes.stream().filter(MediaType::isConcrete).findFirst().orElse(null);
	}


	/**
	 * Return the {@code Encoder} of this writer.
	 */
	public Encoder<T> getEncoder() {
		return this.encoder;
	}

	@Override
	public List<MediaType> getWritableMediaTypes() {
		return this.mediaTypes;
	}


	@Override
	public boolean canWrite(ResolvableType elementType, @Nullable MediaType mediaType) {
		return this.encoder.canEncode(elementType, mediaType);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Mono<Void> write(Publisher<? extends T> inputStream, ResolvableType elementType,
			@Nullable MediaType mediaType, ReactiveHttpOutputMessage message, Map<String, Object> hints) {

		MediaType contentType = updateContentType(message, mediaType);

		Flux<DataBuffer> body = this.encoder.encode(
				inputStream, message.bufferFactory(), elementType, contentType, hints);

		if (inputStream instanceof Mono) {
			HttpHeaders headers = message.getHeaders();
			return Mono.from(body)
					.switchIfEmpty(Mono.defer(() -> {
						headers.setContentLength(0);
						return message.setComplete().then(Mono.empty());
					}))
					.flatMap(buffer -> {
						headers.setContentLength(buffer.readableByteCount());
						return message.writeWith(Mono.just(buffer)
								.doOnDiscard(PooledDataBuffer.class, PooledDataBuffer::release));
					});
		}

		if (isStreamingMediaType(contentType)) {
			return message.writeAndFlushWith(body.map(buffer ->
					Mono.just(buffer).doOnDiscard(PooledDataBuffer.class, PooledDataBuffer::release)));
		}

		return message.writeWith(body);
	}

	@Nullable
	private MediaType updateContentType(ReactiveHttpOutputMessage message, @Nullable MediaType mediaType) {
		MediaType result = message.getHeaders().getContentType();
		if (result != null) {
			return result;
		}
		MediaType fallback = this.defaultMediaType;
		result = (useFallback(mediaType, fallback) ? fallback : mediaType);
		if (result != null) {
			result = addDefaultCharset(result, fallback);
			message.getHeaders().setContentType(result);
		}
		return result;
	}

	private static boolean useFallback(@Nullable MediaType main, @Nullable MediaType fallback) {
		return (main == null || !main.isConcrete() ||
				main.equals(MediaType.APPLICATION_OCTET_STREAM) && fallback != null);
	}

	private static MediaType addDefaultCharset(MediaType main, @Nullable MediaType defaultType) {
		if (main.getCharset() == null && defaultType != null && defaultType.getCharset() != null) {
			return new MediaType(main, defaultType.getCharset());
		}
		return main;
	}

	private boolean isStreamingMediaType(@Nullable MediaType contentType) {
		if (contentType == null || !(this.encoder instanceof HttpMessageEncoder)) {
			return false;
		}
		for (MediaType mediaType : ((HttpMessageEncoder<?>) this.encoder).getStreamingMediaTypes()) {
			if (contentType.isCompatibleWith(mediaType) &&
					contentType.getParameters().entrySet().containsAll(mediaType.getParameters().keySet())) {
				return true;
			}
		}
		return false;
	}


	// Server side only...

	@Override
	public Mono<Void> write(Publisher<? extends T> inputStream, ResolvableType actualType,
			ResolvableType elementType, @Nullable MediaType mediaType, ServerHttpRequest request,
			ServerHttpResponse response, Map<String, Object> hints) {

		Map<String, Object> allHints = Hints.merge(hints,
				getWriteHints(actualType, elementType, mediaType, request, response));

		return write(inputStream, elementType, mediaType, response, allHints);
	}

	/**
	 * Get additional hints for encoding for example based on the server request
	 * or annotations from controller method parameters. By default, delegate to
	 * the encoder if it is an instance of {@link HttpMessageEncoder}.
	 */
	protected Map<String, Object> getWriteHints(ResolvableType streamType, ResolvableType elementType,
			@Nullable MediaType mediaType, ServerHttpRequest request, ServerHttpResponse response) {

		if (this.encoder instanceof HttpMessageEncoder) {
			HttpMessageEncoder<?> encoder = (HttpMessageEncoder<?>) this.encoder;
			return encoder.getEncodeHints(streamType, elementType, mediaType, request, response);
		}
		return Hints.none();
	}

}
