package org.springframework.core

import org.junit.Assert.assertEquals
import org.junit.Test
import org.springframework.core.GenericTypeResolver.resolveReturnTypeArgument
import java.lang.reflect.Method

/**
 * Tests for Kotlin support in [GenericTypeResolver].
 *
 * @author Konrad Kaminski
 * @author Sebastien Deleuze
 */
class KotlinGenericTypeResolverTests {

	@Test
	fun methodReturnTypes() {
		assertEquals(Integer::class.java, resolveReturnTypeArgument(findMethod(MyTypeWithMethods::class.java, "integer")!!,
				MyInterfaceType::class.java))
		assertEquals(String::class.java, resolveReturnTypeArgument(findMethod(MyTypeWithMethods::class.java, "string")!!,
				MyInterfaceType::class.java))
		assertEquals(null, resolveReturnTypeArgument(findMethod(MyTypeWithMethods::class.java, "raw")!!,
				MyInterfaceType::class.java))
		assertEquals(null, resolveReturnTypeArgument(findMethod(MyTypeWithMethods::class.java, "object")!!,
				MyInterfaceType::class.java))
	}

	private fun findMethod(clazz: Class<*>, name: String): Method? =
			clazz.methods.firstOrNull { it.name == name }

	open class MyTypeWithMethods<T> {
		suspend fun integer(): MyInterfaceType<Int>? = null

		suspend fun string(): MySimpleInterfaceType? = null

		suspend fun `object`(): Any? = null

		suspend fun raw(): MyInterfaceType<*>? = null
	}

	interface MyInterfaceType<T>

	interface MySimpleInterfaceType: MyInterfaceType<String>

	open class MySimpleTypeWithMethods: MyTypeWithMethods<Int>()
}
