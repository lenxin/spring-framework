package org.springframework.messaging.rsocket;

import java.util.function.Function;

import io.rsocket.ConnectionSetupPayload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import reactor.core.publisher.Mono;

import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.util.MimeType;

/**
 * Extension of {@link RSocketMessageHandler} that can be plugged directly into
 * RSocket to receive connections either on the
 * {@link io.rsocket.RSocketFactory.ClientRSocketFactory#acceptor(Function) client} or on the
 * {@link io.rsocket.RSocketFactory.ServerRSocketFactory#acceptor(SocketAcceptor) server}
 * side. Requests are handled by delegating to the "super" {@link #handleMessage(Message)}.
 *

 * @since 5.2
 */
public final class MessageHandlerAcceptor extends RSocketMessageHandler
		implements SocketAcceptor, Function<RSocket, RSocket> {

	@Nullable
	private MimeType defaultDataMimeType;


	/**
	 * Configure the default content type to use for data payloads.
	 * <p>By default this is not set. However a server acceptor will use the
	 * content type from the {@link ConnectionSetupPayload}, so this is typically
	 * required for clients but can also be used on servers as a fallback.
	 * @param defaultDataMimeType the MimeType to use
	 */
	public void setDefaultDataMimeType(@Nullable MimeType defaultDataMimeType) {
		this.defaultDataMimeType = defaultDataMimeType;
	}


	@Override
	public Mono<RSocket> accept(ConnectionSetupPayload setupPayload, RSocket sendingRSocket) {
		MessagingRSocket rsocket = createRSocket(sendingRSocket);

		// Allow handling of the ConnectionSetupPayload via @MessageMapping methods.
		// However, if the handling is to make requests to the client, it's expected
		// it will do so decoupled from the handling, e.g. via .subscribe().
		return rsocket.handleConnectionSetupPayload(setupPayload).then(Mono.just(rsocket));
	}

	@Override
	public RSocket apply(RSocket sendingRSocket) {
		return createRSocket(sendingRSocket);
	}

	private MessagingRSocket createRSocket(RSocket rsocket) {
		return new MessagingRSocket(this::handleMessage,
				RSocketRequester.wrap(rsocket, this.defaultDataMimeType, getRSocketStrategies()),
				this.defaultDataMimeType,
				getRSocketStrategies().dataBufferFactory());
	}

}
