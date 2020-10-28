package org.springframework.web.reactive.function.server;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.result.view.View;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.FixedLocaleContextResolver;

import static org.junit.Assert.*;

/**

 */
public class LocaleContextResolverIntegrationTests extends AbstractRouterFunctionIntegrationTests {

	private final WebClient webClient = WebClient.create();


	@Override
	protected RouterFunction<?> routerFunction() {
		return RouterFunctions.route(RequestPredicates.path("/"), this::render);
	}

	public Mono<RenderingResponse> render(ServerRequest request) {
		return RenderingResponse.create("foo").build();
	}

	@Override
	protected HandlerStrategies handlerStrategies() {
		return HandlerStrategies.builder()
				.viewResolver(new DummyViewResolver())
				.localeContextResolver(new FixedLocaleContextResolver(Locale.GERMANY))
				.build();
	}


	@Test
	public void fixedLocale() {
		Mono<ClientResponse> result = webClient
				.get()
				.uri("http://localhost:" + this.port + "/")
				.exchange();

		StepVerifier
				.create(result)
				.consumeNextWith(response -> {
					assertEquals(HttpStatus.OK, response.statusCode());
					assertEquals(Locale.GERMANY, response.headers().asHttpHeaders().getContentLanguage());
				})
				.verifyComplete();
	}


	private static class DummyViewResolver implements ViewResolver {

		@Override
		public Mono<View> resolveViewName(String viewName, Locale locale) {
			return Mono.just(new DummyView(locale));
		}
	}


	private static class DummyView implements View {

		private final Locale locale;

		public DummyView(Locale locale) {
			this.locale = locale;
		}

		@Override
		public List<MediaType> getSupportedMediaTypes() {
			return Collections.singletonList(MediaType.TEXT_HTML);
		}

		@Override
		public Mono<Void> render(@Nullable Map<String, ?> model, @Nullable MediaType contentType,
				ServerWebExchange exchange) {
			exchange.getResponse().getHeaders().setContentLanguage(locale);
			return Mono.empty();
		}
	}

}
