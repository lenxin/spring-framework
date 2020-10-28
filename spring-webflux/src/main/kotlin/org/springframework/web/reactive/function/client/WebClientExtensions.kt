package org.springframework.web.reactive.function.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactive.flow.asFlow
import kotlinx.coroutines.reactive.flow.asPublisher
import kotlinx.coroutines.reactor.mono
import org.reactivestreams.Publisher
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Extension for [WebClient.RequestBodySpec.body] providing a `body(Publisher<T>)` variant
 * leveraging Kotlin reified type parameters. This extension is not subject to type
 * erasure and retains actual generic type arguments.
 *

 * @since 5.0
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
inline fun <reified T : Any, S : Publisher<T>> RequestBodySpec.body(publisher: S): RequestHeadersSpec<*> =
		body(publisher, object : ParameterizedTypeReference<T>() {})

/**
 * Coroutines [Flow] based extension for [WebClient.RequestBodySpec.body] providing a
 * body(Flow<T>)` variant leveraging Kotlin reified type parameters. This extension is
 * not subject to type erasure and retains actual generic type arguments.
 *

 * @since 5.2
 */
@FlowPreview
inline fun <reified T : Any, S : Flow<T>> RequestBodySpec.body(flow: S): RequestHeadersSpec<*> =
		body(flow.asPublisher(), object : ParameterizedTypeReference<T>() {})

/**
 * Extension for [WebClient.ResponseSpec.bodyToMono] providing a `bodyToMono<Foo>()` variant
 * leveraging Kotlin reified type parameters. This extension is not subject to type
 * erasure and retains actual generic type arguments.
 *

 * @since 5.0
 */
inline fun <reified T : Any> WebClient.ResponseSpec.bodyToMono(): Mono<T> =
		bodyToMono(object : ParameterizedTypeReference<T>() {})


/**
 * Extension for [WebClient.ResponseSpec.bodyToFlux] providing a `bodyToFlux<Foo>()` variant
 * leveraging Kotlin reified type parameters. This extension is not subject to type
 * erasure and retains actual generic type arguments.
 *

 * @since 5.0
 */
inline fun <reified T : Any> WebClient.ResponseSpec.bodyToFlux(): Flux<T> =
		bodyToFlux(object : ParameterizedTypeReference<T>() {})

/**
 * Coroutines [kotlinx.coroutines.flow.Flow] based variant of [WebClient.ResponseSpec.bodyToFlux].
 *
 * Backpressure is controlled by [batchSize] parameter that controls the size of in-flight elements
 * and [org.reactivestreams.Subscription.request] size.
 *

 * @since 5.2
 */
@FlowPreview
inline fun <reified T : Any> WebClient.ResponseSpec.bodyToFlow(batchSize: Int = 1): Flow<T> =
		bodyToFlux<T>().asFlow(batchSize)


/**
 * Coroutines variant of [WebClient.RequestHeadersSpec.exchange].
 *

 * @since 5.2
 */
suspend fun WebClient.RequestHeadersSpec<out WebClient.RequestHeadersSpec<*>>.awaitExchange(): ClientResponse =
		exchange().awaitSingle()

/**
 * Coroutines variant of [WebClient.RequestBodySpec.body].
 *

 * @since 5.2
 */
inline fun <reified T: Any> WebClient.RequestBodySpec.body(crossinline supplier: suspend () -> T)
		= body(GlobalScope.mono(Dispatchers.Unconfined) { supplier.invoke() })

/**
 * Coroutines variant of [WebClient.ResponseSpec.bodyToMono].
 *

 * @since 5.2
 */
suspend inline fun <reified T : Any> WebClient.ResponseSpec.awaitBody() : T =
		bodyToMono<T>().awaitSingle()
