package org.springframework.aop.aspectj.autoproxy;

import org.junit.Test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**

 */
public class AutoProxyWithCodeStyleAspectsTests {

	@Test
	@SuppressWarnings("resource")
	public void noAutoproxyingOfAjcCompiledAspects() {
		new ClassPathXmlApplicationContext("org/springframework/aop/aspectj/autoproxy/ajcAutoproxyTests.xml");
	}

}
