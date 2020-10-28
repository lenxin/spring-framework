package org.springframework.test.context.junit4.aci.annotation;

import org.junit.Test;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.aci.DevProfileInitializer;

import static org.junit.Assert.*;

/**
 * Integration tests that verify support for {@link ApplicationContextInitializer
 * ApplicationContextInitializers} in conjunction with annotation-driven
 * configuration in the TestContext framework.
 *

 * @since 3.2
 */
@ContextConfiguration(initializers = DevProfileInitializer.class, inheritInitializers = false)
public class OverriddenInitializersAnnotationConfigTests extends SingleInitializerAnnotationConfigTests {

	@Test
	@Override
	public void activeBeans() {
		assertEquals("foo", foo);
		assertNull(bar);
		assertEquals("dev profile config", baz);
	}
}
