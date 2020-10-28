package org.springframework.test.context.support;

import org.springframework.context.annotation.Configuration;

/**
 * Not an actual <em>test case</em>.
 *

 * @since 3.1
 * @see AnnotationConfigContextLoaderTests
 */
public class PrivateConfigInnerClassTestCase {

	// Intentionally private
	@Configuration
	private static class PrivateConfig {
	}

}
