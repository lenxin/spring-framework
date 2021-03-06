package org.springframework.aop.aspectj.generic;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Tests that poitncut matching is correct with generic method parameter.
 * See SPR-3904 for more details.
 */
public class GenericParameterMatchingTests {

	private CounterAspect counterAspect;

	private GenericInterface<String> testBean;


	@SuppressWarnings("unchecked")
	@org.junit.Before
	public void setup() {
		ClassPathXmlApplicationContext ctx =
				new ClassPathXmlApplicationContext(getClass().getSimpleName() + "-context.xml", getClass());

		counterAspect = (CounterAspect) ctx.getBean("counterAspect");
		counterAspect.reset();

		testBean = (GenericInterface<String>) ctx.getBean("testBean");
	}


	@Test
	public void testGenericInterfaceGenericArgExecution() {
		testBean.save("");
		assertEquals(1, counterAspect.genericInterfaceGenericArgExecutionCount);
	}

	@Test
	public void testGenericInterfaceGenericCollectionArgExecution() {
		testBean.saveAll(null);
		assertEquals(1, counterAspect.genericInterfaceGenericCollectionArgExecutionCount);
	}

	@Test
	public void testGenericInterfaceSubtypeGenericCollectionArgExecution() {
		testBean.saveAll(null);
		assertEquals(1, counterAspect.genericInterfaceSubtypeGenericCollectionArgExecutionCount);
	}


	static interface GenericInterface<T> {

		public void save(T bean);

		public void saveAll(Collection<T> beans);
	}


	static class GenericImpl<T> implements GenericInterface<T> {

		@Override
		public void save(T bean) {
		}

		@Override
		public void saveAll(Collection<T> beans) {
		}
	}


	@Aspect
	static class CounterAspect {

		int genericInterfaceGenericArgExecutionCount;
		int genericInterfaceGenericCollectionArgExecutionCount;
		int genericInterfaceSubtypeGenericCollectionArgExecutionCount;

		public void reset() {
			genericInterfaceGenericArgExecutionCount = 0;
			genericInterfaceGenericCollectionArgExecutionCount = 0;
			genericInterfaceSubtypeGenericCollectionArgExecutionCount = 0;
		}

		@Pointcut("execution(* org.springframework.aop.aspectj.generic.GenericParameterMatchingTests.GenericInterface.save(..))")
		public void genericInterfaceGenericArgExecution() {
		}

		@Pointcut("execution(* org.springframework.aop.aspectj.generic.GenericParameterMatchingTests.GenericInterface.saveAll(..))")
		public void GenericInterfaceGenericCollectionArgExecution() {
		}

		@Pointcut("execution(* org.springframework.aop.aspectj.generic.GenericParameterMatchingTests.GenericInterface+.saveAll(..))")
		public void genericInterfaceSubtypeGenericCollectionArgExecution() {
		}

		@Before("genericInterfaceGenericArgExecution()")
		public void incrementGenericInterfaceGenericArgExecution() {
			genericInterfaceGenericArgExecutionCount++;
		}

		@Before("GenericInterfaceGenericCollectionArgExecution()")
		public void incrementGenericInterfaceGenericCollectionArgExecution() {
			genericInterfaceGenericCollectionArgExecutionCount++;
		}

		@Before("genericInterfaceSubtypeGenericCollectionArgExecution()")
		public void incrementGenericInterfaceSubtypeGenericCollectionArgExecution() {
			genericInterfaceSubtypeGenericCollectionArgExecutionCount++;
		}
	}

}
