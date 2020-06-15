

package org.springframework.web.reactive.function.server

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactive.flow.asFlow
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.codec.multipart.Part
import org.springframework.util.MultiValueMap
import org.springframework.web.server.WebSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.security.Principal

/**
 * Extension for [ServerRequest.bodyToMono] providing a `bodyToMono<Foo>()` variant
 * leveraging Kotlin reified type parameters. This extension is not subject to type
 * erasure and retains actual generic type arguments.
 * 
 * @author Sebastien Deleuze
 * @since 5.0
 */
inline fun <reified T : Any> ServerRequest.bodyToMono(): Mono<T> =
		bodyToMono(object : ParameterizedTypeReference<T>() {})

/**
 * Extension for [ServerRequest.bodyToFlux] providing a `bodyToFlux<Foo>()` variant
 * leveraging Kotlin reified type parameters. This extension is not subject to type
 * erasure and retains actual generic type arguments.
 *
 * @author Sebastien Deleuze
 * @since 5.0
 */
inline fun <reified T : Any> ServerRequest.bodyToFlux(): Flux<T> =
		bodyToFlux(object : ParameterizedTypeReference<T>() {})

/**
 * Coroutines [kotlinx.coroutines.flow.Flow] based variant of [ServerRequest.bodyToFlux].
 *
 * Backpressure is controlled by [batchSize] parameter that controls the size of in-flight elements
 * and [org.reactivestreams.Subscription.request] size.
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
@FlowPreview
inline fun <reified T : Any> ServerRequest.bodyToFlow(batchSize: Int = 1): Flow<T> =
		bodyToFlux<T>().asFlow(batchSize)

/**
 * Non-nullable Coroutines variant of [ServerRequest.bodyToMono].
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
suspend inline fun <reified T : Any> ServerRequest.awaitBody(): T =
		bodyToMono<T>().awaitSingle()

/**
 * Nullable Coroutines variant of [ServerRequest.bodyToMono].
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
suspend inline fun <reified T : Any> ServerRequest.awaitBodyOrNull(): T? =
		bodyToMono<T>().awaitFirstOrNull()

/**
 * Coroutines variant of [ServerRequest.formData].
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
suspend fun ServerRequest.awaitFormData(): MultiValueMap<String, String> =
		formData().awaitSingle()

/**
 * Coroutines variant of [ServerRequest.multipartData].
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
suspend fun ServerRequest.awaitMultipartData(): MultiValueMap<String, Part> =
		multipartData().awaitSingle()

/**
 * Coroutines variant of [ServerRequest.principal].
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
suspend fun ServerRequest.awaitPrincipalOrNull(): Principal? =
		principal().awaitFirstOrNull()

/**
 * Coroutines variant of [ServerRequest.session].
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
suspend fun ServerRequest.awaitSession(): WebSession =
		session().awaitSingle()
