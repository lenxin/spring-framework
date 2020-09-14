package org.springframework.core.type.filter;

import org.springframework.core.type.ClassMetadata;
import org.springframework.util.Assert;

import java.util.regex.Pattern;

/**
 * A simple filter for matching a fully-qualified class name with a regex {@link Pattern}.
 *
 * @since 2.5
 */
public class RegexPatternTypeFilter extends AbstractClassTestingTypeFilter {
	private final Pattern pattern;

	public RegexPatternTypeFilter(Pattern pattern) {
		Assert.notNull(pattern, "Pattern must not be null");
		this.pattern = pattern;
	}

	@Override
	protected boolean match(ClassMetadata metadata) {
		return this.pattern.matcher(metadata.getClassName()).matches();
	}
}
