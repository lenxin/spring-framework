package org.springframework.context.annotation.aspectj;

import org.junit.Test;

import org.springframework.beans.factory.aspectj.ShouldBeConfiguredBySpring;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import static org.junit.Assert.*;

/**
 * Tests that @EnableSpringConfigured properly registers an
 * {@link org.springframework.beans.factory.aspectj.AnnotationBeanConfigurerAspect},
 * just as does {@code <context:spring-configured>}.
 *

 * @since 3.1
 */
public class AnnotationBeanConfigurerTests {

	@Test
	public void injection() {
		try (AnnotationConfigApplicationContext context = new  AnnotationConfigApplicationContext(Config.class)) {
			ShouldBeConfiguredBySpring myObject = new ShouldBeConfiguredBySpring();
			assertEquals("Rod", myObject.getName());
		}
	}


	@Configuration
	@ImportResource("org/springframework/beans/factory/aspectj/beanConfigurerTests-beans.xml")
	@EnableSpringConfigured
	static class Config {
	}

}
