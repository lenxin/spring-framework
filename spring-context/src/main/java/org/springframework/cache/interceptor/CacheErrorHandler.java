package org.springframework.cache.interceptor;

import org.springframework.cache.Cache;
import org.springframework.lang.Nullable;

/**
 * A strategy for handling cache-related errors. In most cases, any
 * exception thrown by the provider should simply be thrown back at
 * the client but, in some circumstances, the infrastructure may need
 * to handle cache-provider exceptions in a different way.
 *
 * <p>Typically, failing to retrieve an object from the cache with
 * a given id can be transparently managed as a cache miss by not
 * throwing back such exception.
 *
 * @author Stephane Nicoll
 * @since 4.1
 */
public interface CacheErrorHandler {

	/**
	 * Handle the given runtime exception thrown by the cache provider when
	 * retrieving an item with the specified {@code key}, possibly
	 * rethrowing it as a fatal exception.
	 * @param exception the exception thrown by the cache provider
	 * @param cache the cache
	 * @param key the key used to get the item
	 * @see Cache#get(Object)
	 */
	void handleCacheGetError(RuntimeException exception, Cache cache, Object key);

	/**
	 * Handle the given runtime exception thrown by the cache provider when
	 * updating an item with the specified {@code key} and {@code value},
	 * possibly rethrowing it as a fatal exception.
	 * @param exception the exception thrown by the cache provider
	 * @param cache the cache
	 * @param key the key used to update the item
	 * @param value the value to associate with the key
	 * @see Cache#put(Object, Object)
	 */
	void handleCachePutError(RuntimeException exception, Cache cache, Object key, @Nullable Object value);

	/**
	 * Handle the given runtime exception thrown by the cache provider when
	 * clearing an item with the specified {@code key}, possibly rethrowing
	 * it as a fatal exception.
	 * @param exception the exception thrown by the cache provider
	 * @param cache the cache
	 * @param key the key used to clear the item
	 */
	void handleCacheEvictError(RuntimeException exception, Cache cache, Object key);

	/**
	 * Handle the given runtime exception thrown by the cache provider when
	 * clearing the specified {@link Cache}, possibly rethrowing it as a
	 * fatal exception.
	 * @param exception the exception thrown by the cache provider
	 * @param cache the cache to clear
	 */
	void handleCacheClearError(RuntimeException exception, Cache cache);

}
