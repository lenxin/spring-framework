package org.springframework.http.client;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

/**
 * {@link ClientHttpRequest} implementation based on
 * Apache HttpComponents HttpClient.
 *
 * <p>Created via the {@link HttpComponentsClientHttpRequestFactory}.
 *
 * @author Oleg Kalnichevski
 * @author Arjen Poutsma
 * @author Juergen Hoeller
 * @since 3.1
 * @see HttpComponentsClientHttpRequestFactory#createRequest(URI, HttpMethod)
 */
final class HttpComponentsClientHttpRequest extends AbstractBufferingClientHttpRequest {

	private final HttpClient httpClient;

	private final HttpUriRequest httpRequest;

	private final HttpContext httpContext;


	HttpComponentsClientHttpRequest(HttpClient client, HttpUriRequest request, HttpContext context) {
		this.httpClient = client;
		this.httpRequest = request;
		this.httpContext = context;
	}


	@Override
	public String getMethodValue() {
		return this.httpRequest.getMethod();
	}

	@Override
	public URI getURI() {
		return this.httpRequest.getURI();
	}

	HttpContext getHttpContext() {
		return this.httpContext;
	}


	@Override
	protected ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException {
		addHeaders(this.httpRequest, headers);

		if (this.httpRequest instanceof HttpEntityEnclosingRequest) {
			HttpEntityEnclosingRequest entityEnclosingRequest = (HttpEntityEnclosingRequest) this.httpRequest;
			HttpEntity requestEntity = new ByteArrayEntity(bufferedOutput);
			entityEnclosingRequest.setEntity(requestEntity);
		}
		HttpResponse httpResponse = this.httpClient.execute(this.httpRequest, this.httpContext);
		return new HttpComponentsClientHttpResponse(httpResponse);
	}


	/**
	 * Add the given headers to the given HTTP request.
	 * @param httpRequest the request to add the headers to
	 * @param headers the headers to add
	 */
	static void addHeaders(HttpUriRequest httpRequest, HttpHeaders headers) {
		headers.forEach((headerName, headerValues) -> {
			if (HttpHeaders.COOKIE.equalsIgnoreCase(headerName)) {  // RFC 6265
				String headerValue = StringUtils.collectionToDelimitedString(headerValues, "; ");
				httpRequest.addHeader(headerName, headerValue);
			}
			else if (!HTTP.CONTENT_LEN.equalsIgnoreCase(headerName) &&
					!HTTP.TRANSFER_ENCODING.equalsIgnoreCase(headerName)) {
				for (String headerValue : headerValues) {
					httpRequest.addHeader(headerName, headerValue);
				}
			}
		});
	}

}
