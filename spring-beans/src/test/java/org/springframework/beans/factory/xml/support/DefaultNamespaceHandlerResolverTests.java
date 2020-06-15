package org.springframework.beans.factory.xml.support;

import org.junit.Test;

import org.springframework.beans.factory.xml.DefaultNamespaceHandlerResolver;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.UtilNamespaceHandler;

import static org.junit.Assert.*;

/**
 * Unit and integration tests for the {@link DefaultNamespaceHandlerResolver} class.
 *
 * @author Rob Harrop
 * @author Rick Evans
 */
public class DefaultNamespaceHandlerResolverTests {

	@Test
	public void testResolvedMappedHandler() {
		DefaultNamespaceHandlerResolver resolver = new DefaultNamespaceHandlerResolver(getClass().getClassLoader());
		NamespaceHandler handler = resolver.resolve("http://www.springframework.org/schema/util");
		assertNotNull("Handler should not be null.", handler);
		assertEquals("Incorrect handler loaded", UtilNamespaceHandler.class, handler.getClass());
	}

	@Test
	public void testResolvedMappedHandlerWithNoArgCtor() {
		DefaultNamespaceHandlerResolver resolver = new DefaultNamespaceHandlerResolver();
		NamespaceHandler handler = resolver.resolve("http://www.springframework.org/schema/util");
		assertNotNull("Handler should not be null.", handler);
		assertEquals("Incorrect handler loaded", UtilNamespaceHandler.class, handler.getClass());
	}

	@Test
	public void testNonExistentHandlerClass() throws Exception {
		String mappingPath = "org/springframework/beans/factory/xml/support/nonExistent.properties";
		try {
			new DefaultNamespaceHandlerResolver(getClass().getClassLoader(), mappingPath);
			// pass
		}
		catch (Throwable ex) {
			fail("Non-existent handler classes must be ignored: " + ex);
		}
	}

	@Test
	public void testResolveInvalidHandler() throws Exception {
		String mappingPath = "org/springframework/beans/factory/xml/support/invalid.properties";
		try {
			new DefaultNamespaceHandlerResolver(getClass().getClassLoader(), mappingPath);
			fail("Should not be able to map a class that doesn't implement NamespaceHandler");
		}
		catch (Throwable expected) {
		}
	}

	@Test
	public void testCtorWithNullClassLoaderArgument() throws Exception {
		// simply must not bail...
		new DefaultNamespaceHandlerResolver(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCtorWithNullClassLoaderArgumentAndNullMappingLocationArgument() throws Exception {
		new DefaultNamespaceHandlerResolver(null, null);
	}

	@Test
	public void testCtorWithNonExistentMappingLocationArgument() throws Exception {
		// simply must not bail; we don't want non-existent resources to result in an Exception
		new DefaultNamespaceHandlerResolver(null, "738trbc bobabloobop871");
	}

}
