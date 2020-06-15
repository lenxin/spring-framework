

package org.springframework.web.servlet.function

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test
import org.springframework.core.ParameterizedTypeReference
import java.util.*

/**
 * Tests for WebMvc.fn [ServerResponse] extensions.
 *
 * @author Sebastien Deleuze
 */
class ServerResponseExtensionsTests {

	@Test
	fun bodyWithType() {
		val builder = mockk<ServerResponse.BodyBuilder>()
		val response = mockk<ServerResponse>()
		val body = Arrays.asList("foo", "bar")
		val typeReference = object: ParameterizedTypeReference<List<String>>() {}
		every { builder.body(body, typeReference) } returns response
		Assert.assertEquals(response, builder.bodyWithType<List<String>>(body))
		verify { builder.body(body, typeReference) }
	}
}
