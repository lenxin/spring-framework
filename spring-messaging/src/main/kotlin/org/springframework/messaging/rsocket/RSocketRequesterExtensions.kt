package org.springframework.messaging.rsocket

import io.rsocket.transport.ClientTransport
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactive.flow.asFlow
import kotlinx.coroutines.reactive.flow.asPublisher
import org.springframework.core.ParameterizedTypeReference
import java.net.URI

/**
 * Coroutines variant of [RSocketRequester.Builder.connect].
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
suspend fun RSocketRequester.Builder.connectAndAwait(transport: ClientTransport): RSocketRequester =
		connect(transport).awaitSingle()

/**
 * Coroutines variant of [RSocketRequester.Builder.connectTcp].
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
suspend fun RSocketRequester.Builder.connectTcpAndAwait(host: String, port: Int): RSocketRequester =
		connectTcp(host, port).awaitSingle()

/**
 * Coroutines variant of [RSocketRequester.Builder.connectWebSocket].
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
suspend fun RSocketRequester.Builder.connectWebSocketAndAwait(uri: URI): RSocketRequester =
		connectWebSocket(uri).awaitSingle()


/**
 * Kotlin [Flow] variant of [RSocketRequester.RequestSpec.data].
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
@FlowPreview
fun <T : Any> RSocketRequester.RequestSpec.dataFlow(data: Flow<T>): RSocketRequester.ResponseSpec =
		data(data.asPublisher(), object : ParameterizedTypeReference<T>() {})

/**
 * Coroutines variant of [RSocketRequester.ResponseSpec.send].
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
suspend fun RSocketRequester.ResponseSpec.sendAndAwait() {
	send().awaitFirstOrNull()
}

/**
 * Coroutines variant of [RSocketRequester.ResponseSpec.retrieveMono].
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
suspend fun <T : Any> RSocketRequester.ResponseSpec.retrieveAndAwait(): T =
		retrieveMono(object : ParameterizedTypeReference<T>() {}).awaitSingle()

/**
 * Coroutines variant of [RSocketRequester.ResponseSpec.retrieveFlux].
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
@FlowPreview
fun <T : Any> RSocketRequester.ResponseSpec.retrieveFlow(batchSize: Int = 1): Flow<T> =
		retrieveFlux(object : ParameterizedTypeReference<T>() {}).asFlow(batchSize)
