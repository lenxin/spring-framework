package org.springframework.web.servlet.function;

import java.util.Optional;

import org.junit.Test;

import org.springframework.mock.web.test.MockHttpServletRequest;

import static java.util.Collections.emptyList;
import static org.junit.Assert.*;

/**

 */
@SuppressWarnings("unchecked")
public class RouterFunctionTests {

	@Test
	public void and() {
		HandlerFunction<ServerResponse> handlerFunction = request -> ServerResponse.ok().build();
		RouterFunction<ServerResponse> routerFunction1 = request -> Optional.empty();
		RouterFunction<ServerResponse> routerFunction2 = request -> Optional.of(handlerFunction);

		RouterFunction<ServerResponse> result = routerFunction1.and(routerFunction2);
		assertNotNull(result);

		MockHttpServletRequest servletRequest = new MockHttpServletRequest();
		ServerRequest request = new DefaultServerRequest(servletRequest, emptyList());

		Optional<HandlerFunction<ServerResponse>> resultHandlerFunction = result.route(request);
		assertTrue(resultHandlerFunction.isPresent());
		assertEquals(handlerFunction, resultHandlerFunction.get());
	}


	@Test
	public void andOther() {
		HandlerFunction<ServerResponse> handlerFunction = request -> ServerResponse.ok().body("42");
		RouterFunction<?> routerFunction1 = request -> Optional.empty();
		RouterFunction<ServerResponse> routerFunction2 = request -> Optional.of(handlerFunction);

		RouterFunction<?> result = routerFunction1.andOther(routerFunction2);
		assertNotNull(result);

		MockHttpServletRequest servletRequest = new MockHttpServletRequest();
		ServerRequest request = new DefaultServerRequest(servletRequest, emptyList());

		Optional<? extends HandlerFunction<?>> resultHandlerFunction = result.route(request);
		assertTrue(resultHandlerFunction.isPresent());
		assertEquals(handlerFunction, resultHandlerFunction.get());
	}


	@Test
	public void andRoute() {
		RouterFunction<ServerResponse> routerFunction1 = request -> Optional.empty();
		RequestPredicate requestPredicate = request -> true;

		RouterFunction<ServerResponse> result = routerFunction1.andRoute(requestPredicate, this::handlerMethod);
		assertNotNull(result);

		MockHttpServletRequest servletRequest = new MockHttpServletRequest();
		ServerRequest request = new DefaultServerRequest(servletRequest, emptyList());

		Optional<? extends HandlerFunction<?>> resultHandlerFunction = result.route(request);
		assertTrue(resultHandlerFunction.isPresent());
	}


	@Test
	public void filter() {
		String string = "42";
		HandlerFunction<EntityResponse<String>> handlerFunction =
				request -> EntityResponse.fromObject(string).build();
		RouterFunction<EntityResponse<String>> routerFunction =
				request -> Optional.of(handlerFunction);

		HandlerFilterFunction<EntityResponse<String>, EntityResponse<Integer>> filterFunction =
				(request, next) -> {
					String stringResponse = next.handle(request).entity();
					Integer intResponse = Integer.parseInt(stringResponse);
					return EntityResponse.fromObject(intResponse).build();
				};

		RouterFunction<EntityResponse<Integer>> result = routerFunction.filter(filterFunction);
		assertNotNull(result);

		MockHttpServletRequest servletRequest = new MockHttpServletRequest();
		ServerRequest request = new DefaultServerRequest(servletRequest, emptyList());

		Optional<EntityResponse<Integer>> resultHandlerFunction = result.route(request)
				.map(hf -> {
					try {
						return hf.handle(request);
					}
					catch (Exception e) {
						fail(e.getMessage());
						return null;
					}
				});
		assertTrue(resultHandlerFunction.isPresent());
		assertEquals(42, (int)resultHandlerFunction.get().entity());
	}


	private ServerResponse handlerMethod(ServerRequest request) {
		return ServerResponse.ok().body("42");
	}
}
