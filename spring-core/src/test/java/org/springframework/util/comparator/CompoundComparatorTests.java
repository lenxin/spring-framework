package org.springframework.util.comparator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Comparator;

/**
 * Test for {@link CompoundComparator}.
 */
@Deprecated
public class CompoundComparatorTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void shouldNeedAtLeastOneComparator() {
		Comparator<String> c = new CompoundComparator<>();
		thrown.expect(IllegalStateException.class);
		c.compare("foo", "bar");
	}

}
