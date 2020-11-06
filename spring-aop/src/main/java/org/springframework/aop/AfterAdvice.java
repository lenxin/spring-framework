package org.springframework.aop;

import org.aopalliance.aop.Advice;

/**
 * Common marker interface for after advice,
 * such as {@link AfterReturningAdvice} and {@link ThrowsAdvice}.
 *
 * @see BeforeAdvice
 * @since 2.0.3
 */
public interface AfterAdvice extends Advice {
}
