package org.springframework.context.index.processor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Used by {@link CandidateComponentsIndexer} to collect {@link CandidateComponentsMetadata}.
 *
 * @since 5.0
 */
class MetadataCollector {
	private final List<ItemMetadata> metadataItems = new ArrayList<>();
	private final ProcessingEnvironment processingEnvironment;
	private final CandidateComponentsMetadata previousMetadata;
	private final TypeHelper typeHelper;
	private final Set<String> processedSourceTypes = new HashSet<>();

	/**
	 * Create a new {@code MetadataProcessor} instance.
	 *
	 * @param processingEnvironment the processing environment of the build
	 * @param previousMetadata      any previous metadata or {@code null}
	 */
	public MetadataCollector(ProcessingEnvironment processingEnvironment,
							 CandidateComponentsMetadata previousMetadata) {
		this.processingEnvironment = processingEnvironment;
		this.previousMetadata = previousMetadata;
		this.typeHelper = new TypeHelper(processingEnvironment);
	}

	public void processing(RoundEnvironment roundEnv) {
		for (Element element : roundEnv.getRootElements()) {
			markAsProcessed(element);
		}
	}

	private void markAsProcessed(Element element) {
		if (element instanceof TypeElement) {
			this.processedSourceTypes.add(this.typeHelper.getType(element));
		}
	}

	public void add(ItemMetadata metadata) {
		this.metadataItems.add(metadata);
	}

	public CandidateComponentsMetadata getMetadata() {
		CandidateComponentsMetadata metadata = new CandidateComponentsMetadata();
		for (ItemMetadata item : this.metadataItems) {
			metadata.add(item);
		}
		if (this.previousMetadata != null) {
			List<ItemMetadata> items = this.previousMetadata.getItems();
			for (ItemMetadata item : items) {
				if (shouldBeMerged(item)) {
					metadata.add(item);
				}
			}
		}
		return metadata;
	}

	private boolean shouldBeMerged(ItemMetadata itemMetadata) {
		String sourceType = itemMetadata.getType();
		return (sourceType != null && !deletedInCurrentBuild(sourceType)
				&& !processedInCurrentBuild(sourceType));
	}

	private boolean deletedInCurrentBuild(String sourceType) {
		return this.processingEnvironment.getElementUtils()
				.getTypeElement(sourceType) == null;
	}

	private boolean processedInCurrentBuild(String sourceType) {
		return this.processedSourceTypes.contains(sourceType);
	}
}
