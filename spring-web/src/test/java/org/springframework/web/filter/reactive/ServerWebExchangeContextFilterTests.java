package org.springframework.web.filter.reactive;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import reactor.core.publisher.Mono;

import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.mock.http.server.reactive.test.MockServerHttpRequest;
import org.springframework.mock.http.server.reactive.test.MockServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link ServerWebExchangeContextFilter}.
 * @author Rossen Stoyanchev
 */
public class ServerWebExchangeContextFilterTests {

	@Test
	public void extractServerWebExchangeFromContext() {
		MyService service = new MyService();

		HttpHandler httpHandler = WebHttpHandlerBuilder
				.webHandler(exchange -> service.service().then())
				.filter(new ServerWebExchangeContextFilter())
				.build();

		httpHandler.handle(MockServerHttpRequest.get("/path").build(), new MockServerHttpResponse())
				.block(Duration.ofSeconds(5));

		assertNotNull(service.getExchange());
	}


	private static class MyService {

		private final AtomicReference<ServerWebExchange> exchangeRef = new AtomicReference<>();


		public ServerWebExchange getExchange() {
			return this.exchangeRef.get();
		}

		public Mono<String> service() {
			return Mono.just("result").subscriberContext(context -> {
				ServerWebExchangeContextFilter.get(context).ifPresent(exchangeRef::set);
				return context;
			});
		}
	}

}
