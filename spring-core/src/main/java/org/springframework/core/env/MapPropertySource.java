package org.springframework.core.env;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * {@link PropertySource} that reads keys and values from a {@code Map} object.
 *
 * @see PropertiesPropertySource
 * @since 3.1
 */
public class MapPropertySource extends EnumerablePropertySource<Map<String, Object>> {
	public MapPropertySource(String name, Map<String, Object> source) {
		super(name, source);
	}

	@Override
	@Nullable
	public Object getProperty(String name) {
		return this.source.get(name);
	}

	@Override
	public boolean containsProperty(String name) {
		return this.source.containsKey(name);
	}

	@Override
	public String[] getPropertyNames() {
		return StringUtils.toStringArray(this.source.keySet());
	}
}