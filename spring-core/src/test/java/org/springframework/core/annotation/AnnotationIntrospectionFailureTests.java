package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

import org.junit.Test;

import org.springframework.core.OverridingClassLoader;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests that trigger annotation introspection failures and ensure that they are
 * dealt with correctly.
 *

 * @since 5.2
 * @see AnnotationUtils
 * @see AnnotatedElementUtils
 */
public class AnnotationIntrospectionFailureTests {

	@Test
	public void filteredTypeThrowsTypeNotPresentException() throws Exception {
		FilteringClassLoader classLoader = new FilteringClassLoader(
				getClass().getClassLoader());
		Class<?> withExampleAnnotation = ClassUtils.forName(
				WithExampleAnnotation.class.getName(), classLoader);
		Annotation annotation = withExampleAnnotation.getAnnotations()[0];
		Method method = annotation.annotationType().getMethod("value");
		method.setAccessible(true);
		assertThatExceptionOfType(TypeNotPresentException.class).isThrownBy(() -> {
			ReflectionUtils.invokeMethod(method, annotation);
		}).withCauseInstanceOf(ClassNotFoundException.class);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void filteredTypeInMetaAnnotationWhenUsingAnnotatedElementUtilsHandlesException() throws Exception {
		FilteringClassLoader classLoader = new FilteringClassLoader(
				getClass().getClassLoader());
		Class<?> withExampleMetaAnnotation = ClassUtils.forName(
				WithExampleMetaAnnotation.class.getName(), classLoader);
		Class<Annotation> exampleAnnotationClass = (Class<Annotation>) ClassUtils.forName(
				ExampleAnnotation.class.getName(), classLoader);
		Class<Annotation> exampleMetaAnnotationClass = (Class<Annotation>) ClassUtils.forName(
				ExampleMetaAnnotation.class.getName(), classLoader);
		assertThat(AnnotatedElementUtils.getMergedAnnotationAttributes(
				withExampleMetaAnnotation, exampleAnnotationClass)).isNull();
		assertThat(AnnotatedElementUtils.getMergedAnnotationAttributes(
				withExampleMetaAnnotation, exampleMetaAnnotationClass)).isNull();
		assertThat(AnnotatedElementUtils.hasAnnotation(withExampleMetaAnnotation,
				exampleAnnotationClass)).isFalse();
		assertThat(AnnotatedElementUtils.hasAnnotation(withExampleMetaAnnotation,
				exampleMetaAnnotationClass)).isFalse();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void filteredTypeInMetaAnnotationWhenUsingMergedAnnotationsHandlesException() throws Exception {
		FilteringClassLoader classLoader = new FilteringClassLoader(
				getClass().getClassLoader());
		Class<?> withExampleMetaAnnotation = ClassUtils.forName(
				WithExampleMetaAnnotation.class.getName(), classLoader);
		Class<Annotation> exampleAnnotationClass = (Class<Annotation>) ClassUtils.forName(
				ExampleAnnotation.class.getName(), classLoader);
		Class<Annotation> exampleMetaAnnotationClass = (Class<Annotation>) ClassUtils.forName(
				ExampleMetaAnnotation.class.getName(), classLoader);
		MergedAnnotations annotations = MergedAnnotations.from(withExampleMetaAnnotation);
		assertThat(annotations.get(exampleAnnotationClass).isPresent()).isFalse();
		assertThat(annotations.get(exampleMetaAnnotationClass).isPresent()).isFalse();
		assertThat(annotations.isPresent(exampleMetaAnnotationClass)).isFalse();
		assertThat(annotations.isPresent(exampleAnnotationClass)).isFalse();
	}


	static class FilteringClassLoader extends OverridingClassLoader {

		FilteringClassLoader(ClassLoader parent) {
			super(parent);
		}

		@Override
		protected boolean isEligibleForOverriding(String className) {
			return className.startsWith(
					AnnotationIntrospectionFailureTests.class.getName());
		}

		@Override
		protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
			if (name.startsWith(AnnotationIntrospectionFailureTests.class.getName()) &&
					name.contains("Filtered")) {
				throw new ClassNotFoundException(name);
			}
			return super.loadClass(name, resolve);
		}
	}

	static class FilteredType {
	}

	@Retention(RetentionPolicy.RUNTIME)
	@interface ExampleAnnotation {

		Class<?> value() default Void.class;
	}

	@ExampleAnnotation(FilteredType.class)
	static class WithExampleAnnotation {
	}

	@Retention(RetentionPolicy.RUNTIME)
	@ExampleAnnotation
	@interface ExampleMetaAnnotation {

		@AliasFor(annotation = ExampleAnnotation.class, attribute = "value")
		Class<?> example1() default Void.class;

		@AliasFor(annotation = ExampleAnnotation.class, attribute = "value")
		Class<?> example2() default Void.class;

	}

	@ExampleMetaAnnotation(example1 = FilteredType.class)
	static class WithExampleMetaAnnotation {
	}

}
