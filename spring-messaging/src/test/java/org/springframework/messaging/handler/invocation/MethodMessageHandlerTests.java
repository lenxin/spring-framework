package org.springframework.messaging.handler.invocation;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.handler.DestinationPatternsMessageCondition;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.messaging.handler.annotation.support.MessageMethodArgumentResolver;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;

import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Test fixture for
 * {@link org.springframework.messaging.handler.invocation.AbstractMethodMessageHandler}.
 */
public class MethodMessageHandlerTests {

	private static final String DESTINATION_HEADER = "destination";

	private TestMethodMessageHandler messageHandler;

	private TestController testController;


	@Before
	public void setup() {

		List<String> destinationPrefixes = Arrays.asList("/test");

		this.messageHandler = new TestMethodMessageHandler();
		this.messageHandler.setApplicationContext(new StaticApplicationContext());
		this.messageHandler.setDestinationPrefixes(destinationPrefixes);
		this.messageHandler.afterPropertiesSet();

		this.testController = new TestController();
		this.messageHandler.registerHandler(this.testController);
	}

	@Test(expected = IllegalStateException.class)
	public void duplicateMapping() {
		this.messageHandler.registerHandler(new DuplicateMappingsController());
	}

	@Test
	public void registeredMappings() {

		Map<String, HandlerMethod> handlerMethods = this.messageHandler.getHandlerMethods();

		assertNotNull(handlerMethods);
		assertThat(handlerMethods.keySet(), Matchers.hasSize(3));
	}

	@Test
	public void patternMatch() throws Exception {

		Method method = this.testController.getClass().getMethod("handlerPathMatchWildcard");
		this.messageHandler.registerHandlerMethod(this.testController, method, "/handlerPathMatch*");

		this.messageHandler.handleMessage(toDestination("/test/handlerPathMatchFoo"));

		assertEquals("pathMatchWildcard", this.testController.method);
	}

	@Test
	public void bestMatch() throws Exception {

		Method method = this.testController.getClass().getMethod("bestMatch");
		this.messageHandler.registerHandlerMethod(this.testController, method, "/bestmatch/{foo}/path");

		method = this.testController.getClass().getMethod("secondBestMatch");
		this.messageHandler.registerHandlerMethod(this.testController, method, "/bestmatch/*/*");

		this.messageHandler.handleMessage(toDestination("/test/bestmatch/bar/path"));

		assertEquals("bestMatch", this.testController.method);
	}

	@Test
	public void argumentResolution() {

		this.messageHandler.handleMessage(toDestination("/test/handlerArgumentResolver"));

		assertEquals("handlerArgumentResolver", this.testController.method);
		assertNotNull(this.testController.arguments.get("message"));
	}

	@Test
	public void handleException() {

		this.messageHandler.handleMessage(toDestination("/test/handlerThrowsExc"));

		assertEquals("illegalStateException", this.testController.method);
		assertNotNull(this.testController.arguments.get("exception"));
	}

	private Message<?> toDestination(String destination) {
		return MessageBuilder.withPayload(new byte[0]).setHeader(DESTINATION_HEADER, destination).build();
	}


	@SuppressWarnings("unused")
	private static class TestController {

		public String method;

		private Map<String, Object> arguments = new LinkedHashMap<>();

		public void handlerPathMatchWildcard() {
			this.method = "pathMatchWildcard";
		}

		@SuppressWarnings("rawtypes")
		public void handlerArgumentResolver(Message message) {
			this.method = "handlerArgumentResolver";
			this.arguments.put("message", message);
		}

		public void handlerThrowsExc() {
			throw new IllegalStateException();
		}

		public void bestMatch() {
			this.method = "bestMatch";
		}

		public void secondBestMatch() {
			this.method = "secondBestMatch";
		}

		public void handleIllegalStateException(IllegalStateException exception) {
			this.method = "illegalStateException";
			this.arguments.put("exception", exception);
		}

	}

	@SuppressWarnings("unused")
	private static class DuplicateMappingsController {

		public void handlerFoo() {
		}

		public void handlerFoo(String arg) {
		}
	}


	private static class TestMethodMessageHandler extends AbstractMethodMessageHandler<String> {

		private PathMatcher pathMatcher = new AntPathMatcher();


		public void registerHandler(Object handler) {
			super.detectHandlerMethods(handler);
		}

		public void registerHandlerMethod(Object handler, Method method, String mapping) {
			super.registerHandlerMethod(handler, method, mapping);
		}

		@Override
		protected List<? extends HandlerMethodArgumentResolver> initArgumentResolvers() {
			List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
			resolvers.add(new MessageMethodArgumentResolver(new SimpleMessageConverter()));
			resolvers.addAll(getCustomArgumentResolvers());
			return resolvers;
		}

		@Override
		protected List<? extends HandlerMethodReturnValueHandler> initReturnValueHandlers() {
			List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>();
			handlers.addAll(getCustomReturnValueHandlers());
			return handlers;
		}

		@Override
		protected boolean isHandler(Class<?> beanType) {
			return beanType.getName().contains("Controller");
		}

		@Override
		protected String getMappingForMethod(Method method, Class<?> handlerType) {
			String methodName = method.getName();
			if (methodName.startsWith("handler")) {
				return "/" + methodName;
			}
			return null;
		}

		@Override
		protected Set<String> getDirectLookupDestinations(String mapping) {
			Set<String> result = new LinkedHashSet<>();
			if (!this.pathMatcher.isPattern(mapping)) {
				result.add(mapping);
			}
			return result;
		}

		@Override
		protected String getDestination(Message<?> message) {
			return (String) message.getHeaders().get(DESTINATION_HEADER);
		}

		@Override
		protected String getMatchingMapping(String mapping, Message<?> message) {
			String destination = getLookupDestination(getDestination(message));
			Assert.notNull(destination, "No destination");
			return mapping.equals(destination) || this.pathMatcher.match(mapping, destination) ? mapping : null;
		}

		@Override
		protected Comparator<String> getMappingComparator(final Message<?> message) {
			return (info1, info2) -> {
				DestinationPatternsMessageCondition cond1 = new DestinationPatternsMessageCondition(info1);
				DestinationPatternsMessageCondition cond2 = new DestinationPatternsMessageCondition(info2);
				return cond1.compareTo(cond2, message);
			};
		}

		@Override
		protected AbstractExceptionHandlerMethodResolver createExceptionHandlerMethodResolverFor(Class<?> beanType) {
			return new TestExceptionResolver(beanType);
		}
	}

}
