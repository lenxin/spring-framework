package org.springframework.expression;

import org.springframework.lang.Nullable;

/**
 * Instances of a type comparator should be able to compare pairs of objects for equality.
 * The specification of the return value is the same as for {@link java.lang.Comparable}.
 *
 * @see java.lang.Comparable
 * @since 3.0
 */
public interface TypeComparator {
	/**
	 * Return {@code true} if the comparator can compare these two objects.
	 *
	 * @param firstObject  the first object
	 * @param secondObject the second object
	 * @return {@code true} if the comparator can compare these objects
	 */
	boolean canCompare(@Nullable Object firstObject, @Nullable Object secondObject);

	/**
	 * Compare two given objects.
	 *
	 * @param firstObject  the first object
	 * @param secondObject the second object
	 * @return 0 if they are equal, <0 if the first is smaller than the second,
	 * or >0 if the first is larger than the second
	 * @throws EvaluationException if a problem occurs during comparison
	 *                             (or if they are not comparable in the first place)
	 */
	int compare(@Nullable Object firstObject, @Nullable Object secondObject) throws EvaluationException;
}