package org.springframework.core.annotation;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests for {@link MergedAnnotationPredicates}.
 *
 * @author Phillip Webb
 */
public class MergedAnnotationPredicatesTests {

	@Test
	public void typeInStringArrayWhenNameMatchesAccepts() {
		MergedAnnotation<TestAnnotation> annotation = MergedAnnotations.from(
				WithTestAnnotation.class).get(TestAnnotation.class);
		assertThat(MergedAnnotationPredicates.typeIn(
				TestAnnotation.class.getName())).accepts(annotation);
	}

	@Test
	public void typeInStringArrayWhenNameDoesNotMatchRejects() {
		MergedAnnotation<TestAnnotation> annotation = MergedAnnotations.from(
				WithTestAnnotation.class).get(TestAnnotation.class);
		assertThat(MergedAnnotationPredicates.typeIn(
				MissingAnnotation.class.getName())).rejects(annotation);
	}

	@Test
	public void typeInClassArrayWhenNameMatchesAccepts() {
		MergedAnnotation<TestAnnotation> annotation =
				MergedAnnotations.from(WithTestAnnotation.class).get(TestAnnotation.class);
		assertThat(MergedAnnotationPredicates.typeIn(TestAnnotation.class)).accepts(annotation);
	}

	@Test
	public void typeInClassArrayWhenNameDoesNotMatchRejects() {
		MergedAnnotation<TestAnnotation> annotation =
				MergedAnnotations.from(WithTestAnnotation.class).get(TestAnnotation.class);
		assertThat(MergedAnnotationPredicates.typeIn(MissingAnnotation.class)).rejects(annotation);
	}

	@Test
	public void typeInCollectionWhenMatchesStringInCollectionAccepts() {
		MergedAnnotation<TestAnnotation> annotation = MergedAnnotations.from(
				WithTestAnnotation.class).get(TestAnnotation.class);
		assertThat(MergedAnnotationPredicates.typeIn(
				Collections.singleton(TestAnnotation.class.getName()))).accepts(annotation);
	}

	@Test
	public void typeInCollectionWhenMatchesClassInCollectionAccepts() {
		MergedAnnotation<TestAnnotation> annotation = MergedAnnotations.from(
				WithTestAnnotation.class).get(TestAnnotation.class);
		assertThat(MergedAnnotationPredicates.typeIn(
				Collections.singleton(TestAnnotation.class))).accepts(annotation);
	}

	@Test
	public void typeInCollectionWhenDoesNotMatchAnyRejects() {
		MergedAnnotation<TestAnnotation> annotation = MergedAnnotations.from(
				WithTestAnnotation.class).get(TestAnnotation.class);
		assertThat(MergedAnnotationPredicates.typeIn(Arrays.asList(
				MissingAnnotation.class.getName(), MissingAnnotation.class))).rejects(annotation);
	}

	@Test
	public void firstRunOfAcceptsOnlyFirstRun() {
		List<MergedAnnotation<TestAnnotation>> filtered = MergedAnnotations.from(
				WithMultipleTestAnnotation.class).stream(TestAnnotation.class).filter(
						MergedAnnotationPredicates.firstRunOf(
								this::firstCharOfValue)).collect(Collectors.toList());
		assertThat(filtered.stream().map(
				annotation -> annotation.getString("value"))).containsExactly("a1", "a2", "a3");
	}

	@Test
	public void firstRunOfWhenValueExtractorIsNullThrowsException() {
		assertThatIllegalArgumentException().isThrownBy(
				() -> MergedAnnotationPredicates.firstRunOf(null));
	}

	@Test
	public void uniqueAcceptsUniquely() {
		List<MergedAnnotation<TestAnnotation>> filtered = MergedAnnotations.from(
				WithMultipleTestAnnotation.class).stream(TestAnnotation.class).filter(
						MergedAnnotationPredicates.unique(
								this::firstCharOfValue)).collect(Collectors.toList());
		assertThat(filtered.stream().map(
				annotation -> annotation.getString("value"))).containsExactly("a1", "b1", "c1");
	}

	@Test
	public void uniqueWhenKeyExtractorIsNullThrowsException() {
		assertThatIllegalArgumentException().isThrownBy(
				() -> MergedAnnotationPredicates.unique(null));
	}

	private char firstCharOfValue(MergedAnnotation<TestAnnotation> annotation) {
		return annotation.getString("value").charAt(0);
	}


	@Retention(RetentionPolicy.RUNTIME)
	@Repeatable(TestAnnotations.class)
	@interface TestAnnotation {

		String value() default "";
	}

	@Retention(RetentionPolicy.RUNTIME)
	@interface TestAnnotations {

		TestAnnotation[] value();
	}

	@interface MissingAnnotation {
	}

	@TestAnnotation("test")
	static class WithTestAnnotation {
	}

	@TestAnnotation("a1")
	@TestAnnotation("a2")
	@TestAnnotation("a3")
	@TestAnnotation("b1")
	@TestAnnotation("b2")
	@TestAnnotation("b3")
	@TestAnnotation("c1")
	@TestAnnotation("c2")
	@TestAnnotation("c3")
	static class WithMultipleTestAnnotation {
	}

}
