package org.springframework.core.convert.support;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Converts a Collection to an array.
 *
 * <p>First, creates a new array of the requested targetType with a length equal to the
 * size of the source Collection. Then sets each collection element into the array.
 * Will perform an element conversion from the collection's parameterized type to the
 * array's component type if necessary.
 *


 * @since 3.0
 */
final class CollectionToArrayConverter implements ConditionalGenericConverter {

	private final ConversionService conversionService;


	public CollectionToArrayConverter(ConversionService conversionService) {
		this.conversionService = conversionService;
	}


	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(Collection.class, Object[].class));
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return ConversionUtils.canConvertElements(sourceType.getElementTypeDescriptor(),
				targetType.getElementTypeDescriptor(), this.conversionService);
	}

	@Override
	@Nullable
	public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (source == null) {
			return null;
		}
		Collection<?> sourceCollection = (Collection<?>) source;
		TypeDescriptor targetElementType = targetType.getElementTypeDescriptor();
		Assert.state(targetElementType != null, "No target element type");
		Object array = Array.newInstance(targetElementType.getType(), sourceCollection.size());
		int i = 0;
		for (Object sourceElement : sourceCollection) {
			Object targetElement = this.conversionService.convert(sourceElement,
					sourceType.elementTypeDescriptor(sourceElement), targetElementType);
			Array.set(array, i++, targetElement);
		}
		return array;
	}

}
