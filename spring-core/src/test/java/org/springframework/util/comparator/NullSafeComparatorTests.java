package org.springframework.util.comparator;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link NullSafeComparator}.
 */
public class NullSafeComparatorTests {

	@SuppressWarnings("unchecked")
	@Test
	public void shouldCompareWithNullsLow() {
		Comparator<String> c = NullSafeComparator.NULLS_LOW;
		assertTrue(c.compare(null, "boo") < 0);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldCompareWithNullsHigh() {
		Comparator<String> c = NullSafeComparator.NULLS_HIGH;
		assertTrue(c.compare(null, "boo") > 0);
		assertTrue(c.compare(null, null) == 0);
	}

}
