package org.springframework.beans.factory.support;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * A simple holder for {@code BeanDefinition} property defaults.
 * 保存BeanDefinition的默认值属性
 *
 * @since 2.5
 */
public class BeanDefinitionDefaults {
	@Nullable
	private Boolean lazyInit;
	private int autowireMode = AbstractBeanDefinition.AUTOWIRE_NO;
	private int dependencyCheck = AbstractBeanDefinition.DEPENDENCY_CHECK_NONE;
	@Nullable
	private String initMethodName;
	@Nullable
	private String destroyMethodName;

	/**
	 * Set whether beans should be lazily initialized by default.
	 * <p>If {@code false}, the bean will get instantiated on startup by bean
	 * factories that perform eager initialization of singletons.
	 * 设置默认情况下是否应该延迟初始化bean。
	 * 如果为false, bean将在启动时由执行单例对象的初始化的bean工厂实例化。
	 */
	public void setLazyInit(boolean lazyInit) {
		this.lazyInit = lazyInit;
	}

	public boolean isLazyInit() {
		return (this.lazyInit != null && this.lazyInit.booleanValue());
	}

	@Nullable
	public Boolean getLazyInit() {
		return this.lazyInit;
	}

	/**
	 * Set the autowire mode. This determines whether any automagical detection
	 * and setting of bean references will happen. Default is AUTOWIRE_NO
	 * which means there won't be convention-based autowiring by name or type
	 * (however, there may still be explicit annotation-driven autowiring).
	 *
	 * @param autowireMode 必须是AbstractBeanDefinition中定义的常量之一
	 */
	public void setAutowireMode(int autowireMode) {
		this.autowireMode = autowireMode;
	}

	public int getAutowireMode() {
		return this.autowireMode;
	}

	/**
	 * Set the dependency check code.
	 *
	 * @param dependencyCheck 必须是AbstractBeanDefinition中定义的常量之一
	 */
	public void setDependencyCheck(int dependencyCheck) {
		this.dependencyCheck = dependencyCheck;
	}

	public int getDependencyCheck() {
		return this.dependencyCheck;
	}

	public void setInitMethodName(@Nullable String initMethodName) {
		this.initMethodName = (StringUtils.hasText(initMethodName) ? initMethodName : null);
	}

	@Nullable
	public String getInitMethodName() {
		return this.initMethodName;
	}

	public void setDestroyMethodName(@Nullable String destroyMethodName) {
		this.destroyMethodName = (StringUtils.hasText(destroyMethodName) ? destroyMethodName : null);
	}

	@Nullable
	public String getDestroyMethodName() {
		return this.destroyMethodName;
	}
}