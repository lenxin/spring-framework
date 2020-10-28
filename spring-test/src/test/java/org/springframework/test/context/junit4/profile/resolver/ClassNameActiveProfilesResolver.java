package org.springframework.test.context.junit4.profile.resolver;

import org.springframework.test.context.ActiveProfilesResolver;

/**

 * @since 4.0
 */
public class ClassNameActiveProfilesResolver implements ActiveProfilesResolver {

	@Override
	public String[] resolve(Class<?> testClass) {
		return new String[] { testClass.getSimpleName().toLowerCase() };
	}
}
