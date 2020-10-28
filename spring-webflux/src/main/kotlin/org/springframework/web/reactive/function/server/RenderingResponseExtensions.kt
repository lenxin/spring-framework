package org.springframework.web.reactive.function.server

import kotlinx.coroutines.reactive.awaitSingle

/**
 * Coroutines variant of [RenderingResponse.Builder.build].
 *

 * @since 5.2
 */
suspend fun RenderingResponse.Builder.buildAndAwait(): RenderingResponse = build().awaitSingle()
