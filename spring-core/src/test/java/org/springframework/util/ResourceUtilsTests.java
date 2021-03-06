package org.springframework.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Juergen Hoeller
 */
public class ResourceUtilsTests {

	@Test
	public void isJarURL() throws Exception {
		assertTrue(ResourceUtils.isJarURL(new URL("jar:file:myjar.jar!/mypath")));
		assertTrue(ResourceUtils.isJarURL(new URL(null, "zip:file:myjar.jar!/mypath", new DummyURLStreamHandler())));
		assertTrue(ResourceUtils.isJarURL(new URL(null, "wsjar:file:myjar.jar!/mypath", new DummyURLStreamHandler())));
		assertTrue(ResourceUtils.isJarURL(new URL(null, "jar:war:file:mywar.war*/myjar.jar!/mypath", new DummyURLStreamHandler())));
		assertFalse(ResourceUtils.isJarURL(new URL("file:myjar.jar")));
		assertFalse(ResourceUtils.isJarURL(new URL("http:myserver/myjar.jar")));
	}

	@Test
	public void extractJarFileURL() throws Exception {
		assertEquals(new URL("file:myjar.jar"),
				ResourceUtils.extractJarFileURL(new URL("jar:file:myjar.jar!/mypath")));
		assertEquals(new URL("file:/myjar.jar"),
				ResourceUtils.extractJarFileURL(new URL(null, "jar:myjar.jar!/mypath", new DummyURLStreamHandler())));
		assertEquals(new URL("file:myjar.jar"),
				ResourceUtils.extractJarFileURL(new URL(null, "zip:file:myjar.jar!/mypath", new DummyURLStreamHandler())));
		assertEquals(new URL("file:myjar.jar"),
				ResourceUtils.extractJarFileURL(new URL(null, "wsjar:file:myjar.jar!/mypath", new DummyURLStreamHandler())));

		assertEquals(new URL("file:myjar.jar"),
				ResourceUtils.extractJarFileURL(new URL("file:myjar.jar")));
		assertEquals(new URL("file:myjar.jar"),
				ResourceUtils.extractJarFileURL(new URL("jar:file:myjar.jar!/")));
		assertEquals(new URL("file:myjar.jar"),
				ResourceUtils.extractJarFileURL(new URL(null, "zip:file:myjar.jar!/", new DummyURLStreamHandler())));
		assertEquals(new URL("file:myjar.jar"),
				ResourceUtils.extractJarFileURL(new URL(null, "wsjar:file:myjar.jar!/", new DummyURLStreamHandler())));
	}

	@Test
	public void extractArchiveURL() throws Exception {
		assertEquals(new URL("file:myjar.jar"),
				ResourceUtils.extractArchiveURL(new URL("jar:file:myjar.jar!/mypath")));
		assertEquals(new URL("file:/myjar.jar"),
				ResourceUtils.extractArchiveURL(new URL(null, "jar:myjar.jar!/mypath", new DummyURLStreamHandler())));
		assertEquals(new URL("file:myjar.jar"),
				ResourceUtils.extractArchiveURL(new URL(null, "zip:file:myjar.jar!/mypath", new DummyURLStreamHandler())));
		assertEquals(new URL("file:myjar.jar"),
				ResourceUtils.extractArchiveURL(new URL(null, "wsjar:file:myjar.jar!/mypath", new DummyURLStreamHandler())));
		assertEquals(new URL("file:mywar.war"),
				ResourceUtils.extractArchiveURL(new URL(null, "jar:war:file:mywar.war*/myjar.jar!/mypath", new DummyURLStreamHandler())));

		assertEquals(new URL("file:myjar.jar"),
				ResourceUtils.extractArchiveURL(new URL("file:myjar.jar")));
		assertEquals(new URL("file:myjar.jar"),
				ResourceUtils.extractArchiveURL(new URL("jar:file:myjar.jar!/")));
		assertEquals(new URL("file:myjar.jar"),
				ResourceUtils.extractArchiveURL(new URL(null, "zip:file:myjar.jar!/", new DummyURLStreamHandler())));
		assertEquals(new URL("file:myjar.jar"),
				ResourceUtils.extractArchiveURL(new URL(null, "wsjar:file:myjar.jar!/", new DummyURLStreamHandler())));
		assertEquals(new URL("file:mywar.war"),
				ResourceUtils.extractArchiveURL(new URL(null, "jar:war:file:mywar.war*/myjar.jar!/", new DummyURLStreamHandler())));
	}


	/**
	 * Dummy URLStreamHandler that's just specified to suppress the standard
	 * {@code java.net.URL} URLStreamHandler lookup, to be able to
	 * use the standard URL class for parsing "rmi:..." URLs.
	 */
	private static class DummyURLStreamHandler extends URLStreamHandler {

		@Override
		protected URLConnection openConnection(URL url) throws IOException {
			throw new UnsupportedOperationException();
		}
	}

}
