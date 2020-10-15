package org.springframework.core.io.support;

import org.springframework.core.env.PropertySource;
import org.springframework.lang.Nullable;

import java.io.IOException;

/**
 * The default implementation for {@link PropertySourceFactory},
 * wrapping every resource in a {@link ResourcePropertySource}.
 *
 * @see PropertySourceFactory
 * @see ResourcePropertySource
 * @since 4.3
 */
public class DefaultPropertySourceFactory implements PropertySourceFactory {
	@Override
	public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource resource) throws IOException {
		return (name != null ? new ResourcePropertySource(name, resource) : new ResourcePropertySource(resource));
	}
}
