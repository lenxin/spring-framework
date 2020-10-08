package org.springframework.web.context.support;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import javax.servlet.ServletContext;

/**
 * ResourceLoader implementation that resolves paths as ServletContext
 * resources, for use outside a WebApplicationContext (for example,
 * in an HttpServletBean or GenericFilterBean subclass).
 *
 * <p>Within a WebApplicationContext, resource paths are automatically
 * resolved as ServletContext resources by the context implementation.
 *
 * @see #getResourceByPath
 * @see ServletContextResource
 * @see org.springframework.web.context.WebApplicationContext
 * @see org.springframework.web.servlet.HttpServletBean
 * @see org.springframework.web.filter.GenericFilterBean
 * @since 1.0.2
 */
public class ServletContextResourceLoader extends DefaultResourceLoader {
	private final ServletContext servletContext;

	/**
	 * Create a new ServletContextResourceLoader.
	 *
	 * @param servletContext the ServletContext to load resources with
	 */
	public ServletContextResourceLoader(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/**
	 * This implementation supports file paths beneath the root of the web application.
	 *
	 * @see ServletContextResource
	 */
	@Override
	protected Resource getResourceByPath(String path) {
		return new ServletContextResource(this.servletContext, path);
	}
}
