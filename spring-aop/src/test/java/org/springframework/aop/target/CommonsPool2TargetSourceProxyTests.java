package org.springframework.aop.target;

import org.junit.Test;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.springframework.tests.sample.beans.ITestBean;

import static org.junit.Assert.*;
import static org.springframework.tests.TestResourceUtils.*;

/**

 */
public class CommonsPool2TargetSourceProxyTests {

	private static final Resource CONTEXT =
		qualifiedResource(CommonsPool2TargetSourceProxyTests.class, "context.xml");

	@Test
	public void testProxy() throws Exception {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
		reader.loadBeanDefinitions(CONTEXT);
		beanFactory.preInstantiateSingletons();
		ITestBean bean = (ITestBean)beanFactory.getBean("testBean");
		assertTrue(AopUtils.isAopProxy(bean));
	}
}
