package org.springframework.beans.factory.xml;

import org.springframework.beans.factory.parsing.DefaultsDefinition;
import org.springframework.lang.Nullable;

/**
 * Simple JavaBean that holds the defaults specified at the {@code <beans>}
 * level in a standard Spring XML bean definition document:
 * {@code default-lazy-init}, {@code default-autowire}, etc.
 * 简单的JavaBean对象用于保存在beans级别定义的默认值
 *
 * @since 2.0.2
 */
public class DocumentDefaultsDefinition implements DefaultsDefinition {
	/*初始化懒加载(default-lazy-init)*/
	@Nullable
	private String lazyInit;
	/*(default-merge)*/
	@Nullable
	private String merge;
	/*自动装载类型(default-autowire)*/
	@Nullable
	private String autowire;
	/*(default-autowire-candidates),以逗号分隔*/
	@Nullable
	private String autowireCandidates;
	/*初始化方法(default-init-method)*/
	@Nullable
	private String initMethod;
	/*销毁方法(default-destroy-method)*/
	@Nullable
	private String destroyMethod;
	/*配置源对象*/
	@Nullable
	private Object source;

	public void setLazyInit(@Nullable String lazyInit) {
		this.lazyInit = lazyInit;
	}

	@Nullable
	public String getLazyInit() {
		return this.lazyInit;
	}

	public void setMerge(@Nullable String merge) {
		this.merge = merge;
	}

	@Nullable
	public String getMerge() {
		return this.merge;
	}

	public void setAutowire(@Nullable String autowire) {
		this.autowire = autowire;
	}

	@Nullable
	public String getAutowire() {
		return this.autowire;
	}

	public void setAutowireCandidates(@Nullable String autowireCandidates) {
		this.autowireCandidates = autowireCandidates;
	}

	@Nullable
	public String getAutowireCandidates() {
		return this.autowireCandidates;
	}

	public void setInitMethod(@Nullable String initMethod) {
		this.initMethod = initMethod;
	}

	@Nullable
	public String getInitMethod() {
		return this.initMethod;
	}

	public void setDestroyMethod(@Nullable String destroyMethod) {
		this.destroyMethod = destroyMethod;
	}

	@Nullable
	public String getDestroyMethod() {
		return this.destroyMethod;
	}

	/**
	 * Set the configuration source {@code Object} for this metadata element.
	 * <p>The exact type of the object will depend on the configuration mechanism used.
	 * 设置此元数据元素的配置源对象。对象的确切类型将取决于所使用的配置机制。
	 */
	public void setSource(@Nullable Object source) {
		this.source = source;
	}

	@Override
	@Nullable
	public Object getSource() {
		return this.source;
	}
}