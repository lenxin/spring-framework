package org.springframework.test.context.junit4;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.ProfileValueSource;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.context.TestExecutionListeners;

import static org.junit.Assert.*;

/**
 * Verifies proper handling of JUnit's {@link Ignore &#064;Ignore} and Spring's
 * {@link IfProfileValue &#064;IfProfileValue} and
 * {@link ProfileValueSourceConfiguration &#064;ProfileValueSourceConfiguration}
 * (with the <em>implicit, default {@link ProfileValueSource}</em>) annotations in
 * conjunction with the {@link SpringRunner}.
 * <p>
 * Note that {@link TestExecutionListeners &#064;TestExecutionListeners} is
 * explicitly configured with an empty list, thus disabling all default
 * listeners.
 *
 * @author Sam Brannen
 * @since 2.5
 * @see HardCodedProfileValueSourceSpringRunnerTests
 */
@RunWith(SpringRunner.class)
@TestExecutionListeners( {})
public class EnabledAndIgnoredSpringRunnerTests {

	protected static final String NAME = "EnabledAndIgnoredSpringRunnerTests.profile_value.name";

	protected static final String VALUE = "enigma";

	protected static int numTestsExecuted = 0;


	@BeforeClass
	public static void setProfileValue() {
		numTestsExecuted = 0;
		System.setProperty(NAME, VALUE);
	}

	@AfterClass
	public static void verifyNumTestsExecuted() {
		assertEquals("Verifying the number of tests executed.", 3, numTestsExecuted);
	}

	@Test
	@IfProfileValue(name = NAME, value = VALUE + "X")
	public void testIfProfileValueDisabled() {
		numTestsExecuted++;
		fail("The body of a disabled test should never be executed!");
	}

	@Test
	@IfProfileValue(name = NAME, value = VALUE)
	public void testIfProfileValueEnabledViaSingleValue() {
		numTestsExecuted++;
	}

	@Test
	@IfProfileValue(name = NAME, values = { "foo", VALUE, "bar" })
	public void testIfProfileValueEnabledViaMultipleValues() {
		numTestsExecuted++;
	}

	@Test
	public void testIfProfileValueNotConfigured() {
		numTestsExecuted++;
	}

	@Test
	@Ignore
	public void testJUnitIgnoreAnnotation() {
		numTestsExecuted++;
		fail("The body of an ignored test should never be executed!");
	}

}
