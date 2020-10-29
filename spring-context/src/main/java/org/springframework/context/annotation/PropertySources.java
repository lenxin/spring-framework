package org.springframework.context.annotation;

import java.lang.annotation.*;

/**
 * Container annotation that aggregates several {@link PropertySource} annotations.
 *
 * <p>Can be used natively, declaring several nested {@link PropertySource} annotations.
 * Can also be used in conjunction with Java 8's support for <em>repeatable annotations</em>,
 * where {@link PropertySource} can simply be declared several times on the same
 * {@linkplain ElementType#TYPE type}, implicitly generating this container annotation.
 *
 * @see PropertySource
 * @since 4.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PropertySources {
	PropertySource[] value();
}
