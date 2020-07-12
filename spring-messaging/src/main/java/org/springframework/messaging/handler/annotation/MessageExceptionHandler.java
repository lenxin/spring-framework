package org.springframework.messaging.handler.annotation;

import java.lang.annotation.*;

/**
 * Annotation for handling exceptions thrown from message-handling methods within a
 * specific handler class.
 *
 * @since 4.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageExceptionHandler {

	/**
	 * Exceptions handled by the annotated method. If empty, will default to any
	 * exceptions listed in the method argument list.
	 */
	Class<? extends Throwable>[] value() default {};

}
