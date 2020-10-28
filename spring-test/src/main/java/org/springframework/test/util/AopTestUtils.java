package org.springframework.test.util;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.util.Assert;

/**
 * {@code AopTestUtils} is a collection of AOP-related utility methods for
 * use in unit and integration testing scenarios.
 *
 * <p>For Spring's core AOP utilities, see
 * {@link org.springframework.aop.support.AopUtils AopUtils} and
 * {@link org.springframework.aop.framework.AopProxyUtils AopProxyUtils}.
 *


 * @since 4.2
 * @see org.springframework.aop.support.AopUtils
 * @see org.springframework.aop.framework.AopProxyUtils
 * @see ReflectionTestUtils
 */
public abstract class AopTestUtils {

	/**
	 * Get the <em>target</em> object of the supplied {@code candidate} object.
	 * <p>If the supplied {@code candidate} is a Spring
	 * {@linkplain AopUtils#isAopProxy proxy}, the target of the proxy will
	 * be returned; otherwise, the {@code candidate} will be returned
	 * <em>as is</em>.
	 * @param candidate the instance to check (potentially a Spring AOP proxy;
	 * never {@code null})
	 * @return the target object or the {@code candidate} (never {@code null})
	 * @throws IllegalStateException if an error occurs while unwrapping a proxy
	 * @see Advised#getTargetSource()
	 * @see #getUltimateTargetObject
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getTargetObject(Object candidate) {
		Assert.notNull(candidate, "Candidate must not be null");
		try {
			if (AopUtils.isAopProxy(candidate) && candidate instanceof Advised) {
				Object target = ((Advised) candidate).getTargetSource().getTarget();
				if (target != null) {
					return (T) target;
				}
			}
		}
		catch (Throwable ex) {
			throw new IllegalStateException("Failed to unwrap proxied object", ex);
		}
		return (T) candidate;
	}

	/**
	 * Get the ultimate <em>target</em> object of the supplied {@code candidate}
	 * object, unwrapping not only a top-level proxy but also any number of
	 * nested proxies.
	 * <p>If the supplied {@code candidate} is a Spring
	 * {@linkplain AopUtils#isAopProxy proxy}, the ultimate target of all
	 * nested proxies will be returned; otherwise, the {@code candidate}
	 * will be returned <em>as is</em>.
	 * @param candidate the instance to check (potentially a Spring AOP proxy;
	 * never {@code null})
	 * @return the target object or the {@code candidate} (never {@code null})
	 * @throws IllegalStateException if an error occurs while unwrapping a proxy
	 * @see Advised#getTargetSource()
	 * @see org.springframework.aop.framework.AopProxyUtils#ultimateTargetClass
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getUltimateTargetObject(Object candidate) {
		Assert.notNull(candidate, "Candidate must not be null");
		try {
			if (AopUtils.isAopProxy(candidate) && candidate instanceof Advised) {
				Object target = ((Advised) candidate).getTargetSource().getTarget();
				if (target != null) {
					return (T) getUltimateTargetObject(target);
				}
			}
		}
		catch (Throwable ex) {
			throw new IllegalStateException("Failed to unwrap proxied object", ex);
		}
		return (T) candidate;
	}

}
