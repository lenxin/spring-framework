package org.springframework.web.reactive.result.view.script;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.test.MockServerHttpRequest;
import org.springframework.mock.http.server.reactive.test.MockServerHttpResponse;
import org.springframework.mock.web.test.server.MockServerWebExchange;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for Kotlin script templates running on Kotlin JSR-223 support.
 *
 * @author Sebastien Deleuze
 */
public class KotlinScriptTemplateTests {

	@Ignore
	@Test
	public void renderTemplateWithFrenchLocale() throws Exception {
		Map<String, Object> model = new HashMap<>();
		model.put("foo", "Foo");
		String url = "org/springframework/web/reactive/result/view/script/kotlin/template.kts";
		MockServerHttpResponse response = render(url, model, Locale.FRENCH, ScriptTemplatingConfiguration.class);
		assertEquals("<html><body>\n<p>Bonjour Foo</p>\n</body></html>", response.getBodyAsString().block());
	}

	@Ignore
	@Test
	public void renderTemplateWithEnglishLocale() throws Exception {
		Map<String, Object> model = new HashMap<>();
		model.put("foo", "Foo");
		String url = "org/springframework/web/reactive/result/view/script/kotlin/template.kts";
		MockServerHttpResponse response = render(url, model, Locale.ENGLISH, ScriptTemplatingConfiguration.class);
		assertEquals("<html><body>\n<p>Hello Foo</p>\n</body></html>", response.getBodyAsString().block());
	}

	@Ignore
	@Test
	public void renderTemplateWithoutRenderFunction() throws Exception {
		Map<String, Object> model = new HashMap<>();
		model.put("header", "<html><body>");
		model.put("hello", "Hello");
		model.put("foo", "Foo");
		model.put("footer", "</body></html>");
		String url = "org/springframework/web/reactive/result/view/script/kotlin/eval.kts";
		Class<?> configClass = ScriptTemplatingConfigurationWithoutRenderFunction.class;
		MockServerHttpResponse response = render(url, model, Locale.ENGLISH, configClass);
		assertEquals("<html><body>\n<p>Hello Foo</p>\n</body></html>",
				response.getBodyAsString().block());
	}


	private MockServerHttpResponse render(String viewUrl, Map<String, Object> model,
			Locale locale, Class<?> configuration) throws Exception {

		ScriptTemplateView view = createViewWithUrl(viewUrl, configuration);
		MockServerHttpRequest request = MockServerHttpRequest.get("/").acceptLanguageAsLocales(locale).build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);
		view.renderInternal(model, MediaType.TEXT_HTML, exchange).block();
		return exchange.getResponse();
	}

	private ScriptTemplateView createViewWithUrl(String viewUrl, Class<?> configuration) throws Exception {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(configuration);
		ctx.refresh();

		ScriptTemplateView view = new ScriptTemplateView();
		view.setApplicationContext(ctx);
		view.setUrl(viewUrl);
		view.afterPropertiesSet();
		return view;
	}


	@Configuration
	static class ScriptTemplatingConfiguration {

		@Bean
		public ScriptTemplateConfigurer kotlinScriptConfigurer() {
			ScriptTemplateConfigurer configurer = new ScriptTemplateConfigurer();
			configurer.setEngineName("kotlin");
			configurer.setScripts("org/springframework/web/reactive/result/view/script/kotlin/render.kts");
			configurer.setRenderFunction("render");
			return configurer;
		}

		@Bean
		public ResourceBundleMessageSource messageSource() {
			ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
			messageSource.setBasename("org/springframework/web/reactive/result/view/script/messages");
			return messageSource;
		}
	}


	@Configuration
	static class ScriptTemplatingConfigurationWithoutRenderFunction {

		@Bean
		public ScriptTemplateConfigurer kotlinScriptConfigurer() {
			return new ScriptTemplateConfigurer("kotlin");
		}
	}

}
