

package org.springframework.web.servlet.function;

import java.io.File;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

import org.junit.Test;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.test.MockHttpServletRequest;

import static org.junit.Assert.*;

/**
 * @author Arjen Poutsma
 */
public class PathResourceLookupFunctionTests {

	@Test
	public void normal() throws Exception {
		ClassPathResource location =
				new ClassPathResource("org/springframework/web/servlet/function/");

		PathResourceLookupFunction function =
				new PathResourceLookupFunction("/resources/**", location);

		MockHttpServletRequest servletRequest =
				new MockHttpServletRequest("GET", "/resources/response.txt");
		ServerRequest request = new DefaultServerRequest(servletRequest, Collections.emptyList());

		Optional<Resource> result = function.apply(request);
		assertTrue(result.isPresent());

		File expected = new ClassPathResource("response.txt", getClass()).getFile();
		assertEquals(expected, result.get().getFile());
	}

	@Test
	public void subPath() throws Exception {
		ClassPathResource location =
				new ClassPathResource("org/springframework/web/servlet/function/");

		PathResourceLookupFunction function =
				new PathResourceLookupFunction("/resources/**", location);

		MockHttpServletRequest servletRequest =
				new MockHttpServletRequest("GET", "/resources/child/response.txt");
		ServerRequest request = new DefaultServerRequest(servletRequest, Collections.emptyList());

		Optional<Resource> result = function.apply(request);
		assertTrue(result.isPresent());

		File expected =
				new ClassPathResource("org/springframework/web/servlet/function/child/response.txt")
						.getFile();
		assertEquals(expected, result.get().getFile());
	}

	@Test
	public void notFound() {
		ClassPathResource location =
				new ClassPathResource("org/springframework/web/reactive/function/server/");

		PathResourceLookupFunction function =
				new PathResourceLookupFunction("/resources/**", location);

		MockHttpServletRequest servletRequest =
				new MockHttpServletRequest("GET", "/resources/foo.txt");
		ServerRequest request = new DefaultServerRequest(servletRequest, Collections.emptyList());

		Optional<Resource> result = function.apply(request);
		assertFalse(result.isPresent());
	}

	@Test
	public void composeResourceLookupFunction() throws Exception {
		ClassPathResource defaultResource = new ClassPathResource("response.txt", getClass());

		Function<ServerRequest, Optional<Resource>> lookupFunction =
				new PathResourceLookupFunction("/resources/**",
						new ClassPathResource("org/springframework/web/servlet/function/"));

		Function<ServerRequest, Optional<Resource>> customLookupFunction =
				lookupFunction.andThen((Optional<Resource> optionalResource) -> {
					if (optionalResource.isPresent()) {
						return optionalResource;
					}
					else {
						return Optional.of(defaultResource);
					}
				});

		MockHttpServletRequest servletRequest = new MockHttpServletRequest("GET", "/resources/foo");
		ServerRequest request = new DefaultServerRequest(servletRequest, Collections.emptyList());

		Optional<Resource> result = customLookupFunction.apply(request);
		assertTrue(result.isPresent());

		assertEquals(defaultResource.getFile(), result.get().getFile());
	}

}
