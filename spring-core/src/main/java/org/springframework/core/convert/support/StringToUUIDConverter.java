package org.springframework.core.convert.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * Converts from a String to a {@link java.util.UUID}.
 *
 * @see UUID#fromString
 * @since 3.2
 */
final class StringToUUIDConverter implements Converter<String, UUID> {
	@Override
	public UUID convert(String source) {
		return (StringUtils.hasLength(source) ? UUID.fromString(source.trim()) : null);
	}
}