package org.springframework.test.web.reactive.server

import org.reactivestreams.Publisher
import org.springframework.test.util.AssertionErrors.assertEquals
import org.springframework.test.web.reactive.server.WebTestClient.*

/**
 * Extension for [RequestBodySpec.body] providing a variant without explicit class
 * parameter thanks to Kotlin reified type parameters.
 *

 * @since 5.0
 */
inline fun <reified T : Any, S : Publisher<T>> RequestBodySpec.body(publisher: S): RequestHeadersSpec<*>
		= body(publisher, T::class.java)

/**
 * Extension for [ResponseSpec.expectBody] providing an `expectBody<Foo>()` variant and
 * a workaround for [KT-5464](https://youtrack.jetbrains.com/issue/KT-5464) which
 * prevents to use `WebTestClient.BodySpec` in Kotlin.
 *

 * @since 5.0
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
inline fun <reified B : Any> ResponseSpec.expectBody(): KotlinBodySpec<B> =
		expectBody(B::class.java).returnResult().let {
			object : KotlinBodySpec<B> {

				override fun isEqualTo(expected: B): KotlinBodySpec<B> = it
							.assertWithDiagnostics({ assertEquals("Response body", expected, it.responseBody) })
							.let { this }

				override fun consumeWith(consumer: (EntityExchangeResult<B>) -> Unit): KotlinBodySpec<B> =
					it
							.assertWithDiagnostics({ consumer.invoke(it) })
							.let { this }

				override fun returnResult(): EntityExchangeResult<B> = it
			}
		}

/**
 * Kotlin compliant `WebTestClient.BodySpec` for expectations on the response body decoded
 * to a single Object, see [KT-5464](https://youtrack.jetbrains.com/issue/KT-5464) for
 * more details.
 * @since 5.0.6
 */
interface KotlinBodySpec<B> {

	/**
	 * Assert the extracted body is equal to the given value.
	 */
	fun isEqualTo(expected: B): KotlinBodySpec<B>

	/**
	 * Assert the exchange result with the given consumer.
	 */
	fun consumeWith(consumer: (EntityExchangeResult<B>) -> Unit): KotlinBodySpec<B>

	/**
	 * Exit the chained API and return an `ExchangeResult` with the
	 * decoded response content.
	 */
	fun returnResult(): EntityExchangeResult<B>
}

/**
 * Extension for [ResponseSpec.expectBodyList] providing a `expectBodyList<Foo>()` variant.
 *

 * @since 5.0
 */
inline fun <reified E : Any> ResponseSpec.expectBodyList(): ListBodySpec<E> =
		expectBodyList(E::class.java)

/**
 * Extension for [ResponseSpec.returnResult] providing a `returnResult<Foo>()` variant.
 *

 * @since 5.0
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
inline fun <reified T : Any> ResponseSpec.returnResult(): FluxExchangeResult<T> =
		returnResult(T::class.java)
