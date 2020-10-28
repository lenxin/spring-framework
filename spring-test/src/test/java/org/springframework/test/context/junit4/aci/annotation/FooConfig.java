package org.springframework.test.context.junit4.aci.annotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**

 * @since 4.3
 */
@Configuration
class FooConfig {

	@Bean
	String foo() {
		return "foo";
	}

}
