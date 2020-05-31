package org.springframework.beans.factory.parsing;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Representation of an import that has been processed during the parsing process.
 *
 * @see ReaderEventListener#importProcessed(ImportDefinition)
 * @since 2.0
 */
public class ImportDefinition implements BeanMetadataElement {
	private final String importedResource;

	@Nullable
	private final Resource[] actualResources;

	@Nullable
	private final Object source;

	/**
	 * Create a new ImportDefinition.
	 *
	 * @param importedResource the location of the imported resource
	 */
	public ImportDefinition(String importedResource) {
		this(importedResource, null, null);
	}

	/**
	 * Create a new ImportDefinition.
	 *
	 * @param importedResource the location of the imported resource
	 * @param source           the source object (may be {@code null})
	 */
	public ImportDefinition(String importedResource, @Nullable Object source) {
		this(importedResource, null, source);
	}

	/**
	 * Create a new ImportDefinition.
	 *
	 * @param importedResource the location of the imported resource
	 * @param source           the source object (may be {@code null})
	 */
	public ImportDefinition(String importedResource, @Nullable Resource[] actualResources, @Nullable Object source) {
		Assert.notNull(importedResource, "Imported resource must not be null");
		this.importedResource = importedResource;
		this.actualResources = actualResources;
		this.source = source;
	}

	/**
	 * Return the location of the imported resource.
	 */
	public final String getImportedResource() {
		return this.importedResource;
	}

	@Nullable
	public final Resource[] getActualResources() {
		return this.actualResources;
	}

	@Override
	@Nullable
	public final Object getSource() {
		return this.source;
	}
}