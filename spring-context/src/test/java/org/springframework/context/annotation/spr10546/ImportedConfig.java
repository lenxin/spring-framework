package org.springframework.context.annotation.spr10546;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *

 */
@Configuration
public class ImportedConfig {
	@Bean
	public String myBean() {
		return "myBean";
	}
}
