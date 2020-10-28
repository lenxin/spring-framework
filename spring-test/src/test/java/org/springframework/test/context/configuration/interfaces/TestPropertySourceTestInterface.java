package org.springframework.test.context.configuration.interfaces;

import org.springframework.test.context.TestPropertySource;

/**

 * @since 4.3
 */
@TestPropertySource(properties = { "foo = bar", "enigma: 42" })
interface TestPropertySourceTestInterface {
}
