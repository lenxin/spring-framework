package org.springframework.test.web.reactive.server

import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import org.reactivestreams.Publisher
import org.springframework.web.reactive.function.server.router

/**
 * Mock object based tests for [WebTestClient] Kotlin extensions
 *

 */
class WebTestClientExtensionsTests {

	val requestBodySpec = mockk<WebTestClient.RequestBodySpec>(relaxed = true)

	val responseSpec = mockk<WebTestClient.ResponseSpec>(relaxed = true)


	@Test
	fun `RequestBodySpec#body with Publisher and reified type parameters`() {
		val body = mockk<Publisher<Foo>>()
		requestBodySpec.body(body)
		verify { requestBodySpec.body(body, Foo::class.java) }
	}

	@Test
	fun `ResponseSpec#expectBody with reified type parameters`() {
		responseSpec.expectBody<Foo>()
		verify { responseSpec.expectBody(Foo::class.java) }
	}

	@Test
	fun `KotlinBodySpec#isEqualTo`() {
		WebTestClient
				.bindToRouterFunction( router { GET("/") { ok().syncBody("foo") } } )
				.build()
				.get().uri("/").exchange().expectBody<String>().isEqualTo("foo")
	}

	@Test
	fun `KotlinBodySpec#consumeWith`() {
		WebTestClient
				.bindToRouterFunction( router { GET("/") { ok().syncBody("foo") } } )
				.build()
				.get().uri("/").exchange().expectBody<String>().consumeWith { assertEquals("foo", it.responseBody) }
	}

	@Test
	fun `KotlinBodySpec#returnResult`() {
		WebTestClient
				.bindToRouterFunction( router { GET("/") { ok().syncBody("foo") } } )
				.build()
				.get().uri("/").exchange().expectBody<String>().returnResult().apply { assertEquals("foo", responseBody) }
	}

	@Test
	fun `ResponseSpec#expectBodyList with reified type parameters`() {
		responseSpec.expectBodyList<Foo>()
		verify { responseSpec.expectBodyList(Foo::class.java) }
	}

	@Test
	fun `ResponseSpec#returnResult with reified type parameters`() {
		responseSpec.returnResult<Foo>()
		verify { responseSpec.returnResult(Foo::class.java) }
	}

	class Foo

}
