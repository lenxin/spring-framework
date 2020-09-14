package org.springframework.core.annotation;

import org.junit.Test;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import javax.annotation.Nonnull;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AnnotationFilter}.
 */
public class AnnotationFilterTests {
	private static final AnnotationFilter FILTER = annotationType ->
			ObjectUtils.nullSafeEquals(annotationType, TestAnnotation.class.getName());

	@Test
	public void matchesAnnotationWhenMatchReturnsTrue() {
		TestAnnotation annotation = WithTestAnnotation.class.getDeclaredAnnotation(TestAnnotation.class);
		assertThat(FILTER.matches(annotation)).isTrue();
	}

	@Test
	public void matchesAnnotationWhenNoMatchReturnsFalse() {
		OtherAnnotation annotation = WithOtherAnnotation.class.getDeclaredAnnotation(OtherAnnotation.class);
		assertThat(FILTER.matches(annotation)).isFalse();
	}

	@Test
	public void matchesAnnotationClassWhenMatchReturnsTrue() {
		Class<TestAnnotation> annotationType = TestAnnotation.class;
		assertThat(FILTER.matches(annotationType)).isTrue();
	}

	@Test
	public void matchesAnnotationClassWhenNoMatchReturnsFalse() {
		Class<OtherAnnotation> annotationType = OtherAnnotation.class;
		assertThat(FILTER.matches(annotationType)).isFalse();
	}

	@Test
	public void plainWhenJavaLangAnnotationReturnsTrue() {
		assertThat(AnnotationFilter.PLAIN.matches(Retention.class)).isTrue();
	}

	@Test
	public void plainWhenSpringLangAnnotationReturnsTrue() {
		assertThat(AnnotationFilter.PLAIN.matches(Nullable.class)).isTrue();
	}

	@Test
	public void plainWhenOtherAnnotationReturnsFalse() {
		assertThat(AnnotationFilter.PLAIN.matches(TestAnnotation.class)).isFalse();
	}

	@Test
	public void javaWhenJavaLangAnnotationReturnsTrue() {
		assertThat(AnnotationFilter.JAVA.matches(Retention.class)).isTrue();
	}

	@Test
	public void javaWhenJavaxAnnotationReturnsTrue() {
		assertThat(AnnotationFilter.JAVA.matches(Nonnull.class)).isTrue();
	}

	@Test
	public void javaWhenSpringLangAnnotationReturnsFalse() {
		assertThat(AnnotationFilter.JAVA.matches(Nullable.class)).isFalse();
	}

	@Test
	public void javaWhenOtherAnnotationReturnsFalse() {
		assertThat(AnnotationFilter.JAVA.matches(TestAnnotation.class)).isFalse();
	}

	@Test
	public void noneReturnsFalse() {
		assertThat(AnnotationFilter.NONE.matches(Retention.class)).isFalse();
		assertThat(AnnotationFilter.NONE.matches(Nullable.class)).isFalse();
		assertThat(AnnotationFilter.NONE.matches(TestAnnotation.class)).isFalse();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@interface TestAnnotation {
	}

	@TestAnnotation
	static class WithTestAnnotation {
	}

	@Retention(RetentionPolicy.RUNTIME)
	@interface OtherAnnotation {
	}

	@OtherAnnotation
	static class WithOtherAnnotation {
	}
}