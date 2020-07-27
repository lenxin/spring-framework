package org.springframework.core.convert.converter;

import org.springframework.core.convert.TypeDescriptor;

/**
 * A {@link GenericConverter} that may conditionally execute based on attributes
 * of the {@code source} and {@code target} {@link TypeDescriptor}.
 *
 * <p>See {@link ConditionalConverter} for details.
 *
 * @see GenericConverter
 * @see ConditionalConverter
 * @since 3.0
 */
public interface ConditionalGenericConverter extends GenericConverter, ConditionalConverter {
}