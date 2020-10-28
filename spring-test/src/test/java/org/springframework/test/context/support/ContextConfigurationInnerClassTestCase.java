package org.springframework.test.context.support;

import org.springframework.context.annotation.Configuration;

/**
 * Not an actual <em>test case</em>.
 *

 * @since 3.1
 * @see AnnotationConfigContextLoaderTests
 */
public class ContextConfigurationInnerClassTestCase {

	@Configuration
	static class ContextConfiguration {
	}

}
