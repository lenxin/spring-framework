package org.springframework.web.servlet.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.test.MockHttpServletRequest;
import org.springframework.mock.web.test.MockHttpServletResponse;

import static org.junit.Assert.*;

/**
 * Unit tests for {@code ResourceUrlEncodingFilter}.
 *
 * @author Brian Clozel
 */
public class ResourceUrlEncodingFilterTests {

	private ResourceUrlEncodingFilter filter;

	private ResourceUrlProvider urlProvider;


	@Before
	public void createFilter() {
		VersionResourceResolver versionResolver = new VersionResourceResolver();
		versionResolver.setStrategyMap(Collections.singletonMap("/**", new ContentVersionStrategy()));
		PathResourceResolver pathResolver = new PathResourceResolver();
		pathResolver.setAllowedLocations(new ClassPathResource("test/", getClass()));
		List<ResourceResolver> resolvers = new ArrayList<>();
		resolvers.add(versionResolver);
		resolvers.add(pathResolver);

		this.filter = new ResourceUrlEncodingFilter();
		this.urlProvider = createResourceUrlProvider(resolvers);
	}

	private ResourceUrlProvider createResourceUrlProvider(List<ResourceResolver> resolvers) {
		ResourceHttpRequestHandler handler = new ResourceHttpRequestHandler();
		handler.setLocations(Collections.singletonList(new ClassPathResource("test/", getClass())));
		handler.setResourceResolvers(resolvers);

		ResourceUrlProvider urlProvider = new ResourceUrlProvider();
		urlProvider.setHandlerMap(Collections.singletonMap("/resources/**", handler));
		return urlProvider;
	}


	@Test
	public void encodeURL() throws Exception {
		testEncodeUrl(new MockHttpServletRequest("GET", "/"),
				"/resources/bar.css", "/resources/bar-11e16cf79faee7ac698c805cf28248d2.css");
	}

	@Test
	public void encodeUrlWithContext() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/context/foo");
		request.setContextPath("/context");

		testEncodeUrl(request, "/context/resources/bar.css",
				"/context/resources/bar-11e16cf79faee7ac698c805cf28248d2.css");
	}


	@Test
	public void encodeUrlWithContextAndForwardedRequest() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/context/foo");
		request.setContextPath("/context");

		this.filter.doFilter(request, new MockHttpServletResponse(), (req, res) -> {
			req.setAttribute(ResourceUrlProviderExposingInterceptor.RESOURCE_URL_PROVIDER_ATTR, this.urlProvider);
			request.setRequestURI("/forwarded");
			request.setContextPath("/");
			String result = ((HttpServletResponse) res).encodeURL("/context/resources/bar.css");
			assertEquals("/context/resources/bar-11e16cf79faee7ac698c805cf28248d2.css", result);
		});
	}

	@Test // SPR-13757
	public void encodeContextPathUrlWithoutSuffix() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/context");
		request.setContextPath("/context");

		testEncodeUrl(request, "/context/resources/bar.css",
				"/context/resources/bar-11e16cf79faee7ac698c805cf28248d2.css");
	}

	@Test
	public void encodeContextPathUrlWithSuffix() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/context/");
		request.setContextPath("/context");

		testEncodeUrl(request, "/context/resources/bar.css",
				"/context/resources/bar-11e16cf79faee7ac698c805cf28248d2.css");
	}

	@Test // SPR-13018
	public void encodeEmptyUrlWithContext() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/context/foo");
		request.setContextPath("/context");

		testEncodeUrl(request, "?foo=1", "?foo=1");
	}

	@Test // SPR-13374
	public void encodeUrlWithRequestParams() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/foo");
		request.setContextPath("/");

		testEncodeUrl(request, "/resources/bar.css?foo=bar&url=https://example.org",
				"/resources/bar-11e16cf79faee7ac698c805cf28248d2.css?foo=bar&url=https://example.org");
	}

	@Test // SPR-13847
	public void encodeUrlPreventStringOutOfBounds() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/context-path/index");
		request.setContextPath("/context-path");
		request.setServletPath("");

		testEncodeUrl(request, "index?key=value", "index?key=value");
	}

	@Test // SPR-17535
	public void encodeUrlWithFragment() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/foo");
		request.setContextPath("/");

		testEncodeUrl(request, "/resources/bar.css#something",
				"/resources/bar-11e16cf79faee7ac698c805cf28248d2.css#something");

		testEncodeUrl(request,
				"/resources/bar.css?foo=bar&url=https://example.org#something",
				"/resources/bar-11e16cf79faee7ac698c805cf28248d2.css?foo=bar&url=https://example.org#something");
	}

	private void testEncodeUrl(MockHttpServletRequest request, String url, String expected)
			throws ServletException, IOException {

		this.filter.doFilter(request, new MockHttpServletResponse(), (req, res) -> {
			req.setAttribute(ResourceUrlProviderExposingInterceptor.RESOURCE_URL_PROVIDER_ATTR, this.urlProvider);
			String result = ((HttpServletResponse) res).encodeURL(url);
			assertEquals(expected, result);
		});
	}

}
