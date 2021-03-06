package org.springframework.web.context.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * {@link org.springframework.beans.factory.config.BeanPostProcessor}
 * implementation that passes the ServletContext to beans that implement
 * the {@link ServletContextAware} interface.
 *
 * <p>Web application contexts will automatically register this with their
 * underlying bean factory. Applications do not use this directly.
 *
 * @see org.springframework.web.context.ServletContextAware
 * @see org.springframework.web.context.support.XmlWebApplicationContext#postProcessBeanFactory
 * @since 12.03.2004
 */
public class ServletContextAwareProcessor implements BeanPostProcessor {
	@Nullable
	private ServletContext servletContext;
	@Nullable
	private ServletConfig servletConfig;

	/**
	 * Create a new ServletContextAwareProcessor without an initial context or config.
	 * When this constructor is used the {@link #getServletContext()} and/or
	 * {@link #getServletConfig()} methods should be overridden.
	 */
	protected ServletContextAwareProcessor() {
	}

	/**
	 * Create a new ServletContextAwareProcessor for the given context.
	 */
	public ServletContextAwareProcessor(ServletContext servletContext) {
		this(servletContext, null);
	}

	/**
	 * Create a new ServletContextAwareProcessor for the given config.
	 */
	public ServletContextAwareProcessor(ServletConfig servletConfig) {
		this(null, servletConfig);
	}

	/**
	 * Create a new ServletContextAwareProcessor for the given context and config.
	 */
	public ServletContextAwareProcessor(@Nullable ServletContext servletContext, @Nullable ServletConfig servletConfig) {
		this.servletContext = servletContext;
		this.servletConfig = servletConfig;
	}

	/**
	 * Returns the {@link ServletContext} to be injected or {@code null}. This method
	 * can be overridden by subclasses when a context is obtained after the post-processor
	 * has been registered.
	 */
	@Nullable
	protected ServletContext getServletContext() {
		if (this.servletContext == null && getServletConfig() != null) {
			return getServletConfig().getServletContext();
		}
		return this.servletContext;
	}

	/**
	 * Returns the {@link ServletConfig} to be injected or {@code null}. This method
	 * can be overridden by subclasses when a context is obtained after the post-processor
	 * has been registered.
	 */
	@Nullable
	protected ServletConfig getServletConfig() {
		return this.servletConfig;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (getServletContext() != null && bean instanceof ServletContextAware) {
			((ServletContextAware) bean).setServletContext(getServletContext());
		}
		if (getServletConfig() != null && bean instanceof ServletConfigAware) {
			((ServletConfigAware) bean).setServletConfig(getServletConfig());
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) {
		return bean;
	}
}