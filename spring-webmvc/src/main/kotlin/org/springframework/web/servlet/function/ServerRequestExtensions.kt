package org.springframework.web.servlet.function

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import java.net.InetSocketAddress
import java.security.Principal

/**
 * Nullable variant of [ServerRequest.remoteAddress]
 *

 * @since 5.2
 */
fun ServerRequest.remoteAddressOrNull(): InetSocketAddress? = remoteAddress().orElse(null)

/**
 * Extension for [ServerRequest.body] providing a `body<Foo>()` variant
 * leveraging Kotlin reified type parameters. This extension is not subject to type
 * erasure and retains actual generic type arguments.
 *

 * @since 5.2
 */
inline fun <reified T : Any> ServerRequest.body(): T = body(object : ParameterizedTypeReference<T>() {})

/**
 * Nullable variant of [ServerRequest.attribute]
 *

 * @since 5.2
 */
fun ServerRequest.attributeOrNull(name: String): Any? = attribute(name).orElse(null)

/**
 * Nullable variant of [ServerRequest.param]
 *

 * @since 5.2
 */
fun ServerRequest.paramOrNull(name: String): String? = param(name).orElse(null)

/**
 * Nullable variant of [ServerRequest.param]
 *

 * @since 5.2
 */
fun ServerRequest.principalOrNull(): Principal? = principal().orElse(null)

/**
 * Nullable variant of [ServerRequest.Headers.contentLength]
 *

 * @since 5.2
 */
fun ServerRequest.Headers.contentLengthOrNull(): Long? = contentLength().let { if (it.isPresent) it.asLong else null }

/**
 * Nullable variant of [ServerRequest.Headers.contentType]
 *

 * @since 5.2
 */
fun ServerRequest.Headers.contentTypeOrNull(): MediaType? = contentType().orElse(null)
