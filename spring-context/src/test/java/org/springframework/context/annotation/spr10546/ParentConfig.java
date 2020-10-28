package org.springframework.context.annotation.spr10546;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *

 */
@Configuration
public class ParentConfig {
	@Bean
	public String myBean() {
		return "myBean";
	}
}

