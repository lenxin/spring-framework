

package org.springframework.aop.aspectj;

import org.junit.Before;
import org.junit.Test;

import org.springframework.aop.aspectj.AdviceBindingTestAspect.AdviceBindingCollaborator;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.tests.sample.beans.ITestBean;
import org.springframework.tests.sample.beans.TestBean;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

/**
 * Tests for various parameter binding scenarios with before advice.
 *
 * @author Adrian Colyer
 * @author Rod Johnson
 * @author Chris Beams
 */
public class BeforeAdviceBindingTests {

	private AdviceBindingCollaborator mockCollaborator;

	private ITestBean testBeanProxy;

	private TestBean testBeanTarget;


	@Before
	public void setup() throws Exception {
		ClassPathXmlApplicationContext ctx =
				new ClassPathXmlApplicationContext(getClass().getSimpleName() + ".xml", getClass());

		testBeanProxy = (ITestBean) ctx.getBean("testBean");
		assertTrue(AopUtils.isAopProxy(testBeanProxy));

		// we need the real target too, not just the proxy...
		testBeanTarget = (TestBean) ((Advised) testBeanProxy).getTargetSource().getTarget();

		AdviceBindingTestAspect beforeAdviceAspect = (AdviceBindingTestAspect) ctx.getBean("testAspect");

		mockCollaborator = mock(AdviceBindingCollaborator.class);
		beforeAdviceAspect.setCollaborator(mockCollaborator);
	}


	@Test
	public void testOneIntArg() {
		testBeanProxy.setAge(5);
		verify(mockCollaborator).oneIntArg(5);
	}

	@Test
	public void testOneObjectArgBoundToProxyUsingThis() {
		testBeanProxy.getAge();
		verify(mockCollaborator).oneObjectArg(this.testBeanProxy);
	}

	@Test
	public void testOneIntAndOneObjectArgs() {
		testBeanProxy.setAge(5);
		verify(mockCollaborator).oneIntAndOneObject(5,this.testBeanTarget);
	}

	@Test
	public void testNeedsJoinPoint() {
		testBeanProxy.getAge();
		verify(mockCollaborator).needsJoinPoint("getAge");
	}

	@Test
	public void testNeedsJoinPointStaticPart() {
		testBeanProxy.getAge();
		verify(mockCollaborator).needsJoinPointStaticPart("getAge");
	}


}


class AuthenticationLogger {

	public void logAuthenticationAttempt(String username) {
		System.out.println("User [" + username + "] attempting to authenticate");
	}

}

class SecurityManager {
	public boolean authenticate(String username, String password) {
		return false;
	}
}
