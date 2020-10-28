package org.springframework.messaging.handler.annotation;

import java.util.function.Predicate;

import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;

/**
 * Predicates for messaging annotations.
 *

 */
public class MessagingPredicates {

	public static DestinationVariablePredicate destinationVar() {
		return new DestinationVariablePredicate();
	}

	public static DestinationVariablePredicate destinationVar(String value) {
		return new DestinationVariablePredicate().value(value);
	}

	public static HeaderPredicate header() {
		return new HeaderPredicate();
	}

	public static HeaderPredicate header(String name) {
		return new HeaderPredicate().name(name);
	}

	public static HeaderPredicate header(String name, String defaultValue) {
		return new HeaderPredicate().name(name).defaultValue(defaultValue);
	}

	public static HeaderPredicate headerPlain() {
		return new HeaderPredicate().noAttributes();
	}


	public static class DestinationVariablePredicate implements Predicate<MethodParameter> {

		@Nullable
		private String value;


		public DestinationVariablePredicate value(@Nullable String name) {
			this.value = name;
			return this;
		}

		public DestinationVariablePredicate noValue() {
			this.value = "";
			return this;
		}

		@Override
		public boolean test(MethodParameter parameter) {
			DestinationVariable annotation = parameter.getParameterAnnotation(DestinationVariable.class);
			return annotation != null && (this.value == null || annotation.value().equals(this.value));
		}
	}


	public static class HeaderPredicate implements Predicate<MethodParameter> {

		@Nullable
		private String name;

		@Nullable
		private Boolean required;

		@Nullable
		private String defaultValue;


		public HeaderPredicate name(@Nullable String name) {
			this.name = name;
			return this;
		}

		public HeaderPredicate noName() {
			this.name = "";
			return this;
		}

		public HeaderPredicate required(boolean required) {
			this.required = required;
			return this;
		}

		public HeaderPredicate defaultValue(@Nullable String value) {
			this.defaultValue = value;
			return this;
		}

		public HeaderPredicate noAttributes() {
			this.name = "";
			this.required = true;
			this.defaultValue = ValueConstants.DEFAULT_NONE;
			return this;
		}

		@Override
		public boolean test(MethodParameter parameter) {
			Header annotation = parameter.getParameterAnnotation(Header.class);
			return annotation != null &&
					(this.name == null || annotation.name().equals(this.name)) &&
					(this.required == null || annotation.required() == this.required) &&
					(this.defaultValue == null || annotation.defaultValue().equals(this.defaultValue));
		}
	}

}
