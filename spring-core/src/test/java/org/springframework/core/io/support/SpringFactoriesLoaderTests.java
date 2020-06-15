

package org.springframework.core.io.support;

import java.lang.reflect.Modifier;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Tests for {@link SpringFactoriesLoader}.
 *
 * @author Arjen Poutsma
 * @author Phillip Webb
 * @author Sam Brannen
 */
public class SpringFactoriesLoaderTests {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void loadFactoriesInCorrectOrder() {
		List<DummyFactory> factories = SpringFactoriesLoader.loadFactories(DummyFactory.class, null);
		assertEquals(2, factories.size());
		assertTrue(factories.get(0) instanceof MyDummyFactory1);
		assertTrue(factories.get(1) instanceof MyDummyFactory2);
	}

	@Test
	public void loadPackagePrivateFactory() {
		List<DummyPackagePrivateFactory> factories =
				SpringFactoriesLoader.loadFactories(DummyPackagePrivateFactory.class, null);
		assertEquals(1, factories.size());
		assertFalse(Modifier.isPublic(factories.get(0).getClass().getModifiers()));
	}

	@Test
	public void attemptToLoadFactoryOfIncompatibleType() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("Unable to instantiate factory class [org.springframework.core.io.support.MyDummyFactory1] for factory type [java.lang.String]");

		SpringFactoriesLoader.loadFactories(String.class, null);
	}

}
