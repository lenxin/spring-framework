package org.springframework.test.context.junit4.profile.resolver;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author Michail Nikolaev
 * @since 4.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@ActiveProfiles(resolver = ClassNameActiveProfilesResolver.class)
public class ClassNameActiveProfilesResolverTests {

	@Configuration
	static class Config {

	}


	@Autowired
	private ApplicationContext applicationContext;


	@Test
	public void test() {
		assertTrue(Arrays.asList(applicationContext.getEnvironment().getActiveProfiles()).contains(
			getClass().getSimpleName().toLowerCase()));
	}

}
