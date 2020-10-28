package org.springframework.context.annotation

/**
 * Extension for [AnnotationConfigApplicationContext] allowing
 * `AnnotationConfigApplicationContext { ... }` style initialization.
 *

 * @since 5.0
 */
fun AnnotationConfigApplicationContext(configure: AnnotationConfigApplicationContext.() -> Unit) =
		AnnotationConfigApplicationContext().apply(configure)
