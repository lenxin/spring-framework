package org.springframework.aop.target;

import java.util.Set;

import org.junit.Test;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.springframework.tests.sample.beans.ITestBean;

import static org.junit.Assert.*;
import static org.springframework.tests.TestResourceUtils.*;

/**
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Chris Beams
 * @since 07.01.2005
 */
public class LazyInitTargetSourceTests {

	private static final Class<?> CLASS = LazyInitTargetSourceTests.class;

	private static final Resource SINGLETON_CONTEXT = qualifiedResource(CLASS, "singleton.xml");
	private static final Resource CUSTOM_TARGET_CONTEXT = qualifiedResource(CLASS, "customTarget.xml");
	private static final Resource FACTORY_BEAN_CONTEXT = qualifiedResource(CLASS, "factoryBean.xml");

	@Test
	public void testLazyInitSingletonTargetSource() {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(bf).loadBeanDefinitions(SINGLETON_CONTEXT);
		bf.preInstantiateSingletons();

		ITestBean tb = (ITestBean) bf.getBean("proxy");
		assertFalse(bf.containsSingleton("target"));
		assertEquals(10, tb.getAge());
		assertTrue(bf.containsSingleton("target"));
	}

	@Test
	public void testCustomLazyInitSingletonTargetSource() {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(bf).loadBeanDefinitions(CUSTOM_TARGET_CONTEXT);
		bf.preInstantiateSingletons();

		ITestBean tb = (ITestBean) bf.getBean("proxy");
		assertFalse(bf.containsSingleton("target"));
		assertEquals("Rob Harrop", tb.getName());
		assertTrue(bf.containsSingleton("target"));
	}

	@Test
	public void testLazyInitFactoryBeanTargetSource() {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(bf).loadBeanDefinitions(FACTORY_BEAN_CONTEXT);
		bf.preInstantiateSingletons();

		Set<?> set1 = (Set<?>) bf.getBean("proxy1");
		assertFalse(bf.containsSingleton("target1"));
		assertTrue(set1.contains("10"));
		assertTrue(bf.containsSingleton("target1"));

		Set<?> set2 = (Set<?>) bf.getBean("proxy2");
		assertFalse(bf.containsSingleton("target2"));
		assertTrue(set2.contains("20"));
		assertTrue(bf.containsSingleton("target2"));
	}


	@SuppressWarnings("serial")
	public static class CustomLazyInitTargetSource extends LazyInitTargetSource {

		@Override
		protected void postProcessTargetObject(Object targetObject) {
			((ITestBean) targetObject).setName("Rob Harrop");
		}
	}

}
