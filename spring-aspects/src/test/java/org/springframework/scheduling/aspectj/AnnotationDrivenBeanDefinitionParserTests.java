package org.springframework.scheduling.aspectj;

import java.util.function.Supplier;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.DirectFieldAccessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.config.TaskManagementConfigUtils;

import static org.junit.Assert.*;

/**

 */
public class AnnotationDrivenBeanDefinitionParserTests {

	private ConfigurableApplicationContext context;

	@Before
	public void setup() {
		this.context = new ClassPathXmlApplicationContext(
				"annotationDrivenContext.xml", AnnotationDrivenBeanDefinitionParserTests.class);
	}

	@After
	public void after() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void asyncAspectRegistered() {
		assertTrue(context.containsBean(TaskManagementConfigUtils.ASYNC_EXECUTION_ASPECT_BEAN_NAME));
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void asyncPostProcessorExecutorReference() {
		Object executor = context.getBean("testExecutor");
		Object aspect = context.getBean(TaskManagementConfigUtils.ASYNC_EXECUTION_ASPECT_BEAN_NAME);
		assertSame(executor, ((Supplier) new DirectFieldAccessor(aspect).getPropertyValue("defaultExecutor")).get());
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void asyncPostProcessorExceptionHandlerReference() {
		Object exceptionHandler = context.getBean("testExceptionHandler");
		Object aspect = context.getBean(TaskManagementConfigUtils.ASYNC_EXECUTION_ASPECT_BEAN_NAME);
		assertSame(exceptionHandler, ((Supplier) new DirectFieldAccessor(aspect).getPropertyValue("exceptionHandler")).get());
	}

}
