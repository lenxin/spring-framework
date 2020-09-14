package org.springframework.core.type.filter;

import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;

/**
 * Type filter that exposes a
 * {@link org.springframework.core.type.ClassMetadata} object
 * to subclasses, for class testing purposes.
 *
 * @see #match(org.springframework.core.type.ClassMetadata)
 * @since 2.5
 */
public abstract class AbstractClassTestingTypeFilter implements TypeFilter {
	@Override
	public final boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
			throws IOException {
		return match(metadataReader.getClassMetadata());
	}

	/**
	 * Determine a match based on the given ClassMetadata object.
	 *
	 * @param metadata the ClassMetadata object
	 * @return whether this filter matches on the specified type
	 */
	protected abstract boolean match(ClassMetadata metadata);
}
