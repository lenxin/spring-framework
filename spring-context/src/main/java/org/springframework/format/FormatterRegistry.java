

package org.springframework.format;

import java.lang.annotation.Annotation;

import org.springframework.core.convert.converter.ConverterRegistry;

/**
 * A registry of field formatting logic.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @since 3.0
 */
public interface FormatterRegistry extends ConverterRegistry {

	/**
	 * Adds a Formatter to format fields of a specific type.
	 * The field type is implied by the parameterized Formatter instance.
	 * @param formatter the formatter to add
	 * @since 3.1
	 * @see #addFormatterForFieldType(Class, Formatter)
	 */
	void addFormatter(Formatter<?> formatter);

	/**
	 * Adds a Formatter to format fields of the given type.
	 * <p>On print, if the Formatter's type T is declared and {@code fieldType} is not assignable to T,
	 * a coercion to T will be attempted before delegating to {@code formatter} to print a field value.
	 * On parse, if the parsed object returned by {@code formatter} is not assignable to the runtime field type,
	 * a coercion to the field type will be attempted before returning the parsed field value.
	 * @param fieldType the field type to format
	 * @param formatter the formatter to add
	 */
	void addFormatterForFieldType(Class<?> fieldType, Formatter<?> formatter);

	/**
	 * Adds a Printer/Parser pair to format fields of a specific type.
	 * The formatter will delegate to the specified {@code printer} for printing
	 * and the specified {@code parser} for parsing.
	 * <p>On print, if the Printer's type T is declared and {@code fieldType} is not assignable to T,
	 * a coercion to T will be attempted before delegating to {@code printer} to print a field value.
	 * On parse, if the object returned by the Parser is not assignable to the runtime field type,
	 * a coercion to the field type will be attempted before returning the parsed field value.
	 * @param fieldType the field type to format
	 * @param printer the printing part of the formatter
	 * @param parser the parsing part of the formatter
	 */
	void addFormatterForFieldType(Class<?> fieldType, Printer<?> printer, Parser<?> parser);

	/**
	 * Adds a Formatter to format fields annotated with a specific format annotation.
	 * @param annotationFormatterFactory the annotation formatter factory to add
	 */
	void addFormatterForFieldAnnotation(AnnotationFormatterFactory<? extends Annotation> annotationFormatterFactory);

}
