package org.springframework.context.annotation;

import java.lang.annotation.*;

/**
 * Adds a textual description to bean definitions derived from
 * {@link org.springframework.stereotype.Component} or {@link Bean}.
 *
 * @see org.springframework.beans.factory.config.BeanDefinition#getDescription()
 * @since 4.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Description {
	/**
	 * The textual description to associate with the bean definition.
	 */
	String value();
}
