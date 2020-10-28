package org.springframework.test.web.servlet.htmlunit;

import org.junit.Test;

/**
 * Unit tests for {@link UrlRegexRequestMatcher}.
 *


 * @since 4.2
 */
public class UrlRegexRequestMatcherTests extends AbstractWebRequestMatcherTests {

	@Test
	public void verifyExampleInClassLevelJavadoc() throws Exception {
		WebRequestMatcher cdnMatcher = new UrlRegexRequestMatcher(".*?//code.jquery.com/.*");
		assertMatches(cdnMatcher, "https://code.jquery.com/jquery-1.11.0.min.js");
		assertDoesNotMatch(cdnMatcher, "http://localhost/jquery-1.11.0.min.js");
	}

}
