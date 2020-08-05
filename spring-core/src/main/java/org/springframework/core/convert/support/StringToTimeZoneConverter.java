package org.springframework.core.convert.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.util.TimeZone;

/**
 * Convert a String to a {@link TimeZone}.
 *
 * @since 4.2
 */
class StringToTimeZoneConverter implements Converter<String, TimeZone> {
	@Override
	public TimeZone convert(String source) {
		return StringUtils.parseTimeZoneString(source);
	}
}