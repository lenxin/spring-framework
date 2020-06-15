package org.springframework.test.context.junit4.aci.annotation;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertySource;
import org.springframework.mock.env.MockPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Integration tests that verify that a {@link PropertySource} can be set via a
 * custom {@link ApplicationContextInitializer} in the Spring TestContext Framework.
 *
 * @author Sam Brannen
 * @since 4.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = PropertySourcesInitializerTests.PropertySourceInitializer.class)
public class PropertySourcesInitializerTests {

	@Configuration
	static class Config {

		@Value("${enigma}")
		// The following can also be used to directly access the
		// environment instead of relying on placeholder replacement.
		// @Value("#{ environment['enigma'] }")
		private String enigma;


		@Bean
		public String enigma() {
			return enigma;
		}

	}


	@Autowired
	private String enigma;


	@Test
	public void customPropertySourceConfiguredViaContextInitializer() {
		assertEquals("foo", enigma);
	}


	public static class PropertySourceInitializer implements
			ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			applicationContext.getEnvironment().getPropertySources().addFirst(
				new MockPropertySource().withProperty("enigma", "foo"));
		}
	}

}
