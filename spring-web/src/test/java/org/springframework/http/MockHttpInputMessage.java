package org.springframework.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.util.Assert;

/**
 * Mock implementation of {@link HttpInputMessage}.
 *
 * @author Rossen Stoyanchev
 * @since 3.2
 */
public class MockHttpInputMessage implements HttpInputMessage {

	private final HttpHeaders headers = new HttpHeaders();

	private final InputStream body;


	public MockHttpInputMessage(byte[] contents) {
		Assert.notNull(contents, "'contents' must not be null");
		this.body = new ByteArrayInputStream(contents);
	}

	public MockHttpInputMessage(InputStream body) {
		Assert.notNull(body, "'body' must not be null");
		this.body = body;
	}


	@Override
	public HttpHeaders getHeaders() {
		return headers;
	}

	@Override
	public InputStream getBody() throws IOException {
		return body;
	}

}
