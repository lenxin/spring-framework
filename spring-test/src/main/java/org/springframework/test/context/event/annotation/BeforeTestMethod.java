package org.springframework.test.context.event.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.event.BeforeTestMethodEvent;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * {@link EventListener @EventListener} annotation used to consume a
 * {@link BeforeTestMethodEvent} published by the
 * {@link org.springframework.test.context.event.EventPublishingTestExecutionListener
 * EventPublishingTestExecutionListener}.
 *
 * <p>This annotation may be used on {@code @EventListener}-compliant methods within
 * a Spring test {@link org.springframework.context.ApplicationContext ApplicationContext}
 * &mdash; for example, on methods in a
 * {@link org.springframework.context.annotation.Configuration @Configuration}
 * class. A method annotated with this annotation will be invoked as part of the
 * {@link org.springframework.test.context.TestExecutionListener#beforeTestMethod}
 * lifecycle.
 *
 * <p>Event processing can optionally be made {@linkplain #value conditional} via
 * a SpEL expression &mdash; for example,
 * {@code @BeforeTestMethod("event.testContext.testMethod.name matches 'test.*'")}.
 *
 * <p>The {@code EventPublishingTestExecutionListener} must be registered in order
 * for this annotation to have an effect &mdash; for example, via
 * {@link org.springframework.test.context.TestExecutionListeners @TestExecutionListeners}.
 *


 * @since 5.2
 * @see BeforeTestMethodEvent
 */
@Retention(RUNTIME)
@Target({ METHOD, ANNOTATION_TYPE })
@Documented
@EventListener(BeforeTestMethodEvent.class)
public @interface BeforeTestMethod {

	/**
	 * Alias for {@link EventListener#condition}.
	 */
	@AliasFor(annotation = EventListener.class, attribute = "condition")
	String value() default "";

}
