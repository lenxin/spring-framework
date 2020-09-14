package org.springframework.core.type;

import java.util.Set;

/**
 * Interface that defines abstract access to the annotations of a specific
 * class, in a form that does not require that class to be loaded yet.
 *
 * @see StandardAnnotationMetadata
 * @see org.springframework.core.type.classreading.MetadataReader#getAnnotationMetadata()
 * @see AnnotatedTypeMetadata
 * @since 2.5
 */
public interface AnnotationMetadata extends ClassMetadata, AnnotatedTypeMetadata {
	/**
	 * Get the fully qualified class names of all annotation types that
	 * are <em>present</em> on the underlying class.
	 * 获取底层类中存在的所有注释类型的完全限定类名。
	 *
	 * @return the annotation type names
	 */
	Set<String> getAnnotationTypes();

	/**
	 * Get the fully qualified class names of all meta-annotation types that
	 * are <em>present</em> on the given annotation type on the underlying class.
	 * 获取底层类上给定注释类型上存在的所有元注释类型的完全限定类名。
	 *
	 * @param annotationName the fully qualified class name of the meta-annotation
	 *                       type to look for
	 * @return the meta-annotation type names, or an empty set if none found
	 */
	Set<String> getMetaAnnotationTypes(String annotationName);

	/**
	 * Determine whether an annotation of the given type is <em>present</em> on
	 * the underlying class.
	 * 确定底层类上是否存在给定类型的注解。
	 *
	 * @param annotationName the fully qualified class name of the annotation
	 *                       type to look for
	 * @return {@code true} if a matching annotation is present
	 */
	boolean hasAnnotation(String annotationName);

	/**
	 * Determine whether the underlying class has an annotation that is itself
	 * annotated with the meta-annotation of the given type.
	 * 确定基础类是否有自己用给定类型的元注解的注解。
	 *
	 * @param metaAnnotationName the fully qualified class name of the
	 *                           meta-annotation type to look for
	 * @return {@code true} if a matching meta-annotation is present
	 */
	boolean hasMetaAnnotation(String metaAnnotationName);

	/**
	 * Determine whether the underlying class has any methods that are
	 * annotated (or meta-annotated) with the given annotation type.
	 *
	 * @param annotationName the fully qualified class name of the annotation
	 *                       type to look for
	 */
	boolean hasAnnotatedMethods(String annotationName);

	/**
	 * Retrieve the method metadata for all methods that are annotated
	 * (or meta-annotated) with the given annotation type.
	 * <p>For any returned method, {@link MethodMetadata#isAnnotated} will
	 * return {@code true} for the given annotation type.
	 *
	 * @param annotationName the fully qualified class name of the annotation
	 *                       type to look for
	 * @return a set of {@link MethodMetadata} for methods that have a matching
	 * annotation. The return value will be an empty set if no methods match
	 * the annotation type.
	 */
	Set<MethodMetadata> getAnnotatedMethods(String annotationName);
}