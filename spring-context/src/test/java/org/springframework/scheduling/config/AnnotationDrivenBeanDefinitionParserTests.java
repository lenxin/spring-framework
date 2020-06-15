

package org.springframework.scheduling.config;

import java.util.function.Supplier;

import org.junit.After;
import org.junit.Test;

import org.springframework.beans.DirectFieldAccessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

/**
 * @author Mark Fisher
 * @author Stephane Nicoll
 */
public class AnnotationDrivenBeanDefinitionParserTests {

	private ConfigurableApplicationContext context = new ClassPathXmlApplicationContext(
			"annotationDrivenContext.xml", AnnotationDrivenBeanDefinitionParserTests.class);


	@After
	public void closeApplicationContext() {
		context.close();
	}


	@Test
	public void asyncPostProcessorRegistered() {
		assertTrue(context.containsBean(TaskManagementConfigUtils.ASYNC_ANNOTATION_PROCESSOR_BEAN_NAME));
	}

	@Test
	public void scheduledPostProcessorRegistered() {
		assertTrue(context.containsBean(TaskManagementConfigUtils.SCHEDULED_ANNOTATION_PROCESSOR_BEAN_NAME));
	}

	@Test
	public void asyncPostProcessorExecutorReference() {
		Object executor = context.getBean("testExecutor");
		Object postProcessor = context.getBean(TaskManagementConfigUtils.ASYNC_ANNOTATION_PROCESSOR_BEAN_NAME);
		assertSame(executor, ((Supplier<?>) new DirectFieldAccessor(postProcessor).getPropertyValue("executor")).get());
	}

	@Test
	public void scheduledPostProcessorSchedulerReference() {
		Object scheduler = context.getBean("testScheduler");
		Object postProcessor = context.getBean(TaskManagementConfigUtils.SCHEDULED_ANNOTATION_PROCESSOR_BEAN_NAME);
		assertSame(scheduler, new DirectFieldAccessor(postProcessor).getPropertyValue("scheduler"));
	}

	@Test
	public void asyncPostProcessorExceptionHandlerReference() {
		Object exceptionHandler = context.getBean("testExceptionHandler");
		Object postProcessor = context.getBean(TaskManagementConfigUtils.ASYNC_ANNOTATION_PROCESSOR_BEAN_NAME);
		assertSame(exceptionHandler, ((Supplier<?>) new DirectFieldAccessor(postProcessor).getPropertyValue("exceptionHandler")).get());
	}

}
