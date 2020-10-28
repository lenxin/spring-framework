package org.springframework.context.support

import org.junit.Assert.assertNotNull
import org.junit.Test
import org.springframework.beans.factory.getBean

/**
 * Tests for [GenericApplicationContext] Kotlin extensions
 *

 */
class GenericApplicationContextExtensionsTests {

	@Test
	fun registerBeanWithClass() {
		val context = GenericApplicationContext()
		context.registerBean<BeanA>()
		context.refresh()
		assertNotNull(context.getBean<BeanA>())
	}

	@Test
	fun registerBeanWithNameAndClass() {
		val context = GenericApplicationContext()
		context.registerBean<BeanA>("a")
		context.refresh()
		assertNotNull(context.getBean("a"))
	}

	@Test
	fun registerBeanWithSupplier() {
		val context = GenericApplicationContext()
		context.registerBean { BeanA() }
		context.refresh()
		assertNotNull(context.getBean<BeanA>())
	}

	@Test
	fun registerBeanWithNameAndSupplier() {
		val context = GenericApplicationContext()
		context.registerBean("a") { BeanA() }
		context.refresh()
		assertNotNull(context.getBean("a"))
	}

	@Test
	fun registerBeanWithFunction() {
		val context = GenericApplicationContext()
		context.registerBean<BeanA>()
		context.registerBean { BeanB(it.getBean<BeanA>()) }
		context.refresh()
		assertNotNull(context.getBean<BeanA>())
		assertNotNull(context.getBean<BeanB>())
	}

	@Test
	fun registerBeanWithNameAndFunction() {
		val context = GenericApplicationContext()
		context.registerBean<BeanA>("a")
		context.registerBean("b") { BeanB(it.getBean<BeanA>()) }
		context.refresh()
		assertNotNull(context.getBean("a"))
		assertNotNull(context.getBean("b"))
	}

	class BeanA

	class BeanB(val a: BeanA)

}
