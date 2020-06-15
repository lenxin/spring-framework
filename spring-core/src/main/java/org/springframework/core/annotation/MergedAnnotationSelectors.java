

package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.util.function.Predicate;

/**
 * {@link MergedAnnotationSelector} implementations that provide various options
 * for {@link MergedAnnotation} instances.
 *
 * @author Phillip Webb
 * @since 5.2
 * @see MergedAnnotations#get(Class, Predicate, MergedAnnotationSelector)
 * @see MergedAnnotations#get(String, Predicate, MergedAnnotationSelector)
 */
public abstract class MergedAnnotationSelectors {

	private static final MergedAnnotationSelector<?> NEAREST = new Nearest();

	private static final MergedAnnotationSelector<?> FIRST_DIRECTLY_DECLARED = new FirstDirectlyDeclared();


	private MergedAnnotationSelectors() {
	}


	/**
	 * Select the nearest annotation, i.e. the one with the lowest depth.
	 * @return a selector that picks the annotation with the lowest depth
	 */
	@SuppressWarnings("unchecked")
	public static <A extends Annotation> MergedAnnotationSelector<A> nearest() {
		return (MergedAnnotationSelector<A>) NEAREST;
	}

	/**
	 * Select the first directly declared annotation when possible. If not direct
	 * annotations are declared then the earliest annotation is selected.
	 * @return a selector that picks the first directly declared annotation whenever possible
	 */
	@SuppressWarnings("unchecked")
	public static <A extends Annotation> MergedAnnotationSelector<A> firstDirectlyDeclared() {
		return (MergedAnnotationSelector<A>) FIRST_DIRECTLY_DECLARED;
	}


	/**
	 * {@link MergedAnnotationSelector} to select the nearest annotation.
	 */
	private static class Nearest implements MergedAnnotationSelector<Annotation> {

		@Override
		public boolean isBestCandidate(MergedAnnotation<Annotation> annotation) {
			return annotation.getDepth() == 0;
		}

		@Override
		public MergedAnnotation<Annotation> select(
				MergedAnnotation<Annotation> existing, MergedAnnotation<Annotation> candidate) {

			if (candidate.getDepth() < existing.getDepth()) {
				return candidate;
			}
			return existing;
		}

	}


	/**
	 * {@link MergedAnnotationSelector} to select the first directly declared
	 * annotation.
	 */
	private static class FirstDirectlyDeclared implements MergedAnnotationSelector<Annotation> {

		@Override
		public boolean isBestCandidate(MergedAnnotation<Annotation> annotation) {
			return annotation.getDepth() == 0;
		}

		@Override
		public MergedAnnotation<Annotation> select(
				MergedAnnotation<Annotation> existing, MergedAnnotation<Annotation> candidate) {

			if (existing.getDepth() > 0 && candidate.getDepth() == 0) {
				return candidate;
			}
			return existing;
		}

	}

}
