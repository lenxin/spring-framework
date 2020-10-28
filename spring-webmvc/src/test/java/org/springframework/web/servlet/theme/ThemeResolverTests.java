package org.springframework.web.servlet.theme;

import org.junit.Test;

import org.springframework.mock.web.test.MockHttpServletRequest;
import org.springframework.mock.web.test.MockHttpServletResponse;
import org.springframework.mock.web.test.MockServletContext;
import org.springframework.web.servlet.ThemeResolver;

import static org.junit.Assert.*;

/**


 * @since 19.06.2003
 */
public class ThemeResolverTests {

	private static final String TEST_THEME_NAME = "test.theme";
	private static final String DEFAULT_TEST_THEME_NAME = "default.theme";

	private void internalTest(ThemeResolver themeResolver, boolean shouldSet, String defaultName) {
		// create mocks
		MockServletContext context = new MockServletContext();
		MockHttpServletRequest request = new MockHttpServletRequest(context);
		MockHttpServletResponse response = new MockHttpServletResponse();
		// check original theme
		String themeName = themeResolver.resolveThemeName(request);
		assertEquals(themeName, defaultName);
		// set new theme name
		try {
			themeResolver.setThemeName(request, response, TEST_THEME_NAME);
			if (!shouldSet)
				fail("should not be able to set Theme name");
			// check new theme namelocale
			themeName = themeResolver.resolveThemeName(request);
			assertEquals(TEST_THEME_NAME, themeName);
			themeResolver.setThemeName(request, response, null);
			themeName = themeResolver.resolveThemeName(request);
			assertEquals(themeName, defaultName);
		}
		catch (UnsupportedOperationException ex) {
			if (shouldSet)
				fail("should be able to set Theme name");
		}
	}

	@Test
	public void fixedThemeResolver() {
		internalTest(new FixedThemeResolver(), false, AbstractThemeResolver.ORIGINAL_DEFAULT_THEME_NAME);
	}

	@Test
	public void cookieThemeResolver() {
		internalTest(new CookieThemeResolver(), true, AbstractThemeResolver.ORIGINAL_DEFAULT_THEME_NAME);
	}

	@Test
	public void sessionThemeResolver() {
		internalTest(new SessionThemeResolver(), true,AbstractThemeResolver.ORIGINAL_DEFAULT_THEME_NAME);
	}

	@Test
	public void sessionThemeResolverWithDefault() {
		SessionThemeResolver tr = new SessionThemeResolver();
		tr.setDefaultThemeName(DEFAULT_TEST_THEME_NAME);
		internalTest(tr, true, DEFAULT_TEST_THEME_NAME);
	}

}
