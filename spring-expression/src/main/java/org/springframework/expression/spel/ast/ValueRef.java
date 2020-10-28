package org.springframework.expression.spel.ast;

import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelMessage;
import org.springframework.lang.Nullable;

/**
 * Represents a reference to a value.  With a reference it is possible to get or set the
 * value. Passing around value references rather than the values themselves can avoid
 * incorrect duplication of operand evaluation. For example in 'list[index++]++' without
 * a value reference for 'list[index++]' it would be necessary to evaluate list[index++]
 * twice (once to get the value, once to determine where the value goes) and that would
 * double increment index.
 *

 * @since 3.2
 */
public interface ValueRef {

	/**
	 * Returns the value this ValueRef points to, it should not require expression
	 * component re-evaluation.
	 * @return the value
	 */
	TypedValue getValue();

	/**
	 * Sets the value this ValueRef points to, it should not require expression component
	 * re-evaluation.
	 * @param newValue the new value
	 */
	void setValue(@Nullable Object newValue);

	/**
	 * Indicates whether calling setValue(Object) is supported.
	 * @return true if setValue() is supported for this value reference.
	 */
	boolean isWritable();


	/**
	 * A ValueRef for the null value.
	 */
	class NullValueRef implements ValueRef {

		static final NullValueRef INSTANCE = new NullValueRef();

		@Override
		public TypedValue getValue() {
			return TypedValue.NULL;
		}

		@Override
		public void setValue(@Nullable Object newValue) {
			// The exception position '0' isn't right but the overhead of creating
			// instances of this per node (where the node is solely for error reporting)
			// would be unfortunate.
			throw new SpelEvaluationException(0, SpelMessage.NOT_ASSIGNABLE, "null");
		}

		@Override
		public boolean isWritable() {
			return false;
		}
	}


	/**
	 * A ValueRef holder for a single value, which cannot be set.
	 */
	class TypedValueHolderValueRef implements ValueRef {

		private final TypedValue typedValue;

		private final SpelNodeImpl node;  // used only for error reporting

		public TypedValueHolderValueRef(TypedValue typedValue, SpelNodeImpl node) {
			this.typedValue = typedValue;
			this.node = node;
		}

		@Override
		public TypedValue getValue() {
			return this.typedValue;
		}

		@Override
		public void setValue(@Nullable Object newValue) {
			throw new SpelEvaluationException(
					this.node.getStartPosition(), SpelMessage.NOT_ASSIGNABLE, this.node.toStringAST());
		}

		@Override
		public boolean isWritable() {
			return false;
		}
	}

}
