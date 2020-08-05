package org.springframework.core.convert.support;

import org.springframework.core.convert.converter.Converter;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Simple converter from Java 8's {@link java.time.ZonedDateTime} to {@link java.util.Calendar}.
 *
 * <p>Note that Spring's default ConversionService setup understands the 'from'/'to' convention
 * that the JSR-310 {@code java.time} package consistently uses. That convention is implemented
 * reflectively in {@link ObjectToObjectConverter}, not in specific JSR-310 converters.
 * It covers {@link java.util.GregorianCalendar#toZonedDateTime()} as well, and also
 * {@link java.util.Date#from(java.time.Instant)} and {@link java.util.Date#toInstant()}.
 *
 * @see java.util.GregorianCalendar#from(java.time.ZonedDateTime)
 * @since 4.0.1
 */
final class ZonedDateTimeToCalendarConverter implements Converter<ZonedDateTime, Calendar> {
	@Override
	public Calendar convert(ZonedDateTime source) {
		return GregorianCalendar.from(source);
	}
}