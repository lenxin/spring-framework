package org.springframework.context.support

import org.springframework.beans.factory.config.BeanDefinitionCustomizer
import org.springframework.context.ApplicationContext
import java.util.function.Supplier

/**
 * Extension for [GenericApplicationContext.registerBean] providing a
 * `registerBean<Foo>()` variant.
 *

 * @since 5.0
 */
inline fun <reified T : Any> GenericApplicationContext.registerBean(vararg customizers: BeanDefinitionCustomizer) {
	registerBean(T::class.java, *customizers)
}

/**
 * Extension for [GenericApplicationContext.registerBean] providing a
 * `registerBean<Foo>("foo")` variant.
 *

 * @since 5.0
 */
inline fun <reified T : Any> GenericApplicationContext.registerBean(beanName: String,
		vararg customizers: BeanDefinitionCustomizer) {
	registerBean(beanName, T::class.java, *customizers)
}

/**
 * Extension for [GenericApplicationContext.registerBean] providing a `registerBean { Foo() }` variant.
 *

 * @since 5.0
 */
inline fun <reified T : Any> GenericApplicationContext.registerBean(
		vararg customizers: BeanDefinitionCustomizer, crossinline function: (ApplicationContext) -> T) {
	registerBean(T::class.java, Supplier { function.invoke(this) }, *customizers)
}

/**
 * Extension for [GenericApplicationContext.registerBean] providing a
 * `registerBean("foo") { Foo() }` variant.
 *

 * @since 5.0
 */
inline fun <reified T : Any> GenericApplicationContext.registerBean(name: String,
		vararg customizers: BeanDefinitionCustomizer, crossinline function: (ApplicationContext) -> T) {
	registerBean(name, T::class.java, Supplier { function.invoke(this) }, *customizers)
}

/**
 * Extension for [GenericApplicationContext] allowing `GenericApplicationContext { ... }`
 * style initialization.
 *

 * @since 5.0
 */
@Deprecated("Use regular apply method instead.", replaceWith = ReplaceWith("GenericApplicationContext().apply(configure)"))
fun GenericApplicationContext(configure: GenericApplicationContext.() -> Unit) =
		GenericApplicationContext().apply(configure)

