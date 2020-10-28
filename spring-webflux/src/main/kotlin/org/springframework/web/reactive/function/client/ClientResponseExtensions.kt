package org.springframework.web.reactive.function.client

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactive.flow.asFlow
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Extension for [ClientResponse.bodyToMono] providing a `bodyToMono<Foo>()` variant
 * leveraging Kotlin reified type parameters. This extension is not subject to type
 * erasure and retains actual generic type arguments.
 *

 * @since 5.0
 */
inline fun <reified T : Any> ClientResponse.bodyToMono(): Mono<T> =
		bodyToMono(object : ParameterizedTypeReference<T>() {})

/**
 * Extension for [ClientResponse.bodyToFlux] providing a `bodyToFlux<Foo>()` variant
 * leveraging Kotlin reified type parameters. This extension is not subject to type
 * erasure and retains actual generic type arguments.
 *

 * @since 5.0
 */
inline fun <reified T : Any> ClientResponse.bodyToFlux(): Flux<T> =
		bodyToFlux(object : ParameterizedTypeReference<T>() {})

/**
 * Coroutines [kotlinx.coroutines.flow.Flow] based variant of [ClientResponse.bodyToFlux].
 *
 * Backpressure is controlled by [batchSize] parameter that controls the size of in-flight elements
 * and [org.reactivestreams.Subscription.request] size.
 *

 * @since 5.2
 */
@FlowPreview
inline fun <reified T : Any> ClientResponse.bodyToFlow(batchSize: Int = 1): Flow<T> =
		bodyToFlux<T>().asFlow(batchSize)

/**
 * Extension for [ClientResponse.toEntity] providing a `toEntity<Foo>()` variant
 * leveraging Kotlin reified type parameters. This extension is not subject to type
 * erasure and retains actual generic type arguments.
 *

 * @since 5.0
 */
inline fun <reified T : Any> ClientResponse.toEntity(): Mono<ResponseEntity<T>> =
		toEntity(object : ParameterizedTypeReference<T>() {})

/**
 * Extension for [ClientResponse.toEntityList] providing a `bodyToEntityList<Foo>()` variant
 * leveraging Kotlin reified type parameters. This extension is not subject to type
 * erasure and retains actual generic type arguments.
 *

 * @since 5.0
 */
inline fun <reified T : Any> ClientResponse.toEntityList(): Mono<ResponseEntity<List<T>>> =
		toEntityList(object : ParameterizedTypeReference<T>() {})

/**
 * Non-nullable Coroutines variant of [ClientResponse.bodyToMono].
 *

 * @since 5.2
 */
suspend inline fun <reified T : Any> ClientResponse.awaitBody(): T =
		bodyToMono<T>().awaitSingle()

/**
 * Nullable coroutines variant of [ClientResponse.bodyToMono].
 *

 * @since 5.2
 */
suspend inline fun <reified T : Any> ClientResponse.awaitBodyOrNull(): T? =
		bodyToMono<T>().awaitFirstOrNull()

/**
 * Coroutines variant of [ClientResponse.toEntity].
 *

 * @since 5.2
 */
suspend inline fun <reified T : Any> ClientResponse.awaitEntity(): ResponseEntity<T> =
		toEntity<T>().awaitSingle()

/**
 * Coroutines variant of [ClientResponse.toEntityList].
 *

 * @since 5.2
 */
suspend inline fun <reified T : Any> ClientResponse.awaitEntityList(): ResponseEntity<List<T>> =
		toEntityList<T>().awaitSingle()
