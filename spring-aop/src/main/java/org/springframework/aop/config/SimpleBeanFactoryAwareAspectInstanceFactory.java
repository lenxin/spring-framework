package org.springframework.aop.config;

import org.springframework.aop.aspectj.AspectInstanceFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * Implementation of {@link AspectInstanceFactory} that locates the aspect from the
 * {@link org.springframework.beans.factory.BeanFactory} using a configured bean name.
 *


 * @since 2.0
 */
public class SimpleBeanFactoryAwareAspectInstanceFactory implements AspectInstanceFactory, BeanFactoryAware {

	@Nullable
	private String aspectBeanName;

	@Nullable
	private BeanFactory beanFactory;


	/**
	 * Set the name of the aspect bean. This is the bean that is returned when calling
	 * {@link #getAspectInstance()}.
	 */
	public void setAspectBeanName(String aspectBeanName) {
		this.aspectBeanName = aspectBeanName;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
		Assert.notNull(this.aspectBeanName, "'aspectBeanName' is required");
	}


	/**
	 * Look up the aspect bean from the {@link BeanFactory} and returns it.
	 * @see #setAspectBeanName
	 */
	@Override
	public Object getAspectInstance() {
		Assert.state(this.beanFactory != null, "No BeanFactory set");
		Assert.state(this.aspectBeanName != null, "No 'aspectBeanName' set");
		return this.beanFactory.getBean(this.aspectBeanName);
	}

	@Override
	@Nullable
	public ClassLoader getAspectClassLoader() {
		if (this.beanFactory instanceof ConfigurableBeanFactory) {
			return ((ConfigurableBeanFactory) this.beanFactory).getBeanClassLoader();
		}
		else {
			return ClassUtils.getDefaultClassLoader();
		}
	}

	@Override
	public int getOrder() {
		if (this.beanFactory != null && this.aspectBeanName != null &&
				this.beanFactory.isSingleton(this.aspectBeanName) &&
				this.beanFactory.isTypeMatch(this.aspectBeanName, Ordered.class)) {
			return ((Ordered) this.beanFactory.getBean(this.aspectBeanName)).getOrder();
		}
		return Ordered.LOWEST_PRECEDENCE;
	}

}
