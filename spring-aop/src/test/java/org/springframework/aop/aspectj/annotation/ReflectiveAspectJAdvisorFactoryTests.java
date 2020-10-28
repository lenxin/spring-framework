package org.springframework.aop.aspectj.annotation;

/**
 * Tests for ReflectiveAtAspectJAdvisorFactory.
 * Tests are inherited: we only set the test fixture here.
 *

 * @since 2.0
 */
public class ReflectiveAspectJAdvisorFactoryTests extends AbstractAspectJAdvisorFactoryTests {

	@Override
	protected AspectJAdvisorFactory getFixture() {
		return new ReflectiveAspectJAdvisorFactory();
	}

}
