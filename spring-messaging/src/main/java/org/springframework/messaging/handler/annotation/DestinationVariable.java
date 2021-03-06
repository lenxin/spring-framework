package org.springframework.messaging.handler.annotation;

import java.lang.annotation.*;

/**
 * Annotation that indicates a method parameter should be bound to a template variable
 * in a destination template string. Supported on message handling methods such as
 * {@link MessageMapping @MessageMapping}.
 * <p>
 * A {@code @DestinationVariable} template variable is always required.
 *
 * @see org.springframework.messaging.handler.annotation.MessageMapping
 * @see org.springframework.messaging.simp.annotation.support.SimpAnnotationMethodMessageHandler
 * @since 4.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DestinationVariable {

	/**
	 * The name of the destination template variable to bind to.
	 */
	String value() default "";

}
