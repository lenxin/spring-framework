package org.springframework.messaging.handler.annotation;

import org.springframework.messaging.Message;

import java.lang.annotation.*;

/**
 * Annotation that indicates a method's return value should be converted to
 * a {@link Message} if necessary and sent to the specified destination.
 *
 * <p>In a typical request/reply scenario, the incoming {@link Message} may
 * convey the destination to use for the reply. In that case, that destination
 * should take precedence.
 *
 * <p>This annotation may be placed class-level in which case it is inherited by
 * methods of the class.
 *
 * @since 4.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SendTo {

	/**
	 * The destination for a message created from the return value of a method.
	 */
	String[] value() default {};

}
