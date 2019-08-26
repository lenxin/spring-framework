package org.springframework.util.xml;

import org.junit.Test;

public class XmlValidationModeDetectorTests {
	private static final String XML =
			"<?pi content?><root xmlns='namespace'><prefix:child xmlns:prefix='namespace2'><!--comment-->content</prefix:child></root>";
}