package org.springframework.core;

import org.springframework.lang.Nullable;

/**
 * Interface defining a generic contract for attaching and accessing metadata
 * to/from arbitrary objects.
 * 元数据操作接口
 *
 * @since 2.0
 */
public interface AttributeAccessor {
	/**
	 * Set the attribute defined by {@code name} to the supplied {@code value}.
	 * If {@code value} is {@code null}, the attribute is {@link #removeAttribute removed}.
	 * <p>In general, users should take care to prevent overlaps with other
	 * metadata attributes by using fully-qualified names, perhaps using
	 * class or package names as prefix.
	 *
	 * @param name  the unique attribute key
	 * @param value the attribute value to be attached
	 *              设置元数据
	 */
	void setAttribute(String name, @Nullable Object value);

	/**
	 * Get the value of the attribute identified by {@code name}.
	 * Return {@code null} if the attribute doesn't exist.
	 *
	 * @param name the unique attribute key
	 * @return the current value of the attribute, if any
	 * 获取元数据
	 */
	@Nullable
	Object getAttribute(String name);

	/**
	 * Remove the attribute identified by {@code name} and return its value.
	 * Return {@code null} if no attribute under {@code name} is found.
	 *
	 * @param name the unique attribute key
	 * @return the last value of the attribute, if any
	 * 删除元数据
	 */
	@Nullable
	Object removeAttribute(String name);

	/**
	 * Return {@code true} if the attribute identified by {@code name} exists.
	 * Otherwise return {@code false}.
	 *
	 * @param name the unique attribute key
	 *             是否含有元数据
	 */
	boolean hasAttribute(String name);

	/**
	 * Return the names of all attributes.
	 * 获取所有元数据的名字
	 */
	String[] attributeNames();
}