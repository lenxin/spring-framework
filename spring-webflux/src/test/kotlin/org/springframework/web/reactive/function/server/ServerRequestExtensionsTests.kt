package org.springframework.web.reactive.function.client

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.codec.multipart.Part
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.server.*
import org.springframework.web.server.WebSession
import reactor.core.publisher.Mono
import java.security.Principal

/**
 * Mock object based tests for [ServerRequest] Kotlin extensions.
 *

 */
class ServerRequestExtensionsTests {

	val request = mockk<ServerRequest>(relaxed = true)

	@Test
	fun `bodyToMono with reified type parameters`() {
		request.bodyToMono<List<Foo>>()
		verify { request.bodyToMono(object : ParameterizedTypeReference<List<Foo>>() {}) }
	}

	@Test
	fun `bodyToFlux with reified type parameters`() {
		request.bodyToFlux<List<Foo>>()
		verify { request.bodyToFlux(object : ParameterizedTypeReference<List<Foo>>() {}) }
	}

	@Test
	@FlowPreview
	fun `bodyToFlow with reified type parameters`() {
		request.bodyToFlow<List<Foo>>()
		verify { request.bodyToFlux(object : ParameterizedTypeReference<List<Foo>>() {}) }
	}

	@Test
	fun awaitBody() {
		every { request.bodyToMono<String>() } returns Mono.just("foo")
		runBlocking {
			assertEquals("foo", request.awaitBody<String>())
		}
	}

	@Test
	fun awaitBodyOrNull() {
		every { request.bodyToMono<String>() } returns Mono.empty()
		runBlocking {
			assertNull(request.awaitBodyOrNull<String>())
		}
	}

	@Test
	fun awaitFormData() {
		val map = mockk<MultiValueMap<String, String>>()
		every { request.formData() } returns Mono.just(map)
		runBlocking {
			assertEquals(map, request.awaitFormData())
		}
	}

	@Test
	fun awaitMultipartData() {
		val map = mockk<MultiValueMap<String, Part>>()
		every { request.multipartData() } returns Mono.just(map)
		runBlocking {
			assertEquals(map, request.awaitMultipartData())
		}
	}

	@Test
	fun awaitPrincipal() {
		val principal = mockk<Principal>()
		every { request.principal() } returns Mono.just(principal)
		runBlocking {
			assertEquals(principal, request.awaitPrincipalOrNull())
		}
	}

	@Test
	fun awaitSession() {
		val session = mockk<WebSession>()
		every { request.session() } returns Mono.just(session)
		runBlocking {
			assertEquals(session, request.awaitSession())
		}
	}


	class Foo
}
