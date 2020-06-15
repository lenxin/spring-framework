package org.springframework.beans.factory.aspectj;

import org.junit.Test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

/**
 * @author Chris Beams
 */
public class XmlBeanConfigurerTests {

	@Test
	public void injection() {
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"org/springframework/beans/factory/aspectj/beanConfigurerTests.xml")) {

			ShouldBeConfiguredBySpring myObject = new ShouldBeConfiguredBySpring();
			assertEquals("Rod", myObject.getName());
		}
	}

}
