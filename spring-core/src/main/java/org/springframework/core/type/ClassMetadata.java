package org.springframework.core.type;

import org.springframework.lang.Nullable;

/**
 * Interface that defines abstract metadata of a specific class,
 * in a form that does not require that class to be loaded yet.
 *
 * @see StandardClassMetadata
 * @see org.springframework.core.type.classreading.MetadataReader#getClassMetadata()
 * @see AnnotationMetadata
 * @since 2.5
 */
public interface ClassMetadata {
	/**
	 * Return the name of the underlying class.
	 * 返回类名
	 */
	String getClassName();

	/**
	 * Return whether the underlying class represents an interface.
	 * 是否是接口
	 */
	boolean isInterface();

	/**
	 * Return whether the underlying class represents an annotation.
	 * 是否是注解
	 *
	 * @since 4.1
	 */
	boolean isAnnotation();

	/**
	 * Return whether the underlying class is marked as abstract.
	 * 是否是抽象类
	 */
	boolean isAbstract();

	/**
	 * Return whether the underlying class represents a concrete class,
	 * i.e. neither an interface nor an abstract class.
	 * 是否是具体类，不是接口或抽象类
	 */
	boolean isConcrete();

	/**
	 * Return whether the underlying class is marked as 'final'.
	 * 是否是最终类，有final修饰
	 */
	boolean isFinal();

	/**
	 * Determine whether the underlying class is independent, i.e. whether
	 * it is a top-level class or a nested class (static inner class) that
	 * can be constructed independently from an enclosing class.
	 * 是否独立，顶层类或静态内部类
	 */
	boolean isIndependent();

	/**
	 * Return whether the underlying class is declared within an enclosing
	 * class (i.e. the underlying class is an inner/nested class or a
	 * local class within a method).
	 * <p>If this method returns {@code false}, then the underlying
	 * class is a top-level class.
	 */
	boolean hasEnclosingClass();

	/**
	 * Return the name of the enclosing class of the underlying class,
	 * or {@code null} if the underlying class is a top-level class.
	 */
	@Nullable
	String getEnclosingClassName();

	/**
	 * Return whether the underlying class has a super class.
	 */
	boolean hasSuperClass();

	/**
	 * Return the name of the super class of the underlying class,
	 * or {@code null} if there is no super class defined.
	 */
	@Nullable
	String getSuperClassName();

	/**
	 * Return the names of all interfaces that the underlying class
	 * implements, or an empty array if there are none.
	 */
	String[] getInterfaceNames();

	/**
	 * Return the names of all classes declared as members of the class represented by
	 * this ClassMetadata object. This includes public, protected, default (package)
	 * access, and private classes and interfaces declared by the class, but excludes
	 * inherited classes and interfaces. An empty array is returned if no member classes
	 * or interfaces exist.
	 *
	 * @since 3.1
	 */
	String[] getMemberClassNames();
}
