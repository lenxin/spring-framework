package org.springframework.core.annotation;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Tests for {@link PackagesAnnotationFilter}.
 *
 * @author Phillip Webb
 */
public class PackagesAnnotationFilterTests {

	@Test
	public void createWhenPackagesIsNullThrowsException() {
		assertThatIllegalArgumentException().isThrownBy(
				() -> new PackagesAnnotationFilter((String[]) null)).withMessage(
						"Packages array must not be null");
	}

	@Test
	public void createWhenPackagesContainsNullThrowsException() {
		assertThatIllegalArgumentException().isThrownBy(
				() -> new PackagesAnnotationFilter((String) null)).withMessage(
						"Packages array must not have empty elements");
	}

	@Test
	public void createWhenPackagesContainsEmptyTextThrowsException() {
		assertThatIllegalArgumentException().isThrownBy(
				() -> new PackagesAnnotationFilter("")).withMessage(
						"Packages array must not have empty elements");
	}

	@Test
	public void matchesWhenInPackageReturnsTrue() {
		PackagesAnnotationFilter filter = new PackagesAnnotationFilter("com.example");
		assertThat(filter.matches("com.example.Component")).isTrue();
	}

	@Test
	public void matchesWhenNotInPackageReturnsFalse() {
		PackagesAnnotationFilter filter = new PackagesAnnotationFilter("com.example");
		assertThat(filter.matches("org.springframework.sterotype.Component")).isFalse();
	}

	@Test
	public void matchesWhenInSimilarPackageReturnsFalse() {
		PackagesAnnotationFilter filter = new PackagesAnnotationFilter("com.example");
		assertThat(filter.matches("com.examples.Component")).isFalse();
	}

	@Test
	public void equalsAndHashCode() {
		PackagesAnnotationFilter filter1 = new PackagesAnnotationFilter("com.example",
				"org.springframework");
		PackagesAnnotationFilter filter2 = new PackagesAnnotationFilter(
				"org.springframework", "com.example");
		PackagesAnnotationFilter filter3 = new PackagesAnnotationFilter("com.examples");
		assertThat(filter1.hashCode()).isEqualTo(filter2.hashCode());
		assertThat(filter1).isEqualTo(filter1).isEqualTo(filter2).isNotEqualTo(filter3);
	}

}
