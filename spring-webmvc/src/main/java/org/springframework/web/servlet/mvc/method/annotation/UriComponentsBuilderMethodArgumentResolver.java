package org.springframework.web.servlet.mvc.method.annotation;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;


/**
 * Resolvers argument values of type {@link UriComponentsBuilder}.
 *
 * <p>The returned instance is initialized via
 * {@link ServletUriComponentsBuilder#fromServletMapping(HttpServletRequest)}.
 *
 * @author Rossen Stoyanchev
 * @since 3.1
 */
public class UriComponentsBuilderMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> type = parameter.getParameterType();
		return (UriComponentsBuilder.class == type || ServletUriComponentsBuilder.class == type);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {

		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		Assert.state(request != null, "No HttpServletRequest");
		return ServletUriComponentsBuilder.fromServletMapping(request);
	}

}
