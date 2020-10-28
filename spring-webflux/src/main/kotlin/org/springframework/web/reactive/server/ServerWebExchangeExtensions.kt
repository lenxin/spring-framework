package org.springframework.web.reactive.server

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.springframework.http.codec.multipart.Part
import org.springframework.util.MultiValueMap
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebSession
import java.security.Principal

/**
 * Coroutines variant of [ServerWebExchange.getFormData].
 *

 * @since 5.2
 */
suspend fun ServerWebExchange.awaitFormData(): MultiValueMap<String, String> =
		this.formData.awaitSingle()

/**
 * Coroutines variant of [ServerWebExchange.getMultipartData].
 *

 * @since 5.2
 */
suspend fun ServerWebExchange.awaitMultipartData(): MultiValueMap<String, Part> =
		this.multipartData.awaitSingle()

/**
 * Coroutines variant of [ServerWebExchange.getPrincipal].
 *

 * @since 5.2
 */
suspend fun <T : Principal> ServerWebExchange.awaitPrincipal(): T =
		this.getPrincipal<T>().awaitSingle()

/**
 * Coroutines variant of [ServerWebExchange.getSession].
 *

 * @since 5.2
 */
suspend fun ServerWebExchange.awaitSession(): WebSession =
		this.session.awaitSingle()

/**
 * Coroutines variant of [ServerWebExchange.Builder.principal].
 *

 * @since 5.2
 */
fun ServerWebExchange.Builder.principal(supplier: suspend () -> Principal): ServerWebExchange.Builder
        = principal(GlobalScope.mono(Dispatchers.Unconfined) { supplier.invoke() })
