package org.springframework.web.reactive.config;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.context.support.StaticApplicationContext;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.reactive.accept.RequestedContentTypeResolverBuilder;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

/**
 * Test fixture for {@link DelegatingWebFluxConfiguration} tests.
 *

 */
public class DelegatingWebFluxConfigurationTests {

	private DelegatingWebFluxConfiguration delegatingConfig;

	@Mock
	private WebFluxConfigurer webFluxConfigurer;

	@Captor
	private ArgumentCaptor<ServerCodecConfigurer> codecsConfigurer;

	@Captor
	private ArgumentCaptor<List<HttpMessageWriter<?>>> writers;

	@Captor
	private ArgumentCaptor<FormatterRegistry> formatterRegistry;


	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		delegatingConfig = new DelegatingWebFluxConfiguration();
		delegatingConfig.setApplicationContext(new StaticApplicationContext());
		given(webFluxConfigurer.getValidator()).willReturn(null);
		given(webFluxConfigurer.getMessageCodesResolver()).willReturn(null);
	}


	@Test
	public void requestMappingHandlerMapping() throws Exception {
		delegatingConfig.setConfigurers(Collections.singletonList(webFluxConfigurer));
		delegatingConfig.requestMappingHandlerMapping(delegatingConfig.webFluxContentTypeResolver());

		verify(webFluxConfigurer).configureContentTypeResolver(any(RequestedContentTypeResolverBuilder.class));
		verify(webFluxConfigurer).addCorsMappings(any(CorsRegistry.class));
		verify(webFluxConfigurer).configurePathMatching(any(PathMatchConfigurer.class));
	}

	@Test
	public void requestMappingHandlerAdapter() throws Exception {
		delegatingConfig.setConfigurers(Collections.singletonList(webFluxConfigurer));
		ReactiveAdapterRegistry reactiveAdapterRegistry = delegatingConfig.webFluxAdapterRegistry();
		ServerCodecConfigurer serverCodecConfigurer = delegatingConfig.serverCodecConfigurer();
		FormattingConversionService formattingConversionService = delegatingConfig.webFluxConversionService();
		Validator validator = delegatingConfig.webFluxValidator();

		ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer)
				this.delegatingConfig.requestMappingHandlerAdapter(reactiveAdapterRegistry, serverCodecConfigurer,
						formattingConversionService, validator).getWebBindingInitializer();

		verify(webFluxConfigurer).configureHttpMessageCodecs(codecsConfigurer.capture());
		verify(webFluxConfigurer).getValidator();
		verify(webFluxConfigurer).getMessageCodesResolver();
		verify(webFluxConfigurer).addFormatters(formatterRegistry.capture());
		verify(webFluxConfigurer).configureArgumentResolvers(any());

		assertNotNull(initializer);
		assertTrue(initializer.getValidator() instanceof LocalValidatorFactoryBean);
		assertSame(formatterRegistry.getValue(), initializer.getConversionService());
		assertEquals(13, codecsConfigurer.getValue().getReaders().size());
	}

	@Test
	public void resourceHandlerMapping() throws Exception {
		delegatingConfig.setConfigurers(Collections.singletonList(webFluxConfigurer));
		doAnswer(invocation -> {
			ResourceHandlerRegistry registry = invocation.getArgument(0);
			registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static");
			return null;
		}).when(webFluxConfigurer).addResourceHandlers(any(ResourceHandlerRegistry.class));

		delegatingConfig.resourceHandlerMapping(delegatingConfig.resourceUrlProvider());
		verify(webFluxConfigurer).addResourceHandlers(any(ResourceHandlerRegistry.class));
		verify(webFluxConfigurer).configurePathMatching(any(PathMatchConfigurer.class));
	}

	@Test
	public void responseBodyResultHandler() throws Exception {
		delegatingConfig.setConfigurers(Collections.singletonList(webFluxConfigurer));
		delegatingConfig.responseBodyResultHandler(
				delegatingConfig.webFluxAdapterRegistry(),
				delegatingConfig.serverCodecConfigurer(),
				delegatingConfig.webFluxContentTypeResolver());

		verify(webFluxConfigurer).configureHttpMessageCodecs(codecsConfigurer.capture());
		verify(webFluxConfigurer).configureContentTypeResolver(any(RequestedContentTypeResolverBuilder.class));
	}

	@Test
	public void viewResolutionResultHandler() throws Exception {
		delegatingConfig.setConfigurers(Collections.singletonList(webFluxConfigurer));
		delegatingConfig.viewResolutionResultHandler(delegatingConfig.webFluxAdapterRegistry(),
				delegatingConfig.webFluxContentTypeResolver());

		verify(webFluxConfigurer).configureViewResolvers(any(ViewResolverRegistry.class));
	}

}
