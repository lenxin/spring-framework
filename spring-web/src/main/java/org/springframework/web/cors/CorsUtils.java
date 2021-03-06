package org.springframework.web.cors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Utility class for CORS request handling based on the
 * <a href="https://www.w3.org/TR/cors/">CORS W3C recommendation</a>.
 *
 * @author Sebastien Deleuze
 * @since 4.2
 */
public abstract class CorsUtils {

	/**
	 * Returns {@code true} if the request is a valid CORS one by checking {@code Origin}
	 * header presence and ensuring that origins are different.
	 */
	public static boolean isCorsRequest(HttpServletRequest request) {
		String origin = request.getHeader(HttpHeaders.ORIGIN);
		if (origin == null) {
			return false;
		}
		UriComponents originUrl = UriComponentsBuilder.fromOriginHeader(origin).build();
		String scheme = request.getScheme();
		String host = request.getServerName();
		int port = request.getServerPort();
		return !(ObjectUtils.nullSafeEquals(scheme, originUrl.getScheme()) &&
				ObjectUtils.nullSafeEquals(host, originUrl.getHost()) &&
				getPort(scheme, port) == getPort(originUrl.getScheme(), originUrl.getPort()));

	}

	private static int getPort(@Nullable String scheme, int port) {
		if (port == -1) {
			if ("http".equals(scheme) || "ws".equals(scheme)) {
				port = 80;
			}
			else if ("https".equals(scheme) || "wss".equals(scheme)) {
				port = 443;
			}
		}
		return port;
	}

	/**
	 * Returns {@code true} if the request is a valid CORS pre-flight one.
	 * To be used in combination with {@link #isCorsRequest(HttpServletRequest)} since
	 * regular CORS checks are not invoked here for performance reasons.
	 */
	public static boolean isPreFlightRequest(HttpServletRequest request) {
		return (HttpMethod.OPTIONS.matches(request.getMethod()) &&
				request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD) != null);
	}

}
