package org.springframework.scheduling.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

import org.springframework.util.ReflectionUtils;

/**
 * Variant of {@link MethodInvokingRunnable} meant to be used for processing
 * of no-arg scheduled methods. Propagates user exceptions to the caller,
 * assuming that an error strategy for Runnables is in place.
 *
 * @author Juergen Hoeller
 * @since 3.0.6
 * @see org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor
 */
public class ScheduledMethodRunnable implements Runnable {

	private final Object target;

	private final Method method;


	/**
	 * Create a {@code ScheduledMethodRunnable} for the given target instance,
	 * calling the specified method.
	 * @param target the target instance to call the method on
	 * @param method the target method to call
	 */
	public ScheduledMethodRunnable(Object target, Method method) {
		this.target = target;
		this.method = method;
	}

	/**
	 * Create a {@code ScheduledMethodRunnable} for the given target instance,
	 * calling the specified method by name.
	 * @param target the target instance to call the method on
	 * @param methodName the name of the target method
	 * @throws NoSuchMethodException if the specified method does not exist
	 */
	public ScheduledMethodRunnable(Object target, String methodName) throws NoSuchMethodException {
		this.target = target;
		this.method = target.getClass().getMethod(methodName);
	}


	/**
	 * Return the target instance to call the method on.
	 */
	public Object getTarget() {
		return this.target;
	}

	/**
	 * Return the target method to call.
	 */
	public Method getMethod() {
		return this.method;
	}


	@Override
	public void run() {
		try {
			ReflectionUtils.makeAccessible(this.method);
			this.method.invoke(this.target);
		}
		catch (InvocationTargetException ex) {
			ReflectionUtils.rethrowRuntimeException(ex.getTargetException());
		}
		catch (IllegalAccessException ex) {
			throw new UndeclaredThrowableException(ex);
		}
	}

	@Override
	public String toString() {
		return this.method.getDeclaringClass().getName() + "." + this.method.getName();
	}

}
