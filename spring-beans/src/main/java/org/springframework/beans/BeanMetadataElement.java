package org.springframework.beans;

import org.springframework.lang.Nullable;

/**
 * Interface to be implemented by bean metadata elements
 * that carry a configuration source object.
 * 接口由携带配置源对象的bean元数据元素实现。
 *
 * @since 2.0
 */
public interface BeanMetadataElement {
	/**
	 * Return the configuration source {@code Object} for this metadata element
	 * (may be {@code null}).
	 */
	@Nullable
	Object getSource();
}