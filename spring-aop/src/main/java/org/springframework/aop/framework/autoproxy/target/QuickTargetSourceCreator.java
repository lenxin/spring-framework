

package org.springframework.aop.framework.autoproxy.target;

import org.springframework.aop.target.AbstractBeanFactoryBasedTargetSource;
import org.springframework.aop.target.CommonsPool2TargetSource;
import org.springframework.aop.target.PrototypeTargetSource;
import org.springframework.aop.target.ThreadLocalTargetSource;
import org.springframework.lang.Nullable;

/**
 * Convenient TargetSourceCreator using bean name prefixes to create one of three
 * well-known TargetSource types:
 * <li>: CommonsPool2TargetSource
 * <li>% ThreadLocalTargetSource
 * <li>! PrototypeTargetSource
 *
 * @author Rod Johnson
 * @author Stephane Nicoll
 * @see org.springframework.aop.target.CommonsPool2TargetSource
 * @see org.springframework.aop.target.ThreadLocalTargetSource
 * @see org.springframework.aop.target.PrototypeTargetSource
 */
public class QuickTargetSourceCreator extends AbstractBeanFactoryBasedTargetSourceCreator {

	/**
	 * The CommonsPool2TargetSource prefix.
	 */
	public static final String PREFIX_COMMONS_POOL = ":";

	/**
	 * The ThreadLocalTargetSource prefix.
	 */
	public static final String PREFIX_THREAD_LOCAL = "%";

	/**
	 * The PrototypeTargetSource prefix.
	 */
	public static final String PREFIX_PROTOTYPE = "!";

	@Override
	@Nullable
	protected final AbstractBeanFactoryBasedTargetSource createBeanFactoryBasedTargetSource(
			Class<?> beanClass, String beanName) {

		if (beanName.startsWith(PREFIX_COMMONS_POOL)) {
			CommonsPool2TargetSource cpts = new CommonsPool2TargetSource();
			cpts.setMaxSize(25);
			return cpts;
		}
		else if (beanName.startsWith(PREFIX_THREAD_LOCAL)) {
			return new ThreadLocalTargetSource();
		}
		else if (beanName.startsWith(PREFIX_PROTOTYPE)) {
			return new PrototypeTargetSource();
		}
		else {
			// No match. Don't create a custom target source.
			return null;
		}
	}

}
