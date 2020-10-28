package org.springframework.web.reactive.result.method;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import reactor.core.publisher.MonoProcessor;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.server.ServerWebExchange;

/**
 * Extension of {@link HandlerMethod} that invokes the underlying method via
 * {@link InvocableHandlerMethod} but uses sync argument resolvers only and
 * thus can return directly a {@link HandlerResult} with no async wrappers.
 *

 * @since 5.0
 */
public class SyncInvocableHandlerMethod extends HandlerMethod {

	private final InvocableHandlerMethod delegate;


	public SyncInvocableHandlerMethod(HandlerMethod handlerMethod) {
		super(handlerMethod);
		this.delegate = new InvocableHandlerMethod(handlerMethod);
	}

	public SyncInvocableHandlerMethod(Object bean, Method method) {
		super(bean, method);
		this.delegate = new InvocableHandlerMethod(bean, method);
	}


	/**
	 * Configure the argument resolvers to use to use for resolving method
	 * argument values against a {@code ServerWebExchange}.
	 */
	public void setArgumentResolvers(List<SyncHandlerMethodArgumentResolver> resolvers) {
		this.delegate.setArgumentResolvers(new ArrayList<>(resolvers));
	}

	/**
	 * Return the configured argument resolvers.
	 */
	public List<SyncHandlerMethodArgumentResolver> getResolvers() {
		return this.delegate.getResolvers().stream()
				.map(resolver -> (SyncHandlerMethodArgumentResolver) resolver)
				.collect(Collectors.toList());
	}

	/**
	 * Set the ParameterNameDiscoverer for resolving parameter names when needed
	 * (e.g. default request attribute name).
	 * <p>Default is a {@link DefaultParameterNameDiscoverer}.
	 */
	public void setParameterNameDiscoverer(ParameterNameDiscoverer nameDiscoverer) {
		this.delegate.setParameterNameDiscoverer(nameDiscoverer);
	}

	/**
	 * Return the configured parameter name discoverer.
	 */
	public ParameterNameDiscoverer getParameterNameDiscoverer() {
		return this.delegate.getParameterNameDiscoverer();
	}


	/**
	 * Invoke the method for the given exchange.
	 * @param exchange the current exchange
	 * @param bindingContext the binding context to use
	 * @param providedArgs optional list of argument values to match by type
	 * @return a Mono with a {@link HandlerResult}.
	 * @throws ServerErrorException if method argument resolution or method invocation fails
	 */
	@Nullable
	public HandlerResult invokeForHandlerResult(ServerWebExchange exchange,
			BindingContext bindingContext, Object... providedArgs) {

		MonoProcessor<HandlerResult> processor = MonoProcessor.create();
		this.delegate.invoke(exchange, bindingContext, providedArgs).subscribeWith(processor);

		if (processor.isTerminated()) {
			Throwable ex = processor.getError();
			if (ex != null) {
				throw (ex instanceof ServerErrorException ? (ServerErrorException) ex :
						new ServerErrorException("Failed to invoke: " + getShortLogMessage(), getMethod(), ex));
			}
			return processor.peek();
		}
		else {
			// Should never happen...
			throw new IllegalStateException(
					"SyncInvocableHandlerMethod should have completed synchronously.");
		}
	}

}
