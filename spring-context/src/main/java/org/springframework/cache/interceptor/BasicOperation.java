package org.springframework.cache.interceptor;

import java.util.Set;

/**
 * The base interface that all cache operations must implement.
 *

 * @since 4.1
 */
public interface BasicOperation {

	/**
	 * Return the cache name(s) associated with the operation.
	 */
	Set<String> getCacheNames();

}
