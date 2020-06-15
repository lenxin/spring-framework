

package org.springframework.web.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import org.springframework.mock.web.test.MockHttpServletRequest;
import org.springframework.mock.web.test.MockHttpServletResponse;

import static org.junit.Assert.*;

/**
 * Tests for {@link HiddenHttpMethodFilter}.
 *
 * @author Arjen Poutsma
 * @author Brian Clozel
 */
public class HiddenHttpMethodFilterTests {

	private final HiddenHttpMethodFilter filter = new HiddenHttpMethodFilter();

	@Test
	public void filterWithParameter() throws IOException, ServletException {
		filterWithParameterForMethod("delete", "DELETE");
		filterWithParameterForMethod("put", "PUT");
		filterWithParameterForMethod("patch", "PATCH");
	}

	@Test
	public void filterWithParameterDisallowedMethods() throws IOException, ServletException {
		filterWithParameterForMethod("trace", "POST");
		filterWithParameterForMethod("head", "POST");
		filterWithParameterForMethod("options", "POST");
	}

	@Test
	public void filterWithNoParameter() throws IOException, ServletException {
		filterWithParameterForMethod(null, "POST");
	}

	private void filterWithParameterForMethod(String methodParam, String expectedMethod)
			throws IOException, ServletException {
		MockHttpServletRequest request = new MockHttpServletRequest("POST", "/hotels");
		if(methodParam != null) {
			request.addParameter("_method", methodParam);
		}
		MockHttpServletResponse response = new MockHttpServletResponse();

		FilterChain filterChain = new FilterChain() {

			@Override
			public void doFilter(ServletRequest filterRequest,
					ServletResponse filterResponse) throws IOException, ServletException {
				assertEquals("Invalid method", expectedMethod,
						((HttpServletRequest) filterRequest).getMethod());
			}
		};
		this.filter.doFilter(request, response, filterChain);
	}

}
