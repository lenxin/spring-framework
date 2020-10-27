package org.springframework.core.type.classreading;

import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * Factory interface for {@link MetadataReader} instances.
 * Allows for caching a MetadataReader per original resource.
 *
 * @see SimpleMetadataReaderFactory
 * @see CachingMetadataReaderFactory
 * @since 2.5
 */
public interface MetadataReaderFactory {
	/**
	 * Obtain a MetadataReader for the given class name.
	 * 获取给定类名的元数据读取器
	 *
	 * @param className the class name (to be resolved to a ".class" file)
	 * @return a holder for the ClassReader instance (never {@code null})
	 * @throws IOException in case of I/O failure
	 */
	MetadataReader getMetadataReader(String className) throws IOException;

	/**
	 * Obtain a MetadataReader for the given resource.
	 * 获取给定资源的元数据读取器
	 *
	 * @param resource the resource (pointing to a ".class" file)
	 * @return a holder for the ClassReader instance (never {@code null})
	 * @throws IOException in case of I/O failure
	 */
	MetadataReader getMetadataReader(Resource resource) throws IOException;
}