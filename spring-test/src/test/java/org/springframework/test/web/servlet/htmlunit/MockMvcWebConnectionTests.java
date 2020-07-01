package org.springframework.test.web.servlet.htmlunit;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import org.junit.Test;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Integration tests for {@link MockMvcWebConnection}.
 *
 * @author Rob Winch
 * @since 4.2
 */
public class MockMvcWebConnectionTests {

	private final WebClient webClient = new WebClient();

	private final MockMvc mockMvc =
			MockMvcBuilders.standaloneSetup(new HelloController(), new ForwardController()).build();


	@Test
	public void contextPathNull() throws IOException {
		this.webClient.setWebConnection(new MockMvcWebConnection(this.mockMvc, this.webClient, null));
		Page page = this.webClient.getPage("http://localhost/context/a");
		assertThat(page.getWebResponse().getStatusCode(), equalTo(200));
	}

	@Test
	public void contextPathExplicit() throws IOException {
		this.webClient.setWebConnection(new MockMvcWebConnection(this.mockMvc, this.webClient, "/context"));
		Page page = this.webClient.getPage("http://localhost/context/a");
		assertThat(page.getWebResponse().getStatusCode(), equalTo(200));
	}

	@Test
	public void contextPathEmpty() throws IOException {
		this.webClient.setWebConnection(new MockMvcWebConnection(this.mockMvc, this.webClient, ""));
		try {
			this.webClient.getPage("http://localhost/context/a");
			fail("Empty context path (root context) should not match to a URL with a context path");
		}
		catch (FailingHttpStatusCodeException ex) {
			assertEquals(404, ex.getStatusCode());
		}
		this.webClient.setWebConnection(new MockMvcWebConnection(this.mockMvc, this.webClient));
		try {
			this.webClient.getPage("http://localhost/context/a");
			fail("No context is the same providing an empty context path");
		}
		catch (FailingHttpStatusCodeException ex) {
			assertEquals(404, ex.getStatusCode());
		}
	}

	@Test
	public void forward() throws IOException {
		this.webClient.setWebConnection(new MockMvcWebConnection(this.mockMvc, this.webClient, ""));
		Page page = this.webClient.getPage("http://localhost/forward");
		assertThat(page.getWebResponse().getContentAsString(), equalTo("hello"));
	}

	@Test(expected = IllegalArgumentException.class)
	@SuppressWarnings("resource")
	public void contextPathDoesNotStartWithSlash() throws IOException {
		new MockMvcWebConnection(this.mockMvc, this.webClient, "context");
	}

	@Test(expected = IllegalArgumentException.class)
	@SuppressWarnings("resource")
	public void contextPathEndsWithSlash() throws IOException {
		new MockMvcWebConnection(this.mockMvc, this.webClient, "/context/");
	}

}
