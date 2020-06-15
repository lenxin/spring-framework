

package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests for {@link TypeMappedAnnotation}. See also
 * {@link MergedAnnotationsTests} for a much more extensive collection of tests.
 *
 * @author Phillip Webb
 */
public class TypeMappedAnnotationTests {

	@Test
	public void mappingWhenMirroredReturnsMirroredValues() {
		testExplicitMirror(WithExplicitMirrorA.class);
		testExplicitMirror(WithExplicitMirrorB.class);
	}

	private void testExplicitMirror(Class<?> annotatedClass) {
		TypeMappedAnnotation<ExplicitMirror> annotation = getTypeMappedAnnotation(
				annotatedClass, ExplicitMirror.class);
		assertThat(annotation.getString("a")).isEqualTo("test");
		assertThat(annotation.getString("b")).isEqualTo("test");
	}

	@Test
	public void mappingExplicitAliasToMetaAnnotationReturnsMappedValues() {
		TypeMappedAnnotation<?> annotation = getTypeMappedAnnotation(
				WithExplicitAliasToMetaAnnotation.class,
				ExplicitAliasToMetaAnnotation.class,
				ExplicitAliasMetaAnnotationTarget.class);
		assertThat(annotation.getString("aliased")).isEqualTo("aliased");
		assertThat(annotation.getString("nonAliased")).isEqualTo("nonAliased");
	}

	@Test
	public void mappingConventionAliasToMetaAnnotationReturnsMappedValues() {
		TypeMappedAnnotation<?> annotation = getTypeMappedAnnotation(
				WithConventionAliasToMetaAnnotation.class,
				ConventionAliasToMetaAnnotation.class,
				ConventionAliasMetaAnnotationTarget.class);
		assertThat(annotation.getString("value")).isEqualTo("");
		assertThat(annotation.getString("convention")).isEqualTo("convention");
	}

	private <A extends Annotation> TypeMappedAnnotation<A> getTypeMappedAnnotation(
			Class<?> source, Class<A> annotationType) {
		return getTypeMappedAnnotation(source, annotationType, annotationType);
	}

	private <A extends Annotation> TypeMappedAnnotation<A> getTypeMappedAnnotation(
			Class<?> source, Class<? extends Annotation> rootAnnotationType,
			Class<A> annotationType) {
		Annotation rootAnnotation = source.getAnnotation(rootAnnotationType);
		AnnotationTypeMapping mapping = getMapping(rootAnnotation, annotationType);
		return TypeMappedAnnotation.createIfPossible(mapping, source, rootAnnotation, 0, IntrospectionFailureLogger.INFO);
	}

	private AnnotationTypeMapping getMapping(Annotation annotation,
			Class<? extends Annotation> mappedAnnotationType) {
		AnnotationTypeMappings mappings = AnnotationTypeMappings.forAnnotationType(
				annotation.annotationType());
		for (int i = 0; i < mappings.size(); i++) {
			AnnotationTypeMapping candidate = mappings.get(i);
			if (candidate.getAnnotationType().equals(mappedAnnotationType)) {
				return candidate;
			}
		}
		throw new IllegalStateException(
				"No mapping from " + annotation + " to " + mappedAnnotationType);
	}

	@Retention(RetentionPolicy.RUNTIME)
	static @interface ExplicitMirror {

		@AliasFor("b")
		String a() default "";

		@AliasFor("a")
		String b() default "";

	}

	@ExplicitMirror(a = "test")
	static class WithExplicitMirrorA {

	}

	@ExplicitMirror(b = "test")
	static class WithExplicitMirrorB {

	}

	@Retention(RetentionPolicy.RUNTIME)
	@ExplicitAliasMetaAnnotationTarget(nonAliased = "nonAliased")
	static @interface ExplicitAliasToMetaAnnotation {

		@AliasFor(annotation = ExplicitAliasMetaAnnotationTarget.class)
		String aliased() default "";

	}

	@Retention(RetentionPolicy.RUNTIME)
	static @interface ExplicitAliasMetaAnnotationTarget {

		String aliased() default "";

		String nonAliased() default "";

	}

	@ExplicitAliasToMetaAnnotation(aliased = "aliased")
	private static class WithExplicitAliasToMetaAnnotation {

	}

	@Retention(RetentionPolicy.RUNTIME)
	@ConventionAliasMetaAnnotationTarget
	static @interface ConventionAliasToMetaAnnotation {

		String value() default "";

		String convention() default "";

	}

	@Retention(RetentionPolicy.RUNTIME)
	static @interface ConventionAliasMetaAnnotationTarget {

		String value() default "";

		String convention() default "";

	}

	@ConventionAliasToMetaAnnotation(value = "value", convention = "convention")
	private static class WithConventionAliasToMetaAnnotation {

	}

}
