package org.springframework.core.type;

/**
 * Interface that defines abstract access to the annotations of a specific
 * class, in a form that does not require that class to be loaded yet.
 *
 * @see StandardMethodMetadata
 * @see AnnotationMetadata#getAnnotatedMethods
 * @see AnnotatedTypeMetadata
 * @since 3.0
 */
public interface MethodMetadata extends AnnotatedTypeMetadata {
	/**
	 * Return the name of the method.
	 */
	String getMethodName();

	/**
	 * Return the fully-qualified name of the class that declares this method.
	 */
	String getDeclaringClassName();

	/**
	 * Return the fully-qualified name of this method's declared return type.
	 *
	 * @since 4.2
	 */
	String getReturnTypeName();

	/**
	 * Return whether the underlying method is effectively abstract:
	 * i.e. marked as abstract on a class or declared as a regular,
	 * non-default method in an interface.
	 *
	 * @since 4.2
	 */
	boolean isAbstract();

	/**
	 * Return whether the underlying method is declared as 'static'.
	 */
	boolean isStatic();

	/**
	 * Return whether the underlying method is marked as 'final'.
	 */
	boolean isFinal();

	/**
	 * Return whether the underlying method is overridable,
	 * i.e. not marked as static, final or private.
	 */
	boolean isOverridable();
}