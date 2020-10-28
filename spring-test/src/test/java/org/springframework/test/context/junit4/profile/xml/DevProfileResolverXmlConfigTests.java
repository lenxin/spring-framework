package org.springframework.test.context.junit4.profile.xml;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ActiveProfilesResolver;

/**

 * @since 4.0
 */
@ActiveProfiles(resolver = DevProfileResolverXmlConfigTests.class, inheritProfiles = false)
public class DevProfileResolverXmlConfigTests extends DevProfileXmlConfigTests implements ActiveProfilesResolver {

	@Override
	public String[] resolve(Class<?> testClass) {
		return new String[] { "dev" };
	}

}
