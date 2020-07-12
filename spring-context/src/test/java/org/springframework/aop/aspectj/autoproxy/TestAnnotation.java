package org.springframework.aop.aspectj.autoproxy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @since 2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@interface TestAnnotation {
	String value();
}
