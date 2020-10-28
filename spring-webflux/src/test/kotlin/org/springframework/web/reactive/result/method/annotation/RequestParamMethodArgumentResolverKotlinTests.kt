package org.springframework.web.reactive.result.method.annotation

import org.junit.Before
import org.junit.Test
import org.springframework.core.MethodParameter
import org.springframework.core.ReactiveAdapterRegistry
import org.springframework.core.annotation.SynthesizingMethodParameter
import org.springframework.format.support.DefaultFormattingConversionService
import org.springframework.mock.http.server.reactive.test.MockServerHttpRequest
import org.springframework.mock.web.test.server.MockServerWebExchange
import org.springframework.util.ReflectionUtils
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer
import org.springframework.web.reactive.BindingContext
import org.springframework.web.server.ServerWebInputException
import reactor.test.StepVerifier

/**
 * Kotlin test fixture for [RequestParamMethodArgumentResolver].
 *

 */
class RequestParamMethodArgumentResolverKotlinTests {

	lateinit var resolver: RequestParamMethodArgumentResolver
	lateinit var bindingContext: BindingContext

	lateinit var nullableParamRequired: MethodParameter
	lateinit var nullableParamNotRequired: MethodParameter
	lateinit var nonNullableParamRequired: MethodParameter
	lateinit var nonNullableParamNotRequired: MethodParameter


	@Before
	fun setup() {
		this.resolver = RequestParamMethodArgumentResolver(null, ReactiveAdapterRegistry.getSharedInstance(), true)
		val initializer = ConfigurableWebBindingInitializer()
		initializer.conversionService = DefaultFormattingConversionService()
		bindingContext = BindingContext(initializer)

		val method = ReflectionUtils.findMethod(javaClass, "handle", String::class.java,
				String::class.java, String::class.java, String::class.java)!!

		nullableParamRequired = SynthesizingMethodParameter(method, 0)
		nullableParamNotRequired = SynthesizingMethodParameter(method, 1)
		nonNullableParamRequired = SynthesizingMethodParameter(method, 2)
		nonNullableParamNotRequired = SynthesizingMethodParameter(method, 3)
	}

	@Test
	fun resolveNullableRequiredWithParameter() {
		var exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/path?name=123"))
		var result = resolver.resolveArgument(nullableParamRequired, bindingContext, exchange)
		StepVerifier.create(result).expectNext("123").expectComplete().verify()
	}

	@Test
	fun resolveNullableRequiredWithoutParameter() {
		var exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/"))
		var result = resolver.resolveArgument(nullableParamRequired, bindingContext, exchange)
		StepVerifier.create(result).expectComplete().verify()
	}

	@Test
	fun resolveNullableNotRequiredWithParameter() {
		var exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/path?name=123"))
		var result = resolver.resolveArgument(nullableParamNotRequired, bindingContext, exchange)
		StepVerifier.create(result).expectNext("123").expectComplete().verify()
	}

	@Test
	fun resolveNullableNotRequiredWithoutParameter() {
		var exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/"))
		var result = resolver.resolveArgument(nullableParamNotRequired, bindingContext, exchange)
		StepVerifier.create(result).expectComplete().verify()
	}

	@Test
	fun resolveNonNullableRequiredWithParameter() {
		var exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/path?name=123"))
		var result = resolver.resolveArgument(nonNullableParamRequired, bindingContext, exchange)
		StepVerifier.create(result).expectNext("123").expectComplete().verify()
	}

	@Test
	fun resolveNonNullableRequiredWithoutParameter() {
		var exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/"))
		var result = resolver.resolveArgument(nonNullableParamRequired, bindingContext, exchange)
		StepVerifier.create(result).expectError(ServerWebInputException::class.java).verify()
	}

	@Test
	fun resolveNonNullableNotRequiredWithParameter() {
		var exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/path?name=123"))
		var result = resolver.resolveArgument(nonNullableParamNotRequired, bindingContext, exchange)
		StepVerifier.create(result).expectNext("123").expectComplete().verify()
	}

	@Test
	fun resolveNonNullableNotRequiredWithoutParameter() {
		var exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/"))
		var result = resolver.resolveArgument(nonNullableParamNotRequired, bindingContext, exchange)
		StepVerifier.create(result).expectComplete().verify()
	}


	@Suppress("unused_parameter")
	fun handle(
			@RequestParam("name") nullableParamRequired: String?,
			@RequestParam("name", required = false) nullableParamNotRequired: String?,
			@RequestParam("name") nonNullableParamRequired: String,
			@RequestParam("name", required = false) nonNullableParamNotRequired: String) {
	}

}

